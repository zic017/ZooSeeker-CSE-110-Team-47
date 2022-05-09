package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "search_list_items")

public class SearchItem {
    @NonNull
    @PrimaryKey
    public String id; // Why is ID set to be string?
    @NonNull
    public String kind;
    public String name;
    public List<String> tags;

    public SearchItem(@NonNull String id, String kind, String name, List<String> tags) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
    }
    @Override
    public String toString() {
        return "SearchListItem{" +
                "id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getId() { return id; }

    public List<String> getTags() {
        return tags;
    }

    @NonNull
    public String getKind() {
        return kind;
    }

    public static List<SearchItem> loadJSON(Context context, String path){
        try  {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<SearchItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
