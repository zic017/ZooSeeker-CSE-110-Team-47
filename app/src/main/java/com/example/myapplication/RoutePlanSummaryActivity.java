package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RoutePlanSummaryActivity extends AppCompatActivity {

    private Button backToSearch;
    private Button getDirections;
    private Button clearList;
    private TextView exhibitListCount;
    private ListView exhibitListDisplay;
    private DirectionsAlgorithm dirAlgo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan_summary);

        // Instantiating objects
        backToSearch = findViewById(R.id.backToSearchBtn);
        getDirections = findViewById(R.id.getDirBtn);
        exhibitListCount = findViewById(R.id.plannedExhibitCount);
        exhibitListDisplay = findViewById(R.id.displayOfExhibits);
        clearList = findViewById(R.id.routePlanClearBtn);

        /**
            Code for printing distances
            Todo: Write the getDistances method in DirectionsAlgorithm.java that
                  will calculate the minimum distance from the entrance to that
                  exhibit
         */
        ArrayList<Integer> distFromEntrance;
        Intent i = getIntent();
        ArrayList<String> plannedList = i.getStringArrayListExtra("key");
        Context context = getApplicationContext();
//
//        dirAlgo = new DirectionsAlgorithm(plannedList, context, 32.73459618, -117.14936);
//        distFromEntrance = dirAlgo.getDistances(plannedList);

        /**
         *  Display number of exhibits
         */
//        exhibitListCount.setText(plannedList.size());

        /**
         *  Display exhibit list
         *  Todo: Append the distances onto each exhibit before displaying
         */
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, plannedList);
        exhibitListDisplay.setAdapter(arrayAdapter);

        /**
         *   Clearing the exhibit list
         *   Todo: Clear the ListView and also the plannedExhibits list that was passed in so we can start over
         */
        clearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /**
         *  Going back to search bar
         */
        backToSearch.setOnClickListener(new View.OnClickListener() {
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
