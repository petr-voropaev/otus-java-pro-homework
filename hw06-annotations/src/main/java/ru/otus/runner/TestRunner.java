package ru.otus.runner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;
import ru.otus.helper.ReflectionHelper;

public class TestRunner {

    public void runTests(String... classNames) {
        for (String className : classNames) {
            List<TestResult> results = new ArrayList<>();

            try {
                Class<?> testClass = Class.forName(className);
                List<Method> beforeMethods = ReflectionHelper.findAnnotatedMethods(testClass, Before.class);
                List<Method> afterMethods = ReflectionHelper.findAnnotatedMethods(testClass, After.class);
                List<Method> testMethods = ReflectionHelper.findAnnotatedMethods(testClass, Test.class);

                for (Method testMethod : testMethods) {
                    TestResult result = executeTest(testClass, beforeMethods, afterMethods, testMethod);
                    results.add(result);
                }

                printStatistics(results);
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className);
            }
        }
    }

    private TestResult executeTest(
            Class<?> testClass, List<Method> beforeMethods, List<Method> afterMethods, Method testMethod) {
        String testName = testMethod.getName();

        try {
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
            ReflectionHelper.executeMethods(testInstance, beforeMethods);
            ReflectionHelper.executeMethods(testInstance, Collections.singletonList(testMethod));
            ReflectionHelper.executeMethods(testInstance, afterMethods);
            return new TestResult(testName, true, null);
        } catch (Exception e) {
            return new TestResult(testName, false, e);
        }
    }

    private void printStatistics(List<TestResult> results) {
        int total = results.size();
        long passed = results.stream().filter(TestResult::success).count();
        long failed = total - passed;

        System.out.println("\n========== TEST STATISTICS ==========");
        System.out.printf("Total: %d%n", total);
        System.out.printf("Passed: %d%n", passed);
        System.out.printf("Failed: %d%n", failed);

        if (failed > 0) {
            System.out.println("\nFailed tests:");
            results.forEach(result -> {
                if (!result.success()) {
                    System.out.printf(
                            "  - %s: %s%n",
                            result.methodName(), result.exception().getCause().getMessage());
                }
            });
        }
        System.out.println("========================================");
    }

    private record TestResult(String methodName, boolean success, Exception exception) {}
}
