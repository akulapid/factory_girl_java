package akula.factory.processor;

import akula.factory.Factory;
import akula.factory.ProxyClassNameMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@SupportedAnnotationTypes("akula.factory.Factory")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class FactoryProcessor extends AbstractProcessor {

    private ProxyClassNameMapper proxyClassNameMapper;
    private FactoriesSourceGenerator factoriesSourceGenerator;
    private ProxySourceGenerator proxySourceGenerator;

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        proxyClassNameMapper = new ProxyClassNameMapper();
        Filer filer = processingEnvironment.getFiler();
        factoriesSourceGenerator = new FactoriesSourceGenerator(filer, proxyClassNameMapper);
        proxySourceGenerator = new ProxySourceGenerator(filer, proxyClassNameMapper);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotationElements, RoundEnvironment roundEnvironment) {
        System.out.println(format("FACTORY LOG: processing annotations [%s], round %s", arrayToString(annotationElements), roundEnvironment.toString()));

        for (TypeElement annotationElement : annotationElements) {
            Set<? extends Element> elementsAnnotatedWithFactory = roundEnvironment.getElementsAnnotatedWith(annotationElement);
            System.out.println(format("FACTORY LOG: found @Factory annotated elements: [%s]", arrayToString(elementsAnnotatedWithFactory)));

            for (Element factoryElement : elementsAnnotatedWithFactory) {
                System.out.println(format("FACTORY LOG: reading %s", factoryElement));

                if (factoryElement instanceof TypeElement) {
                    TypeElement element = getFactorySetupType(factoryElement);
                    proxySourceGenerator.writeSource(element);
                    factoriesSourceGenerator.writeSource(element, factoryElement.getAnnotation(Factory.class).name());
                }
            }
        }
        System.out.println("FACTORY LOG: round completed.");
        return true;
    }

    private String arrayToString(Set<? extends Element> elements) {
        String string = "";
        for (Element element : elements)
            string += element + ", ";
        return string.length() > 2? string.substring(0, string.length() - 2) : string;
    }

    // see http://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor
    private TypeElement getFactorySetupType(Element factorySetupElement) {
        try {
            factorySetupElement.getAnnotation(Factory.class).value();
        } catch (MirroredTypeException e) {
            return (TypeElement) ((DeclaredType) e.getTypeMirror()).asElement();
        }
        throw new RuntimeException("error: did not throw MirroredTypeException as expected.");
    }
}
