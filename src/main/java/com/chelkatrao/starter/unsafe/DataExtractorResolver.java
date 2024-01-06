package com.chelkatrao.starter.unsafe;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class DataExtractorResolver {

    private final Map<String, DataExtractor> extractorMap;

    public DataExtractor resolve(String pathToData) {
        String fileExt = pathToData.split("\\.")[1];
        return extractorMap.get(fileExt);
    }
}
