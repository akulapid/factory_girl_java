package factory.processor;

import factory.ProxyClassNameMapper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

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
        source.append("package factory;\n\n");
        source.append("public class Factory {\n\n");
        for (Pair<TypeElement, String> elementAliasPair : elements)
            source.append(getFactorySource(elementAliasPair));
        source.append("}");
        return source.toString();
    }

    String getFactorySource(Pair<TypeElement, String> elementAliasPair) {
        String classSimpleName = elementAliasPair.getLeft().getSimpleName().toString();
        String proxyClassName = proxyClassNameMapper.map(classSimpleName);
        String alias = elementAliasPair.getRight();
        String factoryName = "new" + (alias.isEmpty()? classSimpleName : alias);

        StringBuilder source = new StringBuilder();
        source.append("    public static factory." + proxyClassName + " " + factoryName + "() {\n");
        source.append("        return factory.Instantiator.createProxy(" + proxyClassName + ".class, \"" + alias + "\");\n");
        source.append("    }\n\n");
        return source.toString();
    }
}
