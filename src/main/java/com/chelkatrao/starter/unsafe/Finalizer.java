package com.chelkatrao.starter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface Finalizer {
    Object doAction(Dataset<Row> dataset, Class<?> modelClass, OrderedBag<?> orderedBag);
}
