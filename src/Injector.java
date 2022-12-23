import java.util.Properties;
import java.lang.annotation.Annotation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;


/**
 *
 * class Injector, который осуществляет
 * внедрение зависимостей в любой объект , который содержит поля,
 * помеченные нашей аннотацией (@AutoInjectable)
 */
class Injector<T> {

    private Properties properties;

    /**
     * Конструктор (инициализация объекта конфигурации)
     */
    Injector(String pathToPropertiesFile) throws IOException {
        // инициализируем объект конфигурации
        properties = new Properties();
        properties.load(new FileInputStream(new File(pathToPropertiesFile)));
    }


    /**
     * inject принимает произвольный объект, исследует существующие
     * в нем поля, и смотрит, аннотированы ли они нужной аннотацией.
     * Если да, то тогда он смотрит тип этого поля и ищет
     * реализацию в файле inj.properties
     */
    T inject(T obj) throws IOException, IllegalAccessException, InstantiationException {
        Class dep;
        Class cl = obj.getClass();
        Field[] fields = cl.getDeclaredFields();

        for (Field field: fields){
            // есть ли нужная аннотация в исследуемом поле
            Annotation a = field.getAnnotation(AutoInjectable.class);
            if (a != null){
                String[] fieldType = field.getType().toString().split(" ");

                String equalsClassName = properties.getProperty(fieldType[1], null);
                if (equalsClassName != null){
                    try {
                        dep = Class.forName(equalsClassName);
                    } catch (ClassNotFoundException e){
                        System.out.println("Не найден class для " + equalsClassName);
                        continue;
                    }
                    field.setAccessible(true); //так как private
                    field.set(obj, dep.newInstance());
                }
                else
                    System.out.println("Не найдены properties для файла типа " + fieldType[1]);
            }
        }
        return obj;
    }
}