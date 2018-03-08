package com.jlhood.retweetcounter;

import java.lang.reflect.Type;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class RetweetCountsMapConverter implements DynamoDBTypeConverter<String, Map<String, RetweetCount>> {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    public String convert(Map<String, RetweetCount> toConvert) {
        return GSON.toJson(toConvert);
    }

    @Override
    public Map<String, RetweetCount> unconvert(String toUnconvert) {
        Type type = new TypeToken<Map<String, RetweetCount>>() {
        }.getType();
        return GSON.fromJson(toUnconvert, type);
    }
}
