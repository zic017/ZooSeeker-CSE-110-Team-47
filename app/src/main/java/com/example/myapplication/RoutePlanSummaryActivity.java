package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoutePlanSummaryActivity extends AppCompatActivity {

    private Button getDirections;
    private Button clearList;
    private Button backToSearch;
    private TextView exhibitListCount;
    private ListView exhibitListDisplay;
    private DirectionsAlgorithm dirAlgo;
    private ArrayList<String> plannedListNames;
    private LinkedHashMap<String, String> exhibitDistancesMap;
    private List<LinkedHashMap<String,String>> displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan_summary);

        // Instantiating objects
        backToSearch = findViewById(R.id.goBack);
        getDirections = findViewById(R.id.getDirBtn);
        exhibitListCount = findViewById(R.id.plannedExhibitCount);
        exhibitListDisplay = findViewById(R.id.displayOfExhibits);
        clearList = findViewById(R.id.routePlanClearBtn);
        plannedListNames = new ArrayList<>();
        exhibitDistancesMap = new LinkedHashMap<String, String>();
        displayList = new ArrayList<>();
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");


        /**
         *  Code for printing exhibits with distances
         *  Todo: Test distance algorithm
         */
        Intent i = getIntent();
        ArrayList<String> plannedListIds = i.getStringArrayListExtra("key");
        Context context = getApplicationContext();

        for (String id : plannedListIds){
            plannedListNames.add(vInfo.get(id).name);
        }

        ArrayList<Integer> distFromEntrance;
        dirAlgo = new DirectionsAlgorithm(plannedListIds, context, 32.73459618, -117.14936);
        distFromEntrance = dirAlgo.getDistances(plannedListIds);


        /**
         *  Display exhibit list
         */

        // Add exhibits and corresponding distances into map to be displayed
        int j = 0;
        for(String exhibit : plannedListNames){
            exhibitDistancesMap.put(exhibit, String.valueOf(distFromEntrance.get(j)) + " ft");
            j++;
        }

        // Create an adapter and add each exhibit and its corresponding distance into the ListView
        SimpleAdapter adapter = new SimpleAdapter(this, displayList, R.layout.summary_list_item,
                new String[] {"First Line", "Second Line"},
                new int[] {R.id.exhibit1, R.id.exhibit2});

        Iterator iterator = exhibitDistancesMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            LinkedHashMap<String,String> resultsMap = new LinkedHashMap<>();
            Map.Entry pair = (Map.Entry)iterator.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            displayList.add(resultsMap);
        }

        exhibitListDisplay.setAdapter(adapter);


        /**
         *  Display count of exhibits
         */
        exhibitListCount.setText(String.valueOf(plannedListIds.size()));


        /**
         *   Clearing the exhibit list
         */
        clearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RoutePlanSummaryActivity.this, SearchListActivity.class);
//                startActivity(intent);
                displayList.clear();
                plannedListIds.clear();
                adapter.notifyDataSetChanged();
                exhibitListCount.setText("0");
            }
        });

        /**
         *  Go back to search bar
         */
        backToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("key", plannedListIds);
                setResult(1, intent);

                finish();
            }
        });

        /**
         *  Going to directions activity
         */
        getDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Code for popup message if plannedListIds is empty
                if(plannedListIds.isEmpty()) {
                    Log.d("Plan Button", "List is empty");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RoutePlanSummaryActivity.this);

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

                Intent intent = new Intent(RoutePlanSummaryActivity.this, DirectionsActivity.class);
                intent.putStringArrayListExtra("key", plannedListIds);
                startActivity(intent);
            }
        });

    }
}
