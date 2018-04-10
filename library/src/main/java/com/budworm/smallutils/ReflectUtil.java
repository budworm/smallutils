package com.budworm.smallutils;

import android.annotation.SuppressLint;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 一个拥有流畅特性(Fluent-API)的反射工具类,
 * 使用起来就像直接调用一样流畅易懂.
 */
@SuppressLint("DefaultLocale")
public class ReflectUtil {

    private final Object object;
    private final boolean isClass;

    private ReflectUtil(Class<?> type) {
        this.object = type;
        this.isClass = true;
    }

    private ReflectUtil(Object object) {
        this.object = object;
        this.isClass = false;
    }

    /**
     * 根据指定的类名构建反射工具类
     *
     * @param name 类的全名
     * @return 反射工具类
     * @throws 如果反射出现意外
     * @see #on(Class)
     */
    public static ReflectUtil on(String name) throws Exception {
        return on(forName(name));
    }

    /**
     * 从指定的类加载起寻找类,并构建反射工具类
     *
     * @param name        类的全名
     * @param classLoader 需要构建工具类的类的类加载器
     *                    loaded.
     * @return 反射工具类
     * @throws Exception 如果反射出现意外
     * @see #on(Class)
     */
    public static ReflectUtil on(String name, ClassLoader classLoader) throws Exception {
        return on(forName(name, classLoader));
    }

    /**
     * 根据指定的类构建反射工具类
     * <p>
     * 当你需要访问静态字段的时候本方法适合你,
     * 你还可以通过调用 {@link #create(Object...)} 创建一个对象.
     *
     * @param clazz 需要构建反射工具类的类
     * @return 反射工具类
     */
    public static ReflectUtil on(Class<?> clazz) {
        return new ReflectUtil(clazz);
    }

    // ---------------------------------------------------------------------  
    // 构造器  
    // ---------------------------------------------------------------------  

    /**
     * Wrap an object.
     * <p>
     * Use this when you want to access instance fields and methods on any
     * {@link Object}
     *
     * @param object The object to be wrapped
     * @return A wrapped object, to be used for further reflection.
     */
    public static ReflectUtil on(Object object) {
        return new ReflectUtil(object);
    }

    /**
     * 让一个{@link AccessibleObject}可访问.
     *
     * @param accessible
     * @param <T>
     * @return
     */
    public static <T extends AccessibleObject> T accessible(T accessible) {
        if (accessible == null) {
            return null;
        }

        if (accessible instanceof Member) {
            Member member = (Member) accessible;

            if (Modifier.isPublic(member.getModifiers()) &&
                    Modifier.isPublic(member.getDeclaringClass().getModifiers())) {

                return accessible;
            }
        }
        if (!accessible.isAccessible()) {
            accessible.setAccessible(true);
        }

        return accessible;
    }

    // ---------------------------------------------------------------------  
    // Fluent Reflection API  
    // ---------------------------------------------------------------------  

    /**
     * 将给定字符串的开头改为小写.
     *
     * @param string
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static String property(String string) {
        int length = string.length();

        if (length == 0) {
            return "";
        } else if (length == 1) {
            return string.toLowerCase();
        } else {
            return string.substring(0, 1).toLowerCase() + string.substring(1);
        }
    }

    private static ReflectUtil on(Constructor<?> constructor, Object... args) throws Exception {
        try {
            return on(accessible(constructor).newInstance(args));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static ReflectUtil on(Method method, Object object, Object... args) throws
            Exception {
        try {
            accessible(method);

            if (method.getReturnType() == void.class) {
                method.invoke(object, args);
                return on(object);
            } else {
                return on(method.invoke(object, args));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 取得内部维护的对象.
     */
    private static Object unwrap(Object object) {
        if (object instanceof ReflectUtil) {
            return ((ReflectUtil) object).get();
        }

        return object;
    }

    /**
     * 将Object数组转换为其类型的数组.
     * 如果对象中包含null,我们用NULL.class代替.
     *
     * @see Object#getClass()
     */
    private static Class<?>[] types(Object... values) {
        if (values == null) {
            return new Class[0];
        }

        Class<?>[] result = new Class[values.length];

        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            result[i] = value == null ? null : value.getClass();
        }

