package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoutePlanSummaryActivity extends AppCompatActivity {

    private Button getDirections;
    private Button clearList;
    private TextView exhibitListCount;
    private ListView exhibitListDisplay;
    private DirectionsAlgorithm dirAlgo;
    private LinkedHashMap<String, String> exhibitDistancesMap;
    private List<LinkedHashMap<String,String>> exhibitList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan_summary);

        // Instantiating objects
        getDirections = findViewById(R.id.getDirBtn);
        exhibitListCount = findViewById(R.id.plannedExhibitCount);
        exhibitListDisplay = findViewById(R.id.displayOfExhibits);
        clearList = findViewById(R.id.routePlanClearBtn);
        exhibitDistancesMap = new LinkedHashMap<String, String>();
        exhibitList = new ArrayList<>();

        /**
         *  Code for printing exhibits with distances
         *  Todo: Test distance algorithm
         */
        Intent i = getIntent();
        ArrayList<String> plannedList = i.getStringArrayListExtra("key");
        Context context = getApplicationContext();

        ArrayList<Integer> distFromEntrance;
        dirAlgo = new DirectionsAlgorithm(plannedList, context, 32.73459618, -117.14936);
        distFromEntrance = dirAlgo.getDistances(plannedList);


        /**
         *  Display exhibit list
         */

        // Add exhibits and corresponding distances into map to be displayed
        int j = 0;
        for(String exhibit : plannedList){
            exhibitDistancesMap.put(exhibit, String.valueOf(distFromEntrance.get(j)) + " ft");
            j++;
        }

        // Create an adapter and add each exhibit and its corresponding distance into the ListView
        SimpleAdapter adapter = new SimpleAdapter(this, exhibitList, R.layout.summary_list_item,
                new String[] {"First Line", "Second Line"},
                new int[] {R.id.exhibit1, R.id.exhibit2});

        Iterator iterator = exhibitDistancesMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            LinkedHashMap<String,String> resultsMap = new LinkedHashMap<>();
            Map.Entry pair = (Map.Entry)iterator.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            exhibitList.add(resultsMap);
        }

        exhibitListDisplay.setAdapter(adapter);


        /**
         *  Display count of exhibits
         */
        exhibitListCount.setText(String.valueOf(plannedList.size()));


        /**
         *   Clearing the exhibit list
         *   Todo: Clear the ListView and also the plannedExhibits list that was passed in so we can start over
         */
        clearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoutePlanSummaryActivity.this, SearchListActivity.class);
                startActivity(intent);
            }
        });


        /**
         *  Going to directions activity
         */
        getDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoutePlanSummaryActivity.this, DirectionsActivity.class);
                intent.putStringArrayListExtra("key", plannedList);
                startActivity(intent);
            }
        });

    }
}
