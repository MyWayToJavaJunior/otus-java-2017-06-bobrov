package com.lwerl.testframework;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.lwerl.testframework.constant.Literals.*;

public class ClassResult {

    private Class<?> testClass;
    private Set<MethodResult> methodResultSet;
    private boolean isExecuted;
    private String causeDescription;
    private Throwable cause;

    public ClassResult(Class<?> testClass) {
        this.testClass = testClass;
        this.isExecuted = true;
        this.methodResultSet = new LinkedHashSet<>();
        this.causeDescription = EMPTY_STRING;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public Set<MethodResult> getMethodResultSet() {
        return methodResultSet;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public String getCauseDescription() {
        return causeDescription;
    }

    public Throwable getCause() {
        return cause;
    }

    public void fail(String causeDescription, Throwable cause) {
        this.isExecuted = false;
        this.causeDescription = causeDescription;
        this.cause = cause;
    }

    public class MethodResult {
        private Method testMethod;
        private boolean isPassed;
        private long duration;
        private String causeDescription;
        private Throwable cause;

        public MethodResult(Method testMethod, boolean isPassed, long duration, String causeDescription, Throwable cause) {
            this.testMethod = testMethod;
            this.isPassed = isPassed;
            this.causeDescription = causeDescription;
            this.duration = duration;
            this.cause = cause;
        }

        public Method getTestMethod() {
            return testMethod;
        }

        public boolean isPassed() {
            return isPassed;
        }

        public long getDuration() {
            return duration;
        }

        public String getCauseDescription() {
            return causeDescription;
        }

        public Throwable getCause() {
            return cause;
        }

        @Override
        public int hashCode() {
            return testMethod.hashCode();
        }
    }
}