        return result;
    }

    /**
     * 取得一个类,此操作会初始化类的static区域.
     *
     * @see Class#forName(String)
     */
    private static Class<?> forName(String name) throws Exception {
        try {
            return Class.forName(name);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static Class<?> forName(String name, ClassLoader classLoader) throws Exception {
        try {
            return Class.forName(name, true, classLoader);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 如果给定的Class是原始类型,那么将其包装为对象类型,
     * 否则返回本身.
     */
    public static Class<?> wrapper(Class<?> type) {
        if (type == null) {
            return null;
        } else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }

        return type;
    }

    /**
     * 取得内部维护的实际对象
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) object;
    }

    /**
     * 设置指定字段为指定值
     *
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    public ReflectUtil set(String name, Object value) throws
            Exception {
        try {
            Field field = field0(name);
            field.setAccessible(true);
            field.set(object, unwrap(value));
            return this;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * @param name
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T get(String name) throws Exception {
        return field(name).get();
    }

    /**
     * 取得指定名称的字段
     *
     * @param name
     * @return
     * @throws Exception
     */
    public ReflectUtil field(String name) throws Exception {
        try {
            Field field = field0(name);
            return on(field.get(object));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private Field field0(String name) throws Exception {
        Class<?> type = type();

        // 先尝试取得公有字段  
        try {
            return type.getField(name);
        }

        //此时尝试非公有字段  
        catch (NoSuchFieldException e) {
            do {
                try {
                    return accessible(type.getDeclaredField(name));
                } catch (NoSuchFieldException ignore) {
                }

                type = type.getSuperclass();
            }
            while (type != null);

            throw new Exception(e);
        }
    }

    /**
     * 取得一个Map,map中的key为字段名,value为字段对应的反射工具类
     *
     * @return
     */
    public Map<String, ReflectUtil> fields() {
        Map<String, ReflectUtil> result = new LinkedHashMap<String, ReflectUtil>();
        Class<?> type = type();

        do {
            for (Field field : type.getDeclaredFields()) {
                if (!isClass ^ Modifier.isStatic(field.getModifiers())) {
                    String name = field.getName();

                    if (!result.containsKey(name))
                        try {
                            result.put(name, field(name));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }

            type = type.getSuperclass();
        }
        while (type != null);

        return result;
    }

    /**
     * 调用指定的无参数方法
     *
     * @param name
     * @return
     * @throws androidx.pluginmgr.reflect.Exception
     */
    public ReflectUtil call(String name) throws Exception {
        return call(name, new Object[0]);
    }

    /**
     * 调用方法根据传入的参数
     *
     * @param name
     * @param args
     * @return
     * @throws androidx.pluginmgr.reflect.Exception
     */
    public ReflectUtil call(String name, Object... args) throws Exception {
        Class<?>[] types = types(args);


        try {
            Method method = exactMethod(name, types);
            return on(method, object, args);
        } catch (NoSuchMethodException e) {
            try {
                Method method = similarMethod(name, types);
                return on(method, object, args);
            } catch (NoSuchMethodException e1) {
                throw new Exception(e1);
            }
        }
    }

    private Method exactMethod(String name, Class<?>[] types) throws NoSuchMethodException {
        Class<?> type = type();

        try {
            return type.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            do {
                try {
                    return type.getDeclaredMethod(name, types);
                } catch (NoSuchMethodException ignore) {
                }

                type = type.getSuperclass();
            }
            while (type != null);

            throw new NoSuchMethodException();
        }
    }

    /**
     * 根据参数和名称匹配方法,如果找不到方法,
     */
    private Method similarMethod(String name, Class<?>[] types) throws NoSuchMethodException {
        Class<?> type = type();

        for (Method method : type.getMethods()) {
            if (isSimilarSignature(method, name, types)) {
                return method;
            }
        }

        do {
            for (Method method : type.getDeclaredMethods()) {
                if (isSimilarSignature(method, name, types)) {
                    return method;
                }
            }

            type = type.getSuperclass();
        }
        while (type != null);

        throw new NoSuchMethodException("No similar method " + name + " with params " + Arrays.toString(types) + " could be found on type " + type() + ".");
    }

    private boolean isSimilarSignature(Method possiblyMatchingMethod, String desiredMethodName, Class<?>[] desiredParamTypes) {
        return possiblyMatchingMethod.getName().equals(desiredMethodName) && match(possiblyMatchingMethod.getParameterTypes(), desiredParamTypes);
    }

    /**
     * 创建一个实例通过默认构造器
     *
     * @return
     * @throws androidx.pluginmgr.reflect.Exception
     */
    public ReflectUtil create() throws Exception {
        return create(new Object[0]);
    }

    /**
     * 创建一个实例根据传入的参数
     *
     * @param args
     * @return
     * @throws Exception
     */
    public ReflectUtil create(Object... args) throws Exception {
        Class<?>[] types = types(args);


        try {
            Constructor<?> constructor = type().getDeclaredConstructor(types);
            return on(constructor, args);
        } catch (NoSuchMethodException e) {
            for (Constructor<?> constructor : type().getDeclaredConstructors()) {
                if (match(constructor.getParameterTypes(), types)) {
                    return on(constructor, args);
                }
            }

            throw new Exception(e);
        }
    }

    /**
     * 创建一个动态代理根据传入的类型.
     * 如果我们正在维护的是一个Map,那么当调用出现异常时我们将从Map中取值.
     *
     * @param proxyType 需要动态代理的类型
     * @return 动态代理生成的对象
     */
    @SuppressWarnings("unchecked")
    public <P> P as(Class<P> proxyType) {
        final boolean isMap = (object instanceof Map);
        final InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                try {
                    return on(object).call(name, args).get();
                } catch (Exception e) {
                    if (isMap) {
                        Map<String, Object> map = (Map<String, Object>) object;
                        int length = (args == null ? 0 : args.length);

                        if (length == 0 && name.startsWith("get")) {
                            return map.get(property(name.substring(3)));
                        } else if (length == 0 && name.startsWith("is")) {
                            return map.get(property(name.substring(2)));
                        } else if (length == 1 && name.startsWith("set")) {
                            map.put(property(name.substring(3)), args[0]);
                            return null;
                        }
                    }

                    throw e;
                }
            }
        };
        return (P) Proxy.newProxyInstance(proxyType.getClassLoader(), new Class[]{proxyType}, handler);
    }

    /**
     * 检查两个数组的类型是否匹配,如果数组中包含原始类型,将它们转换为对应的包装类型.
     */
    private boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (actualTypes[i] == null)
                    continue;

                if (wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i])))
                    continue;

                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return object.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReflectUtil) {
            return object.equals(((ReflectUtil) obj).get());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return object.toString();
    }

    /**
     * 取得我们正在反射的对象的类型.
     *
     * @see Object#getClass()
     */
    public Class<?> type() {
        if (isClass) {
            return (Class<?>) object;
        } else {
            return object.getClass();
        }
    }


}  