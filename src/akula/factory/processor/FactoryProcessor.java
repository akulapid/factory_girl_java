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

@SupportedAnnotationTypes("akula.factory.Factory")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class FactoryProcessor extends AbstractProcessor {

    private ProxyClassNameMapper proxyClassNameMapper;
    private FactoriesSourceGenerator factoriesSourceGenerator;
    private ProxySourceGenerator proxySourceGenerator;

    private List<Pair<TypeElement, String>> elements = new ArrayList<Pair<TypeElement, String>>();

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
        for (TypeElement annotationElement : annotationElements) {
            for (Element factorySetupElement : roundEnvironment.getElementsAnnotatedWith(annotationElement)) {
                if (factorySetupElement instanceof TypeElement) {
                    TypeElement element = getFactorySetupType(factorySetupElement);
                    elements.add(new ImmutablePair<TypeElement, String>(
                        element, factorySetupElement.getAnnotation(Factory.class).name()
                    ));
                    proxySourceGenerator.writeSource(element);
                }
            }
        }
        if (roundEnvironment.processingOver())
            factoriesSourceGenerator.writeSource(elements);
        return true;
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
