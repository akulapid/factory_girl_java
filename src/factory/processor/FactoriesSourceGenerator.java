package factory.processor;

import factory.ProxyClassNameMapper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.String.format;

public class FactoriesSourceGenerator {

    Filer filer;
    ProxyClassNameMapper proxyClassNameMapper;

    public FactoriesSourceGenerator(Filer filer, ProxyClassNameMapper proxyClassNameMapper) {
        this.filer = filer;
        this.proxyClassNameMapper = proxyClassNameMapper;
    }

    public void writeSource(List<Pair<TypeElement,String>> elements) {
        try {
            OutputStream os = filer.createSourceFile("factory.Factory").openOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.print(getSource(elements));
            pw.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getSource(List<Pair<TypeElement, String>> elements) {
        StringBuilder source = new StringBuilder();
        source.append(format("package factory;\n\n"));
        source.append(format("public class Factory {\n\n"));
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
        source.append(format("    public static factory.%s %s() {\n", proxyClassName, factoryName));
        source.append(format("        return factory.Instantiator.createProxy(%s.class, %s.class, \"%s\");\n", proxyClassName, classFQName, setupName));
        source.append(format("    }\n\n"));
        return source.toString();
    }
}
