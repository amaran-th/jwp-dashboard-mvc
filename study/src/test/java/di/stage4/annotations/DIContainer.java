package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        try {
            this.beans = createBeans(classes);
            for (Object bean : beans) {
                setFields(bean);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Set<Object> result = new HashSet<>();
        for (Class<?> clazz : classes) {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            result.add(constructor.newInstance());
        }
        return result;
    }

    private void setFields(final Object bean) throws IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isFieldRegisteredAsBean(field)) {
                field.setAccessible(true);
                field.set(bean, findBeanObject(field));
            }
        }
    }

    private boolean isFieldRegisteredAsBean(Field field) {
        return beans.stream()
                .map(Object::getClass)
                .anyMatch(fieldClass -> field.getType().isAssignableFrom(fieldClass));
    }

    private Object findBeanObject(Field field) {
        return beans.stream()
                .filter(bean -> field.getType().isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElse(null);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass().equals(aClass))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
