package com.chelkatrao.starter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SortTransformation implements SparkTransformation {
    @Override
    public Dataset<Row> transform(Dataset<Row> dataset, List<String> fields, OrderedBag<Object> args) {
        return dataset.orderBy(fields.remove(0),
                               fields.stream().skip(1).toArray(String[]::new));
    }
}
