package com.chelkatrao.starter.unsafe;

import lombok.Builder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.context.ConfigurableApplicationContext;
import scala.Tuple2;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Builder
public class SparkInvocationHandlerImpl implements SparkInvocationHandler {

    private Class<?> modelClass;
    private String pathToData;
    private DataExtractor dataExtractor;
    private Map<Method, List<Tuple2<SparkTransformation, List<String>>>> transformationChain;
    private Map<Method, Finalizer> finilazerMap;
    private ConfigurableApplicationContext context;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        OrderedBag<Object> orderedBag = new OrderedBag<>(args);

        Dataset<Row> dataset = dataExtractor.readData(pathToData, context);
        List<Tuple2<SparkTransformation, List<String>>> tuple2s = transformationChain.get(method);
        for (Tuple2<SparkTransformation, List<String>> tuple2 : tuple2s) {
            SparkTransformation sparkTransformation = tuple2._1();
            List<String> fieldNames = tuple2._2();
            dataset = sparkTransformation.transform(dataset, fieldNames, orderedBag);
        }

        Finalizer finalizer = finilazerMap.get(method);

        return finalizer.doAction(dataset, modelClass, orderedBag);
    }

}
