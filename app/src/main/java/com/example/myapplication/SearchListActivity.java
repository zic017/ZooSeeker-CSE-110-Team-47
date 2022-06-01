package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SearchListActivity extends AppCompatActivity {

    private RecyclerView searchListRV;
    private RecyclerView planListRV;
    private SearchAdapter search_adapter;
    private PlanListAdapter plan_adapter;
    public ArrayList<SearchItem> ItemList;
    private ArrayList<String> AllTags;
    private ArrayList<String> plannedList = plannedList = new ArrayList<String>();
    private ArrayList<String> nameList;
    private ArrayList<String> passedInList;
    private ArrayList<String> passedNameList;
    private ArrayList<String> displayList;
    private HashMap<String, HashSet<SearchItem>> tagMap;
    private TextView count;
    ActivityResultLauncher<Intent> activityLauncher;
    private Boolean cleared = false;
    private Boolean saved = false;
    private Set<String> displaySet;
    private Button clearButton;

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

        // Instantiating variables
        searchListRV = findViewById(R.id.search_list);
        planListRV = findViewById(R.id.plan_list_view);
        plan_adapter = new PlanListAdapter();
        plan_adapter.setHasStableIds(true);
        planListRV.setLayoutManager(new LinearLayoutManager(this));
        planListRV.setAdapter(plan_adapter);
        plan_adapter.setPlanListItems(getDisplayList());
        loadPreference();

        count = findViewById(R.id.plan_count);

        /*
            If the user hits Clear in RoutePlanSummaryActivity, the plannedList is cleared. The it is returned to this activity, where we
            clear this activity's plannedList and displayList.
            The 'cleared' boolean is necessary so that the plannedList isn't immediately repopulated with the same exhibits.

            Otherwise, if the user didn't hit Clear and just hit Go Back then nothing changes with the plannedList in this activity either.
         */
        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 1) {
                    Intent intent = result.getData();

                    if (intent != null) {
                        // Grabs plannedList that was returned from RoutePlanSummaryActivity
                        // (could be empty or could be unchanged depending on if they hit Clear)
                        plannedList = intent.getStringArrayListExtra("key");

                        // If the plannedList is empty then they hit Clear and we should update
                        // this activity's plannedList and displayList
                        if (plannedList.isEmpty()) {
                            displayList.clear();
                            search_adapter.clearList();
                            plan_adapter.notifyDataSetChanged();
                            count.setText("0");
                            cleared = true;
                        }
                    }
                }
            }
        });

        Intent i = getIntent();

        // Building search bar
        buildRecyclerView();
        searchListRV.setVisibility(View.INVISIBLE);

        // Updating plan list and plan list count
        updatePassedInList(i.getStringArrayListExtra("key"));
        updatePassedNameList(i.getStringArrayListExtra("key1"));


        // Clears plan list
        clearButton = findViewById((R.id.clear));
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

        // Moves us to route plan summary page
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");
        Button planButton = findViewById(R.id.plan_btn);
        planButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cleared == false) {
                    if (passedInList != null) {
                        passedInList.addAll(search_adapter.getListOfIds());
                        plannedList = passedInList;
                    } else {
                        plannedList = search_adapter.getListOfIds();
                    }
                }
                // If cleared is true we skip the above code once indicating that the user hit Clear
                // in RoutePlanSummary and we do not want to repopulate the plannedList again immediately
                cleared = false;

                if(saved == true) {
                    for (String ex : displayList) {
                        for (Map.Entry<String, ZooData.VertexInfo> entry : vInfo.entrySet()) {
                            if (entry.getValue().name.equals(ex)) {
                                plannedList.add(entry.getKey());
                            }
                        }
                    }
                    saved = false;
                }

                // Code to display popup alert if the plan list is empty
                if(plannedList.isEmpty()) {
                    Log.d("Plan Button", "List is empty");
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchListActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("Empty list");
                    builder.setMessage("Select some exhibits before planning!");

                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Nothing needs to go here it's just necessary to create this for the popup to work correctly
                        }
                    });
                    builder.show();
                    return;
                }

                Intent intent = new Intent(SearchListActivity.this, RoutePlanSummaryActivity.class);
                intent.putStringArrayListExtra("key", plannedList);
                activityLauncher.launch(intent);

            }
        });

        count.setText("" + displayList.size());
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
        ItemList = (ArrayList<SearchItem>) SearchItem.loadJSON(this, "zoo_node_info.json");

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

        if (displaySet == null){
            displaySet = new HashSet<>();
        }
        displaySet.addAll(displayList);


        for(String s: displaySet){
            System.out.println(s);
        }
        editor.putStringSet("PlanList", displaySet);
        editor.putInt("size", displaySet.size());
        editor.apply();
    }

    public void loadPreference(){
        SharedPreferences sp = getSharedPreferences("exhibit", MODE_PRIVATE);
        Set<String> temp = sp.getStringSet("PlanList", null);
        int size = sp.getInt("size", 0);
        saved = true;

        if(temp != null){
            displayList.addAll(temp);
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