package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {SearchListItem.class}, version=1)
@TypeConverters({Converters.class})
public abstract class SearchDataBase extends RoomDatabase {

    private static SearchDataBase singleton = null;
    public abstract SearchListItemDao searchListItemDao();

    public synchronized static SearchDataBase getSingleton(Context context){
        if(singleton == null){
            singleton = SearchDataBase.makeDatabase(context);
        }
        return singleton;
    }

    private static SearchDataBase makeDatabase(Context context){
        return Room.databaseBuilder(context, SearchDataBase.class, "search.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<SearchListItem> exhibits = SearchListItem
                                    .loadJSON(context, "sample_node_info.json");
                            getSingleton(context).searchListItemDao().insertAll(exhibits);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(SearchDataBase testDatabase){
        if(singleton!=null){
            singleton.close();
        }
        singleton=testDatabase;
    }

}
