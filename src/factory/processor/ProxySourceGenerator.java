package factory.processor;

import factory.FactoryPersistenceHandler;
import factory.PersistenceHandlerMissingException;
import factory.ProxyClassNameMapper;
import factory.__FactorySetupForProxy;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ProxySourceGenerator {

    private Filer filer;
    private ProxyClassNameMapper proxyClassNameMapper;

    public ProxySourceGenerator(Filer filer, ProxyClassNameMapper proxyClassNameMapper) {
        this.filer = filer;
        this.proxyClassNameMapper = proxyClassNameMapper;
    }

    void writeSource(TypeElement element) {
        try {
            String proxyClassName = proxyClassNameMapper.map(getCanonicalName(element));
            OutputStream os = filer.createSourceFile("factory." + proxyClassName).openOutputStream();
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
        source.append("package factory;\n\n");

        String className = classElement.getQualifiedName().toString();
        source.append("public class " + proxyClassName + " extends " + className + " {\n\n");

        source.append("    private " + FactoryPersistenceHandler.class.getCanonicalName() + " persistenceHandler;\n\n");

        source.append(getConstructorSource(proxyClassName));
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

    private String getFactorySetupSource(String proxyClassName) {
        StringBuilder source = new StringBuilder();
        source.append("@" + __FactorySetupForProxy.class.getCanonicalName() + "(" + proxyClassName +  ".class)\n");
        source.append("class " + proxyClassName + "_Setup {\n");
        source.append("}\n");
        return source.toString();
    }

    String getConstructorSource(String proxyClassName) {
        StringBuilder source = new StringBuilder();
        source.append("    public " + proxyClassName + "(" + FactoryPersistenceHandler.class.getCanonicalName() + " persistenceHandler) {\n");
        source.append("        this.persistenceHandler = persistenceHandler;\n");
        source.append("    }\n");
        return source.toString();
    }

    String getBuilderSource(String className) {
        StringBuilder source = new StringBuilder();
        source.append("    public " + className + " build() {\n");
        source.append("        return (" + className + ") this;\n");
        source.append("    }\n");
        return source.toString();
    }

    String getCreateSource(String className) {
        StringBuilder source = new StringBuilder();
        source.append("    public " + className + " create() {\n");
        source.append("        " + className + " object = build();\n" );
        source.append("        if (persistenceHandler == null)\n");
        source.append("            throw new " + PersistenceHandlerMissingException.class.getCanonicalName() + "(\"No persistence handlers found.\");\n");
        source.append("        persistenceHandler.built(object);\n");
        source.append("        return object;\n");
        source.append("    }\n");
        return source.toString();
    }

    String getProxySetterSource(ExecutableElement setterElement, String proxyClassName) {
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
                if (element.getReturnType().toString().equals("void") &&
                    element.getSimpleName().toString().startsWith("set") &&
                    element.getParameters().size() == 1) {
                    setterMethodElements.add(element);
                }
            }
        }
        return setterMethodElements;
    }
}
