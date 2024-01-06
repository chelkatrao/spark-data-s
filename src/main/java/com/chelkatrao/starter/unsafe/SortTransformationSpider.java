package com.chelkatrao.starter.unsafe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component("orderBy")
public class SortTransformationSpider implements TransformationSpider {

    @Autowired
    private SortTransformation sortTransformation;

    @Override
    public Tuple2<SparkTransformation, List<String>> createTransformation(List<String> remainingWords, Set<String> fieldNames) {
        String fieldName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(fieldNames, remainingWords);
        final ArrayList<String> additionalFields = new ArrayList<>();
        while (!remainingWords.isEmpty() && remainingWords.get(0).equalsIgnoreCase("and ")) {
            remainingWords.remove(0);
            additionalFields.add(WordsMatcher.findAndRemoveMatchingPiecesIfExists(fieldNames, remainingWords));
        }
        additionalFields.add(0, fieldName);
        return new Tuple2<>(sortTransformation, additionalFields);
    }

}
