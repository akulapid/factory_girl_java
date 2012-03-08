package factory.processor;

import factory.ProxyClassNameMapper;

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

    public void writeSource(List<TypeElement> elements) {
        try {
            OutputStream os = filer.createSourceFile("factories.Factory").openOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.print(getSource(elements));
            pw.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getSource(List<TypeElement> elements) {
        StringBuilder source = new StringBuilder();
        source.append("package factories;\n\n");
        source.append("public class Factory {\n\n");
        for (TypeElement element : elements)
            source.append(getFactorySource(element));
        source.append("}");
        return source.toString();
    }

    // TODO: will break if two classes in diffent packages have the same name, alias will fix this
    String getFactorySource(TypeElement element) {
        StringBuilder source = new StringBuilder();
        String classSimpleName = element.getSimpleName().toString();
        String proxyClassName = proxyClassNameMapper.map(classSimpleName);
        source.append("    public static factories." + proxyClassName + " new" + classSimpleName + "() {\n");
        source.append("        return new " + proxyClassName + "();\n");
        source.append("    }\n");
        return source.toString();
    }
}
