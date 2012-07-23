package akula.factory.processor;

import akula.factory.ProxyClassNameMapper;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.io.OutputStream;
import java.io.PrintWriter;

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
}
