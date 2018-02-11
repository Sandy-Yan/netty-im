package com.github.andy.im.command.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 14:59
 */
public class BeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private static volatile ClassPathXmlApplicationContext applicationContext;

    private BeanFactory() {
        throw new UnsupportedOperationException();
    }

    public static final void init(String... configLocations) {
        if (configLocations == null) {
            throw new IllegalArgumentException("configLocations is null!!!");
        }
        if (applicationContext == null) {
            synchronized (BeanFactory.class) {
                if (applicationContext == null) {
                    logger.info("init bean factory begin ...");
                    applicationContext = new ClassPathXmlApplicationContext(configLocations);
                    logger.info("init bean factory end !");
                }
            }
        }
    }

    public static final void close() {
        if (inited()) {
            applicationContext.close();
            applicationContext = null;
        }
    }

    public static final boolean inited() {
        return applicationContext != null;
    }

    private static void checkInited() {
        if (!inited()) {
            throw new UnsupportedOperationException("spring must be inited !!!");
        }
    }

    public static final <T> T getBean(Class<T> clazz) {
        checkInited();
        return applicationContext.getBean(clazz);
    }

    public static final <T> T getBean(String beanName) {
        checkInited();
        return (T) applicationContext.getBean(beanName);
    }

    public static final <T> Map<String, T> getBeansOfType(Class<T> type) {
        checkInited();
        return applicationContext.getBeansOfType(type);
    }

    public static final Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        checkInited();
        return applicationContext.getBeansWithAnnotation(annotationType);
    }

}
