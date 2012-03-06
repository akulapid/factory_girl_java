package factory.processor;

import factory.FactorySetup;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("factory.FactorySetup")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class FactorySetupProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotationElements, RoundEnvironment roundEnvironment) {
        for (TypeElement annotationElement : annotationElements) {
            for (Element factorySetupElement : roundEnvironment.getElementsAnnotatedWith(annotationElement)) {
                if (factorySetupElement instanceof TypeElement)
                    handleFactorySetupAnnotatedClass(getFactorySetupType(factorySetupElement));
            }
        }
        return true;
    }

    private TypeElement getFactorySetupType(Element factorySetupElement) {
        // see http://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor
        try {
            factorySetupElement.getAnnotation(FactorySetup.class).type();
        } catch (MirroredTypeException e) {
            return (TypeElement) ((DeclaredType) e.getTypeMirror()).asElement();
        }
        throw new RuntimeException("error: did not throw MirroredTypeException as expected.");
    }

    private void handleFactorySetupAnnotatedClass(TypeElement element) {
        try {
            writeProxySource(element);
        } catch (Exception e) {
            log("Error", stackTraceToString(e));
        }
    }

    private void writeProxySource(TypeElement element) throws Exception {
        String proxyClassName = getCanonicalName(element) + "_GeneratedFactoryProxy";
        OutputStream os = processingEnv.getFiler().createSourceFile("factories." + proxyClassName).openOutputStream();
        PrintWriter pw = new PrintWriter(os);
        pw.print(getProxySource(element, proxyClassName));
        pw.close();
        os.close();
    }

    private String getCanonicalName(TypeElement element) {
        String qualifiedName = element.getQualifiedName().toString();
        return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
    }

    private String getProxySource(TypeElement classElement, String proxyClassName) throws Exception {
        StringBuilder source = new StringBuilder();
        source.append("package factories;\n\n");
        String className = classElement.getQualifiedName().toString();
        source.append("public class " + proxyClassName + " extends " + className + " {\n\n");
        for (ExecutableElement setterElement : getSetterMethodElements(classElement)) {
            source.append(getProxySetterSource(setterElement, proxyClassName));
        }
        source.append(getBuilderSource(className));
        source.append("}");
        return source.toString();
    }

    private String getBuilderSource(String className) {
        StringBuilder source = new StringBuilder();
        source.append("    public " + className + " build() {\n");
        source.append("        return (" + className + ") this;\n");
        source.append("    }\n");
        return source.toString();
    }

    private String getProxySetterSource(ExecutableElement setterElement, String proxyClassName) {
        StringBuilder source = new StringBuilder();
        String methodName = setterElement.getSimpleName().toString();
        String settingType = setterElement.getParameters().get(0).asType().toString();
        String settingVariable = setterElement.getParameters().get(0).toString();

        source.append("    public " + proxyClassName + " " + getProxySetterName(methodName) + "(" + settingType + " " + settingVariable + ") {\n");
        source.append("        " + methodName + "(" + settingVariable + ");\n");
        source.append("        return this;\n");
        source.append("    }\n\n");
        return source.toString();
    }

    private String getProxySetterName(String methodName) {
        StringBuilder proxySetterName = new StringBuilder();
        proxySetterName.append(methodName.substring(3, 4).toLowerCase());
        proxySetterName.append(methodName.substring(4));
        return proxySetterName.toString();
    }

    private List<ExecutableElement> getSetterMethodElements(TypeElement rootElement) {
        List<ExecutableElement> setterMethodElements = new ArrayList<ExecutableElement>();
        for (Element enclosedElement : rootElement.getEnclosedElements()) {
            if (enclosedElement instanceof ExecutableElement) {
                ExecutableElement element = (ExecutableElement) enclosedElement;
                if (element.getReturnType().toString().equals("void") &&
                    element.getSimpleName().toString().startsWith("set") &&
                    element.getParameters().size() == 1) {
                    setterMethodElements.add(element);
                }
            }
        }
        return setterMethodElements;
    }

    private void log(String title, String message) {
        try {
            OutputStream os = processingEnv.getFiler().createSourceFile(title).openOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.print("public class " + title + " {");
            pw.print("/*" + message + "*/");
            pw.print("}");
            pw.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
