package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "plan_list_items")
public class PlanListItem {

    @NonNull
    @PrimaryKey
    public String id;

    @NonNull
    public String kind;
    public String name;
    public List<String> tags;

    public PlanListItem(@NonNull String id, String kind, String name, List<String> tags) {
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
}
