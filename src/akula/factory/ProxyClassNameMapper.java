package akula.factory;

public class ProxyClassNameMapper {

    public String map(String className) {
        return className + "_GeneratedFactoryProxy";
    }
}
