import java.io.IOException;

public class Main {
    public static void main(String... args) throws IOException,IllegalAccessException, InstantiationException {
        SomeBean sb = (new Injector<SomeBean>("src/inj.properties").inject(new SomeBean()));
        sb.foo();
    }
}