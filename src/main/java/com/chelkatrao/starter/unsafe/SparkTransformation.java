package com.chelkatrao.starter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface SparkTransformation {
    Dataset<Row> transform(Dataset<Row> dataset, List<String> fields, OrderedBag<Object> args);
}
