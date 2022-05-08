package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class SearchListActivity extends AppCompatActivity {

    private RecyclerView RV;
    private SearchAdapter adapter;
    public ArrayList<SearchItem> ItemList;
    public ArrayList<SearchItem> ExhibitionList;
    private ArrayList<String> AllTags;
    private ArrayList<String> plannedList;
    private HashMap<String, HashSet<SearchItem>> tagMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        RV = findViewById(R.id.search_list);
        buildRecyclerView();
        RV.setVisibility(View.INVISIBLE);

        Button planButton = findViewById(R.id.plan_btn);
        planButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                plannedList = adapter.getListOfIds();
                Intent intent = new Intent(SearchListActivity.this, DirectionsActivity.class);
                intent.putStringArrayListExtra("key", plannedList);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView search = findViewById(R.id.search_bar);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                if (search.getQuery().length() == 0){
                    RV.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        return true;

    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<SearchItem> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (String tag : AllTags) {
            // checking if the entered string matched with any item of our recycler view.
            if (tag.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                for (SearchItem item : tagMap.get(tag)){
                    filteredlist.add(item);
                }
                break;
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
            RV.setVisibility(View.VISIBLE);
        }
    }
    private void buildRecyclerView() {
        ArrayList<String> AllTags = new ArrayList<>();
        HashMap<String, HashSet<SearchItem>> tagMap = new HashMap<>();

        // below line we are creating a new array list
        ItemList = new ArrayList<>();
        // below line is to add data to our array list.
        ItemList = (ArrayList<SearchItem>) SearchItem.loadJSON(this, "sample_node_info.json");
        ExhibitionList = new ArrayList<SearchItem>();
        for(SearchItem items : ItemList){
            if(items.kind.equals("exhibit")){
                ExhibitionList.add(items);
            }
        }
        for (SearchItem item : ExhibitionList) {
            String name = item.getName();
            tagMap.putIfAbsent(name, new HashSet<>());
            tagMap.get(name).add(item);
             AllTags.add(name);

            for (String tag : item.getTags()){
                tagMap.putIfAbsent(tag, new HashSet<>());
                tagMap.get(tag).add(item);
                AllTags.add(tag);
            }

        }

        // initializing our adapter class.
        this.AllTags = AllTags;
        this.tagMap = tagMap;
        adapter = new SearchAdapter(ExhibitionList, SearchListActivity.this);
        adapter.setHasStableIds(true);
//        adapter.setOnAddBtnClickHandler();
        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        RV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        RV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        RV.setAdapter(adapter);
    }
}