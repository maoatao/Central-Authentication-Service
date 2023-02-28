package com.maoatao.cas.util;

import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Spring容器工具类
 *
 * @author MaoAtao
 * @date 2022-02-11 19:43:24
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    /**
     * 上下文对象
     */
    private static final AppContainer APP_CONTAINER = new AppContainer();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        APP_CONTAINER.setApplicationContext(applicationContext);
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return APP_CONTAINER.getApplicationContext();
    }


    /**
     * 通过clazz,从spring容器中获取bean
     *
     * @param name bean名称
     * @return <T>对象
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过clazz,从spring容器中获取bean
     *
     * @param clazz Class
     * @param <T>   对象种类
     * @return <T>对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 获取某一类型的bean集合
     *
     * @param clazz Class
     * @param <T>   对象种类
     * @return <T>对象
     */
    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }

    /**
     * 通过name和clazz,从spring容器中获取bean
     *
     * @param name  bean名称
     * @param clazz Class
     * @param <T>   对象种类
     * @return <T>对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 静态内部类，用于存放ApplicationContext
     */
    @Data
    public static class AppContainer {
        private ApplicationContext applicationContext;
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     */
    public static String getEnvironmentProperty(String key) {
        return getApplicationContext().getEnvironment().getProperty(key);
    }

    /**
     * 获取spring.profiles.active
     */
    public static String[] getActiveProfile() {
        return getApplicationContext().getEnvironment().getActiveProfiles();
    }

}
