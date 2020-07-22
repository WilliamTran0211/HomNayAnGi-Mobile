package com.cuongtt.homnayangi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Gson gson;

    public static Gson getGsonParser() {
        if(null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }
}


