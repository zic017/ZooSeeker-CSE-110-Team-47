package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SearchListItemDao {
    @Insert
    long insert(SearchListItem searchListItem);

    @Insert
    List<Long> insertAll(List<SearchListItem> searchListItems);

    @Query("SELECT * FROM `search_list_items` WHERE `id`=:id")
    SearchListItem get(long id);

    @Query("SELECT * FROM `search_list_items` ORDER BY `id`")
    List<SearchListItem> getAll();

    @Query("SELECT * FROM `search_list_items` ORDER BY `id`")
    LiveData<List<SearchListItem>> getAllLive();
/*
    @Query("SELECT `order` + 1 FROM `todo_list_items` ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();
 */
    @Update
    int update(SearchListItem searchListItem);

    @Delete
    int delete(SearchListItem searchListItem);
}

