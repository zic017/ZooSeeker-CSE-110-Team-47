package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SearchItemDao {
    @Insert
    long insert(SearchItem searchListItem);

    @Insert
    List<Long> insertAll(List<SearchItem> searchListItems);

    @Query("SELECT * FROM `search_list_items` WHERE `id`=:id")
    SearchItem get(String id);

    @Query("SELECT * FROM `search_list_items` ORDER BY `id`")
    List<SearchItem> getAll();

    @Query("SELECT * FROM `search_list_items` ORDER BY `id`")
    LiveData<List<SearchItem>> getAllLive();
    @Update
    int update(SearchItem searchListItem);

    @Delete
    int delete(SearchItem searchListItem);
}
