package com.chelkatrao.starter.unsafe;

import com.chelkatrao.starter.ForeignKeyName;
import com.chelkatrao.starter.LazySparkList;
import com.chelkatrao.starter.Source;
import lombok.SneakyThrows;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public class LazyCollectionSupportFinalizerPostProcessor implements FinalizerPostProcessor {

    private ConfigurableApplicationContext context;

    @SneakyThrows
    @Override
    public Object postFinalize(Object retVal) {
        if (Collection.class.isAssignableFrom(retVal.getClass())) {
            for (Object model : (List<?>) retVal) {
                Field idField = model.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                long ownerId = idField.getLong(model);

                Field[] declaredFields = model.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    if (List.class.isAssignableFrom(field.getType())) {
                        LazySparkList sparkList = context.getBean(LazySparkList.class);

                        sparkList.setOwnerId(ownerId);

                        String columnName = field.getAnnotation(ForeignKeyName.class).value();
                        sparkList.setForeignKeyName(columnName);

                        Class<?> embeddedModel = getEmbeddedModel(field);
                        sparkList.setModelClass(embeddedModel);
                        String pathToData = embeddedModel.getAnnotation(Source.class).value();
                        sparkList.setPathToSource(pathToData);
                    }
                }
            }
        }
        return null;
    }

    private static Class<?> getEmbeddedModel(Field field) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        return  (Class<?>) genericType.getActualTypeArguments()[0];
    }
}
