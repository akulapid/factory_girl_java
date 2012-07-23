package akula.factory.processor;

import akula.factory.Factory;
import akula.factory.ProxyClassNameMapper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.String.format;

public class FactoriesSourceGenerator {

    public static final String BASE_PACKAGE = "akula.factory";
    public static final String SOURCE_FILE = "Factories";

    Filer filer;
    ProxyClassNameMapper proxyClassNameMapper;

    public FactoriesSourceGenerator(Filer filer, ProxyClassNameMapper proxyClassNameMapper) {
        this.filer = filer;
        this.proxyClassNameMapper = proxyClassNameMapper;
    }

    public void writeSource(TypeElement element, String factoryName) {
        if (factoryName == null || factoryName.isEmpty())
            factoryName = element.getSimpleName().toString();
        System.out.println(format("FACTORY LOG: generating factory for [%s].", factoryName));
        try {
            OutputStream os = filer.createSourceFile(BASE_PACKAGE + "." + factoryName + "Factory").openOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.print(getSource(element, factoryName));
            pw.close();
            os.close();
            System.out.println("FACTORY LOG: generated factories.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getSource(TypeElement element, String factoryName) {
        StringBuilder source = new StringBuilder();
        source.append(format("package %s;\n\n", BASE_PACKAGE));
        source.append(format("public class %sFactory {\n\n", factoryName));
        source.append(getFactorySource(element, factoryName));
        source.append(format("}"));
        return source.toString();
    }

    String getFactorySource(TypeElement element, String factoryName) {
        String classSimpleName = element.getSimpleName().toString();
        String classFQName = element.getQualifiedName().toString();
        String proxyClassName = proxyClassNameMapper.map(classSimpleName);

        StringBuilder source = new StringBuilder();
        source.append(format("    public static %s.%s %s() {\n", BASE_PACKAGE, proxyClassName, "new" + factoryName));
        source.append(format("        return %s.Instantiator.createProxy(%s.class, %s.class, \"%s\");\n", BASE_PACKAGE, proxyClassName, classFQName, factoryName));
        source.append(format("    }\n\n"));
        return source.toString();
    }

    public void writeSource(List<Pair<TypeElement,String>> elements) {
        String factories = "";
        for (Pair pair : elements)
            factories += format("%s, ", ((TypeElement) pair.getLeft()).getQualifiedName().toString());
        factories = factories.length() > 2? factories.substring(0, factories.length() - 2) : factories;
        System.out.println(format("FACTORY LOG: generating factories [%s]..", factories));

        try {
            FileObject fileObject = filer.getResource(StandardLocation.CLASS_OUTPUT, BASE_PACKAGE, SOURCE_FILE);
            System.out.println(format("FACTORY LOG: reading existing factories source %s", fileObject.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            OutputStream os = filer.createSourceFile(BASE_PACKAGE + "." + SOURCE_FILE).openOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.print(getSource(elements));
            pw.close();
            os.close();
            System.out.println("FACTORY LOG: generated factories.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getSource(List<Pair<TypeElement, String>> elements) {
        StringBuilder source = new StringBuilder();
        source.append(format("package %s;\n\n", BASE_PACKAGE));
        source.append(format("public class Factories {\n\n"));
        for (Pair<TypeElement, String> elementSetupPair : elements)
            source.append(getFactorySource(elementSetupPair));
        source.append(format("}"));
        return source.toString();
    }

    String getFactorySource(Pair<TypeElement, String> elementSetupPair) {
        String classSimpleName = elementSetupPair.getLeft().getSimpleName().toString();
        String classFQName = elementSetupPair.getLeft().getQualifiedName().toString();
        String proxyClassName = proxyClassNameMapper.map(classSimpleName);
        String setupName = elementSetupPair.getRight();
        String factoryName = "new" + (setupName.isEmpty()? classSimpleName : setupName);

        StringBuilder source = new StringBuilder();
        source.append(format("    public static %s.%s %s() {\n", BASE_PACKAGE, proxyClassName, factoryName));
        source.append(format("        return %s.Instantiator.createProxy(%s.class, %s.class, \"%s\");\n", BASE_PACKAGE, proxyClassName, classFQName, setupName));
        source.append(format("    }\n\n"));
        return source.toString();
    }
}
