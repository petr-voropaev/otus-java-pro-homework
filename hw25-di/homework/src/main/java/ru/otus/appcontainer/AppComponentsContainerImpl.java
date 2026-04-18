package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfigs(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> initialConfigClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        processConfigs(initialConfigClasses.toArray(Class<?>[]::new));
    }

    private void processConfigs(Class<?>[] initialConfigClasses) {
        Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(configClass -> configClass
                        .getAnnotation(AppComponentsContainerConfig.class)
                        .order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Object configInstance = newConfigInstance(configClass);

        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .forEach(method -> createComponent(configInstance, method));
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object newConfigInstance(Class<?> configClass) {
        try {
            var constructor = configClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not create config instance: " + configClass.getName(), e);
        }
    }

    private void createComponent(Object configInstance, Method method) {
        String componentName = method.getAnnotation(AppComponent.class).name();

        if (appComponentsByName.containsKey(componentName)) {
            throw new IllegalArgumentException("Duplicate component: " + componentName);
        }

        Object[] args = Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .toArray();

        try {
            method.setAccessible(true);
            Object component = method.invoke(configInstance, args);
            appComponents.add(component);
            appComponentsByName.put(componentName, component);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not create component: " + componentName, e);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> components = appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .map(componentClass::cast)
                .toList();

        if (components.size() != 1) {
            throw new IllegalArgumentException("No component found: " + componentClass.getName());
        }

        return components.getFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);

        if (component == null) {
            throw new IllegalArgumentException("No component found: " + componentName);
        }

        return (C) component;
    }
}
