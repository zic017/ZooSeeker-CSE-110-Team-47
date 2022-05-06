package com.example.myapplication;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<String> fromJsonToList(String jsonStr) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, listType);
    }

    @TypeConverter
    public static String fromListToJson(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
