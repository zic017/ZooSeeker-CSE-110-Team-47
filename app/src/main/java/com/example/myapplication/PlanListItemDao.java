package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PlanListItemDao {
    @Insert
    long insert(PlanListItem planListItem);

    @Insert
    List<Long> insertAll(List<PlanListItem> planListItems);

    @Query("SELECT * FROM `plan_list_items` WHERE `id`=:id")
    PlanListItem get(String id);

    @Query("SELECT * FROM `plan_list_items` ORDER BY `id`")
    List<PlanListItem> getAll();

    @Query("SELECT * FROM `plan_list_items` ORDER BY `id`")
    LiveData<List<PlanListItem>> getAllLive();
    /*
        @Query("SELECT `order` + 1 FROM `todo_list_items` ORDER BY `order` DESC LIMIT 1")
        int getOrderForAppend();
     */
    @Update
    int update(PlanListItem planListItem);

    @Delete
    int delete(PlanListItem planListItem);
}

