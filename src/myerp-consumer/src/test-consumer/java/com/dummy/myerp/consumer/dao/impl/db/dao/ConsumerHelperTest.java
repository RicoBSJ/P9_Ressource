package com.dummy.myerp.consumer.dao.impl.db.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerHelperTest {

    private static final String CONTEXT_PATH = "com/dummy/myerp/test-consumer/consumerContext.xml";
    private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(CONTEXT_PATH);
    private static ComptabiliteDaoImpl comptabiliteDao = ComptabiliteDaoImpl.getInstance();

    protected ConsumerHelperTest() { }

    public static ConsumerHelperTest getInstance() {
        return new ConsumerHelperTest();
    }

    protected static Object getBean(String beanId) {
        return getInstance().CONTEXT.getBean(beanId);
    }
}
