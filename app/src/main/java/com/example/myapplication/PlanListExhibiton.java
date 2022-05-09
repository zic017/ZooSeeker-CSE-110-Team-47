package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class PlanListExhibiton extends AppCompatActivity {
    private RecyclerView planListRV;
    private PlanListAdapter plan_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list_exhibiton);

        plan_adapter = new PlanListAdapter();
        plan_adapter.setHasStableIds(true);

        planListRV = findViewById(R.id.planned_list);
        planListRV.setLayoutManager(new LinearLayoutManager(this));
        planListRV.setAdapter(plan_adapter);

        Intent i = getIntent();
        ArrayList<String> planList = i.getStringArrayListExtra("key");

        plan_adapter.setPlanListItems(planList);

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PlanListExhibiton.this, SearchListActivity.class);
                intent.putStringArrayListExtra("key", planList);
                startActivity(intent);
            }
        });
    }

    /*public void buildPlanListRecyclerView() {
        plan_adapter = new PlanAdapter(planList, PlanListExhibiton.this);
        plan_adapter.setHasStableIds(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        planListRV.setHasFixedSize(true);

        planListRV.setLayoutManager(manager);

        planListRV.setAdapter(plan_adapter);
    }*/

}