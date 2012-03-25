package akula.factory.processor;

import akula.factory.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class ProxySourceGenerator {

    public static final String BASE_PACKAGE = "akula.factory";

    private Filer filer;
    private ProxyClassNameMapper proxyClassNameMapper;

    public ProxySourceGenerator(Filer filer, ProxyClassNameMapper proxyClassNameMapper) {
        this.filer = filer;
        this.proxyClassNameMapper = proxyClassNameMapper;
    }

    void writeSource(TypeElement element) {
        try {
            String proxyClassName = proxyClassNameMapper.map(getCanonicalName(element));
            OutputStream os = filer.createSourceFile(BASE_PACKAGE + "." + proxyClassName).openOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.print(getSource(element, proxyClassName));
            pw.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getCanonicalName(TypeElement element) {
        String qualifiedName = element.getQualifiedName().toString();
        return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
    }

    String getSource(TypeElement classElement, String proxyClassName) throws Exception {
        StringBuilder source = new StringBuilder();
        source.append(format("package %s;\n\n", BASE_PACKAGE));

        String className = classElement.getQualifiedName().toString();
        source.append(format("public class %s {\n\n", proxyClassName));

        source.append(format("    private %s object;\n", className));
        source.append(format("    private %s objectDependency;\n", ObjectDependency.class.getCanonicalName()));
        source.append(format("    private %s persistenceHandlerProxy;\n\n", PersistenceHandlerProxy.class.getCanonicalName()));

        source.append(getConstructorSource(proxyClassName, className));
        source.append("\n");

        for (ExecutableElement setterElement : getSetterMethodElements(classElement)) {
            source.append(getProxySetterSource(setterElement, proxyClassName));
        }
        source.append(getBuilderSource(className));
        source.append("\n");

        source.append(getCreateSource(className));
        source.append("}\n\n");

        source.append(getFactorySetupSource(proxyClassName));

        return source.toString();
    }

    String getConstructorSource(String proxyClassName, Object className) {
        StringBuilder source = new StringBuilder();
        source.append(format("    public %s(%s object, %s objectDependency, %s persistenceHandlerProxy) {\n", proxyClassName, className, ObjectDependency.class.getCanonicalName(), PersistenceHandlerProxy.class.getCanonicalName()));
        source.append(format("        this.object = object;\n"));
        source.append(format("        this.objectDependency = objectDependency;\n"));
        source.append(format("        this.persistenceHandlerProxy = persistenceHandlerProxy;\n"));
        source.append(format("    }\n"));
        return source.toString();
    }

    String getBuilderSource(String className) {
        StringBuilder source = new StringBuilder();
        source.append(format("    public %s build() {\n", className));
        source.append(format("        return object;\n"));
        source.append(format("    }\n"));
        return source.toString();
    }

    String getCreateSource(String className) {
        StringBuilder source = new StringBuilder();
        source.append(format("    public %s create() {\n", className));
        source.append(format("        %s object = build();\n", className));
        source.append(format("        this.persistenceHandlerProxy.execute(this.objectDependency);\n"));
        source.append(format("        return object;\n"));
        source.append(format("    }\n"));
        return source.toString();
    }

    private String getFactorySetupSource(String proxyClassName) {
        StringBuilder source = new StringBuilder();
        source.append(format("@%s(%s.class)\n", __SetupForProxy.class.getCanonicalName(), proxyClassName));
        source.append(format("class %s_Setup {\n", proxyClassName));
        source.append(format("}\n"));
        return source.toString();
    }

    String getProxySetterSource(ExecutableElement setterElement, String proxyClassName) {
        StringBuilder source = new StringBuilder();
        String methodName = setterElement.getSimpleName().toString();
        String settingType = setterElement.getParameters().get(0).asType().toString();
        String settingVariable = setterElement.getParameters().get(0).toString();

        source.append(format("    public %s %s(%s %s) {\n", proxyClassName, methodName, settingType, settingVariable));
        source.append(format("        objectDependency.remove(\"%s\");\n", settingVariable));
        source.append(format("        object.%s(%s);\n", methodName, settingVariable));
        source.append(format("        return this;\n"));
        source.append(format("    }\n\n"));
        return source.toString();
    }

    String getProxySetterName(String methodName) {
        StringBuilder proxySetterName = new StringBuilder();
        proxySetterName.append(methodName.substring(3, 4).toLowerCase());
        proxySetterName.append(methodName.substring(4));
        return proxySetterName.toString();
    }

    List<ExecutableElement> getSetterMethodElements(TypeElement rootElement) {
        List<ExecutableElement> setterMethodElements = new ArrayList<ExecutableElement>();
        for (Element enclosedElement : rootElement.getEnclosedElements()) {
            if (enclosedElement instanceof ExecutableElement) {
                ExecutableElement element = (ExecutableElement) enclosedElement;
                if (element.getReturnType().toString().equals("void") && element.getParameters().size() == 1) {
                    setterMethodElements.add(element);
                }
            }
        }
        return setterMethodElements;
    }
}
