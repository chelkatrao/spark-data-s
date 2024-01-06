package com.chelkatrao.starter;

import lombok.experimental.Delegate;

import java.util.List;

public class LazySparkList implements List {

    @Delegate
    private List content;

    public boolean initialized() {
        return (content != null && !content.isEmpty());
    }
}
