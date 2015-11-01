package com.github.ompc.greys.core.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Greys封装的方法<br/>
 * 主要用来封装构造函数Constructor/Method
 * Created by vlinux on 15/10/24.
 */
public class GaMethod {

    private final int type;
    private final Constructor<?> constructor;
    private final Method method;

    /*
     * 构造方法
     */
    private static final int TYPE_INIT = 1 << 1;

    /*
     * 普通方法
     */
    private static final int TYPE_METHOD = 1 << 2;

    /**
     * 是否构造方法
     *
     * @return true/false
     */
    public boolean isInit() {
        return (TYPE_INIT & type) == TYPE_INIT;
    }

    /**
     * 是否普通方法
     *
     * @return true/false
     */
    public boolean isMethod() {
        return (TYPE_METHOD & type) == TYPE_METHOD;
    }

    /**
     * 获取方法名称
     *
     * @return 返回方法名称
     */
    public String getName() {
        return isInit()
                ? "<init>"
                : method.getName();
    }

    @Override
    public String toString() {
        return isInit()
                ? constructor.toString()
                : method.toString();
    }

    public boolean isAccessible() {
        return isInit()
                ? constructor.isAccessible()
                : method.isAccessible();
    }

    public void setAccessible(boolean accessFlag) {
        if (isInit()) {
            constructor.setAccessible(accessFlag);
        } else {
            method.setAccessible(accessFlag);
        }
    }

    public Object invoke(Object target, Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return isInit()
                ? constructor.newInstance(args)
                : method.invoke(target, args);
    }

    private GaMethod(int type, Constructor<?> constructor, Method method) {
        this.type = type;
        this.constructor = constructor;
        this.method = method;
    }

    /**
     * 封装构造函数
     *
     * @param constructor 构造函数
     * @return GaMethod封装
     */
    public static GaMethod newConstructor(Constructor<?> constructor) {
        return new GaMethod(TYPE_INIT, constructor, null);
    }

    /**
     * 封装普通方法(包括静态方法)
     *
     * @param method 方法
     * @return GaMethod封装
     */
    public static GaMethod newMethod(Method method) {
        return new GaMethod(TYPE_METHOD, null, method);
    }

}
