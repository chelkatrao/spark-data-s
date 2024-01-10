package com.chelkatrao.starter.unsafe;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.beans.Introspector;
import java.lang.reflect.Proxy;

public class SparkDataApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext context) {

        AnnotationConfigApplicationContext tempContext = new AnnotationConfigApplicationContext(InternalConfig.class);
        SparkInvocationHandlerFactory factory = tempContext.getBean(SparkInvocationHandlerFactory.class);
        factory.setRealContext(context);

        DataExtractorResolver dataExtractorResolver = tempContext.getBean(DataExtractorResolver.class);
        context.getBeanFactory().registerSingleton("extractorResolverForSpark", dataExtractorResolver);

        tempContext.close();

        registerSparkBeans(context);

        Reflections scanner = new Reflections(context.getEnvironment().getProperty("spark.packs-to-scan"));
        scanner.getSubTypesOf(SparkRepository.class).forEach(repoInterface -> {
            Object proxyInstance = Proxy.newProxyInstance(repoInterface.getClassLoader(),
                                                          new Class[]{repoInterface},
                                                          factory.create(repoInterface));
            context.getBeanFactory().registerSingleton(Introspector.decapitalize(repoInterface.getName()),
                                                       proxyInstance);
        });

    }

    private void registerSparkBeans(ConfigurableApplicationContext context) {
        SparkSession sparkSession = SparkSession.builder()
                .master("local[*]")
                .appName(context.getEnvironment().getProperty("spark.app-name"))
                .getOrCreate();

        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        context.getBeanFactory().registerSingleton("sparkSession", sparkSession);
        context.getBeanFactory().registerSingleton("sparkContext", sparkContext);
    }
}
