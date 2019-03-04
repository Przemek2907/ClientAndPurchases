package com.app.jsonParser.parsers;

public interface StringParser<T> {
    String toString(T t);
    T fromString(String str);
}
