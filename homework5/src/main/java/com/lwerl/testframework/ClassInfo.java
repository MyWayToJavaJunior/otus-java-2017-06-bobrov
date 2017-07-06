package com.lwerl.testframework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    private Class<?> clazz;
    private List<Method> beforeList = new ArrayList<>();
    private List<Method> afterList = new ArrayList<>();
    private List<Method> testList = new ArrayList<>();

    public ClassInfo(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public List<Method> getBeforeList() {
        return beforeList;
    }

    public void setBeforeList(List<Method> beforeList) {
        this.beforeList = beforeList;
    }

    public List<Method> getAfterList() {
        return afterList;
    }

    public void setAfterList(List<Method> afterList) {
        this.afterList = afterList;
    }

    public List<Method> getTestList() {
        return testList;
    }

    public void setTestList(List<Method> testList) {
        this.testList = testList;
    }
}
