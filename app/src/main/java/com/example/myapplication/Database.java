//package com.example.myapplication;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.VisibleForTesting;
//import androidx.databinding.adapters.Converters;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.room.TypeConverters;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//import java.util.List;
//import java.util.concurrent.Executors;
//
//@androidx.room.Database(
//        entities = {
//                SearchItem.class,
//                PlanListItem.class
//        }, version=2)
//@TypeConverters({Converters.class})
//public abstract class Database extends RoomDatabase {
//
//    private static Database singleton = null;
//    public abstract SearchItemDao searchListItemDao();
//
//    public synchronized static Database getSingleton(Context context){
//        if(singleton == null){
//            singleton = Database.makeDatabase(context);
//        }
//        return singleton;
//    }
//
//    private static Database makeDatabase(Context context){
//        return Room.databaseBuilder(context, Database.class, "search.db")
//                .allowMainThreadQueries()
//                .addCallback(new Callback() {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
//                            List<SearchItem> exhibits = SearchItem
//                                    .loadJSON(context, "sample_node_info.json");
//                            getSingleton(context).searchListItemDao().insertAll(exhibits);
//                        });
//                    }
//                })
//                .build();
//    }
//
//    @VisibleForTesting
//    public static void injectTestDatabase(Database testDatabase){
//        if(singleton!=null){
//            singleton.close();
//        }
//        singleton=testDatabase;
//    }
//
//}
