package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.Log;

public class IocLogging {

    private static final Logger logger = LoggerFactory.getLogger(IocLogging.class);

    private IocLogging() {}

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LoggingInvocationHandler(target));
    }

    private static class LoggingInvocationHandler implements InvocationHandler {
        private final Object target;
        private final Set<Method> contextMethod = new HashSet<>();

        public LoggingInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (contextMethod.contains(method)) {
                logger.info("executed method: {}, param(s): {}", method.getName(), Arrays.toString(args));
            } else {
                Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
                if (targetMethod.isAnnotationPresent(Log.class)) {
                    contextMethod.add(method);
                    logger.info("executed method: {}, param(s): {}", method.getName(), Arrays.toString(args));
                }
            }

            return method.invoke(target, args);
        }
    }
}
