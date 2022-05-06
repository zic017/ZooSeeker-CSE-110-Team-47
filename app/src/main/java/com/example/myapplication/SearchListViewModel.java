package com.example.myapplication;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SearchListViewModel extends AndroidViewModel {

    private LiveData<List<SearchListItem>> searchListItems;
    private final SearchListItemDao searchListItemDao;

    public SearchListViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        SearchDataBase db = SearchDataBase.getSingleton(context);
        searchListItemDao = db.searchListItemDao();
    }

//    public LiveData<List<SearchListItem>> getSearchListItems(){
//        if(searchListItems == null){
//            loadUsers();
//        }
//        return searchListItems;
//    }

//    public void loadUsers(){
//        searchListItems = searchListItemDao.getAllLive();
//    }

//    public void deleteTodo(TodoListItem todoListItem){
//        todoListItems.getValue().clear();
//        todoListItemDao.delete(todoListItem);
}