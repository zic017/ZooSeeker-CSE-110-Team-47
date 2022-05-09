package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class PlanListExhibiton extends AppCompatActivity {
    private RecyclerView planListRV;
    private ArrayList<String> planList;
    private PlanAdapter plan_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list_exhibiton);

        Intent i = getIntent();
        planList = i.getStringArrayListExtra("key");
        planListRV = findViewById(R.id.recyclerView);


    }

    /*public void buildPlanListRecyclerView() {
        plan_adapter = new PlanAdapter(planList, PlanListExhibiton.this);
        plan_adapter.setHasStableIds(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        planListRV.setHasFixedSize(true);

        planListRV.setLayoutManager(manager);

        planListRV.setAdapter(plan_adapter);
    }*/

    public void onBackClicked(View view) {
        Intent intent = new Intent(this, SearchListActivity.class);
        startActivity(intent);
    }
}