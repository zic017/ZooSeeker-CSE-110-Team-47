package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class SearchListActivity extends AppCompatActivity {
//test
    private RecyclerView searchListRV;
    private RecyclerView planListRV;
    private SearchAdapter search_adapter;
    private PlanListAdapter plan_adapter;
    public ArrayList<SearchItem> ItemList;
    private ArrayList<String> AllTags;
    private ArrayList<String> plannedList;
    private ArrayList<String> nameList;
    private ArrayList<String> passedInList;
    private ArrayList<String> passedNameList;
    private ArrayList<String> displayList;
    private HashMap<String, HashSet<SearchItem>> tagMap;
    private TextView count;
    private Set<String> set;

    public ArrayList<String> getDisplayList() {
        if (displayList == null){
            displayList = new ArrayList<>();
        }
        return displayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        searchListRV = findViewById(R.id.search_list);
        planListRV = findViewById(R.id.plan_list_view);
        plan_adapter = new PlanListAdapter();
        plan_adapter.setHasStableIds(true);
        planListRV.setLayoutManager(new LinearLayoutManager(this));
        planListRV.setAdapter(plan_adapter);
        plan_adapter.setPlanListItems(getDisplayList());
        loadPreference();

        count = findViewById(R.id.plan_count);

        Intent i = getIntent();

        buildRecyclerView();
        searchListRV.setVisibility(View.INVISIBLE);

        updatePassedInList(i.getStringArrayListExtra("key"));
        updatePassedNameList(i.getStringArrayListExtra("key1"));

        Button clearButton = findViewById((R.id.clear));
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(displayList == null)
                    return;

                displayList.clear();
                search_adapter.clearList();
                clearPreference();
                plan_adapter.notifyDataSetChanged();
                count.setText("0");
            }
        });

        Button planButton = findViewById(R.id.plan_btn);
        planButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(passedInList != null){
                    passedInList.addAll(search_adapter.getListOfIds());
                    plannedList = passedInList;
                }else {
                    plannedList = search_adapter.getListOfIds();
                }

                Intent intent = new Intent(SearchListActivity.this, DirectionsActivity.class);
                intent.putStringArrayListExtra("key", plannedList);
                startActivity(intent);
            }
        });
    }

    /**
    public void updatePlan() {
        if (displayList.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }
        else {
            for(String item : displayList)
            {
                Log.d("insideUpdatePlan", item);
            }
            searchListRV.setVisibility(View.VISIBLE);
            plan_adapter.updateDisplayList(displayList);

        }
    }
     **/

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
                    searchListRV.setVisibility(View.INVISIBLE);
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
            search_adapter.filterList(filteredlist);
            searchListRV.setVisibility(View.VISIBLE);
        }
    }
    private void buildRecyclerView() {
        ArrayList<String> AllTags = new ArrayList<>();
        HashMap<String, HashSet<SearchItem>> tagMap = new HashMap<>();

        // below line we are creating a new array list
        ItemList = new ArrayList<>();
        // below line is to add data to our array list.
        ItemList = (ArrayList<SearchItem>) SearchItem.loadJSON(this, "sample_node_info.json");

        for (SearchItem item : ItemList) {
            if(item.getKind().equals("exhibit")) {
                String name = item.getName();
                tagMap.putIfAbsent(name, new HashSet<>());
                tagMap.get(name).add(item);
                AllTags.add(name);

                for (String tag : item.getTags()) {
                    tagMap.putIfAbsent(tag, new HashSet<>());
                    tagMap.get(tag).add(item);
                    AllTags.add(tag);
                }
            }
        }

        // initializing our adapter class.
        this.AllTags = AllTags;
        this.tagMap = tagMap;

        search_adapter = new SearchAdapter(ItemList, SearchListActivity.this);
        search_adapter.setHasStableIds(true);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        searchListRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        searchListRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        searchListRV.setAdapter(search_adapter);
    }
/**
    public void buildPlanListRecyclerView() {
        plan_adapter = new PlanAdapter(displayList, SearchListActivity.this);
        plan_adapter.setHasStableIds(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        planListRV.setHasFixedSize(true);

        planListRV.setLayoutManager(manager);

        planListRV.setAdapter(plan_adapter);
    }
 **/

    public void updatePassedInList(ArrayList<String> passedInList){
        this.passedInList = passedInList;
    }

    public void updatePassedNameList(ArrayList<String> passedNameList){
        this.passedNameList = passedNameList;
    }

    public void updateDisplayList(String addedExhibit) {
        displayList.add(addedExhibit);
        plan_adapter.notifyDataSetChanged();
        count.setText("" + displayList.size());
    }

    public void savePreference(){
        SharedPreferences sp = getSharedPreferences("exhibit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (set == null){
            set = new HashSet<>();
        }
        set.addAll(displayList);

        for(String s: set){
            System.out.println(s);
        }
        editor.putStringSet("PlanList", set);
        editor.putInt("size", set.size());
        editor.apply();
    }

    public void loadPreference(){
        SharedPreferences sp = getSharedPreferences("exhibit", MODE_PRIVATE);
        Set<String> temp = sp.getStringSet("PlanList", null);
        //int size = sp.getInt("size", 0);
        //count.setText(size);

        if(temp != null){
            displayList.addAll(temp);
            for(String s : temp) {
                System.out.println(s);
            }
            for(String s: temp){
//                updateDisplayList(s);
            }
        }
    }

    public void clearPreference() {
        SharedPreferences sp = getSharedPreferences("exhibit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear();
        editor.apply();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        savePreference();
    }



}