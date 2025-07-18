package ru.pavbatol.myplace.gateway.app.config.deprecation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Spring BeanPostProcessor that detects and logs usage of deprecated beans and bean methods
 * within the application's package hierarchy. Automatically determines the root package
 * (e.g. 'ru.pavbatol.myplace') to only check project-specific beans.
 */
@Slf4j
@Component
public class DeprecatedBeanLogger implements BeanPostProcessor {
    private final String rootPackage;

    /**
     * Constructs the detector and auto-discovers the root package by analyzing
     * this class's package location (takes first 3 package segments).
     */
    public DeprecatedBeanLogger() {
        this.rootPackage = detectRootPackage(getClass());
        log.debug("Auto-detected root package: {}", rootPackage);
    }

    /**
     * Processes each bean after initialization, logging warnings when detecting:
     * <ul>
     *   <li>Bean classes annotated with {@code @Deprecated}</li>
     *   <li>{@code @Bean} methods annotated with {@code @Deprecated}</li>
     * </ul>
     * Only checks beans within the detected root package.
     *
     * @param bean     The bean instance
     * @param beanName The name of the bean
     * @return The original bean (unmodified)
     * @throws BeansException if bean processing fails
     */
    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);

        if (!isInOurProject(targetClass)) {
            return bean;
        }

        checkClassDeprecation(targetClass, beanName);
        checkBeanMethodsDeprecation(targetClass, beanName);

        return bean;
    }

    private boolean isInOurProject(Class<?> clazz) {
        return clazz.getPackageName().startsWith(rootPackage);
    }

    private void checkClassDeprecation(Class<?> clazz, String beanName) {
        if (clazz.isAnnotationPresent(Deprecated.class)) {
            log.warn("⚠\uFE0F DEPRECATED BEAN: {} (Class: {})", beanName, clazz.getSimpleName());
        }

        for (Class<?> iface : clazz.getInterfaces()) {
            if (iface.isAnnotationPresent(Deprecated.class)) {
                log.warn("⚠\uFE0F DEPRECATED BEAN INTERFACE: {} (Interface: {})", beanName, iface.getSimpleName());
                return;
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass.isAnnotationPresent(Deprecated.class)) {
            log.warn("⚠\uFE0F DEPRECATED BEAN SUPERCLASS: {} (Superclass: {})", beanName, superClass.getSimpleName());
        }
    }

    private void checkBeanMethodsDeprecation(Class<?> clazz, String beanName) {
        ReflectionUtils.doWithMethods(
                clazz,
                method -> {
                    if (method.isAnnotationPresent(Deprecated.class)) {
                        log.warn("⚠\uFE0F DEPRECATED BEAN METHOD: {}.{}() (Class: {})", beanName, method.getName(), clazz.getSimpleName());
                    }
                },
                this::isBeanMethod
        );
    }

    private boolean isBeanMethod(Method method) {
        return method.isAnnotationPresent(org.springframework.context.annotation.Bean.class);
    }

    private static String detectRootPackage(Class<?> aClass) {
        String currentPackage = aClass.getPackageName();
        String[] parts = currentPackage.split("\\.");

        return parts[0];
    }
}
