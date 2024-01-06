package com.chelkatrao.starter;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface ForeignKeyName {
    String value();
}
