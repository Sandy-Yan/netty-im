package com.corundumstudio.socketio.command.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 14:59
 */
public class BeanFactory {

    private static volatile ApplicationContext applicationContext;

    private static volatile boolean initByBeanFactory = false;

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    public static final void init() {
        if (applicationContext == null) {
            synchronized (BeanFactory.class) {
                if (applicationContext == null) {
                    logger.info("init bean factory begin ...");
                    initByBeanFactory = true;
                    String[] configXmls = new String[]{"classpath:applicationContext*.xml"};
                    applicationContext = new ClassPathXmlApplicationContext(configXmls);
                    logger.info("init bean factory end !");
                }
            }
        }
    }

    public static final <T> T getBean(Class<T> clazz) {
        init();
        return applicationContext.getBean(clazz);
    }

    public static final <T> T getBean(String beanName) {
        init();
        return (T) applicationContext.getBean(beanName);
    }

    public static final <T> Map<String, T> getBeansOfType(Class<T> type) {
        init();
        return applicationContext.getBeansOfType(type);
    }

    public static final Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        init();
        return applicationContext.getBeansWithAnnotation(annotationType);
    }

    /**
     * @return 是否被beanFactory初始化
     */
    public static final boolean initByBeanFactory() {

        return initByBeanFactory;
    }

    /**
     * @return spring是否被初始化
     */
    public static final boolean inited() {
        return applicationContext != null;
    }

}
