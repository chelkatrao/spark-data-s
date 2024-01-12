package com.chelkatrao.starter.unsafe;

import com.chelkatrao.starter.Source;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SparkInvocationHandlerFactory {

    private final DataExtractorResolver extractorResolver;
    private final Map<String, TransformationSpider> spiderMap;
    private final Map<String, Finalizer> finalizerMap;

    @Setter
    private ConfigurableApplicationContext realContext;


    public SparkInvocationHandler create(Class<? extends SparkRepository> repoInterface) {
        Class<?> modelClass = getModelClass(repoInterface);
        Set<String> fieldNames = getFieldNames(modelClass);
        String pathToData = modelClass.getAnnotation(Source.class).value();
        DataExtractor dataExtractor = extractorResolver.resolve(pathToData);

        Map<Method, List<Tuple2<SparkTransformation, List<String>>>> transformationChain = new HashMap<>();
        Map<Method, Finalizer> methodToFinalizer = new HashMap<>();

        Method[] methods = repoInterface.getMethods();
        for (Method method : methods) {
            List<Tuple2<SparkTransformation, List<String>>> transformations = new ArrayList<>();
            List<String> methodWords = new ArrayList<>(Arrays.asList(method.getName().split("(?=\\p{Upper})")));
            TransformationSpider currentSpider = null;
            while (methodWords.size() > 1) {
                String strategyName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(spiderMap.keySet(), methodWords);
                if (!strategyName.isEmpty()) {
                    currentSpider = spiderMap.get(strategyName);
                }

                Tuple2<SparkTransformation, List<String>> transformation =
                        currentSpider.createTransformation(methodWords, fieldNames);
                transformations.add(transformation);
            }

            String finalizerName = "collect";
            if (methodWords.size() == 1) {
                finalizerName = Introspector.decapitalize(methodWords.get(0));
            }
            transformationChain.put(method, transformations);
            methodToFinalizer.put(method, finalizerMap.get(finalizerName));
        }

        return SparkInvocationHandlerImpl.builder()
                .modelClass(modelClass)
                .dataExtractor(dataExtractor)
                .finilazerMap(methodToFinalizer)
                .pathToData(pathToData)
                .transformationChain(transformationChain)
                .postProcessor(new LazyCollectionSupportFinalizerPostProcessor(realContext))
                .context(realContext)
                .build();

    }

    private static Class<?> getModelClass(Class<? extends SparkRepository> repoInterface) {
        ParameterizedType genericInterface = (ParameterizedType) repoInterface.getGenericInterfaces()[0];
        Class<?> modelClass = (Class<?>) genericInterface.getActualTypeArguments()[0];
        return modelClass;
    }

    private static Set<String> getFieldNames(Class<?> modelClass) {
        return Stream.of(modelClass.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Transient.class))
                .filter(field -> !Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

}
