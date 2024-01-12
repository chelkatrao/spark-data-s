package com.chelkatrao.starter;

import com.chelkatrao.starter.unsafe.DataExtractor;
import com.chelkatrao.starter.unsafe.DataExtractorResolver;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FirstLevelCacheService {

    private final Map<Class<?>, Dataset<Row>> model2Dataset = new HashMap<>();

    @Autowired
    private DataExtractorResolver resolver;

    public List<?> getDataFor(long ownerId, Class<?> modelClass, String path, String foreignKeyName, ConfigurableApplicationContext context) {
        if (!model2Dataset.containsKey(modelClass)) {
            DataExtractor dataExtractor = resolver.resolve(path);
            Dataset<Row> persist = dataExtractor.readData(path, context).persist();
            model2Dataset.put(modelClass, persist);
        }
        return model2Dataset.get(modelClass)
                .filter(functions.col(foreignKeyName).equalTo(ownerId))
                .as(Encoders.bean(modelClass)).collectAsList();
    }
}
