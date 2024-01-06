package com.chelkatrao.starter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("between")
public class BetweenFilter implements FilterTransformation {

    @Override
    public Dataset<Row> transform(Dataset<Row> dataset, List<String> fields, OrderedBag<Object> args) {
        return dataset.filter(functions.col(fields.get(0))
                                      .as(fields.get(0))
                                      .between(args.takeAndRemove(),
                                               args.takeAndRemove()));
    }

}
