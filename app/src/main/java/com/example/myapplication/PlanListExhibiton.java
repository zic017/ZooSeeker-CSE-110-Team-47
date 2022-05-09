package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class PlanListExhibiton extends AppCompatActivity {
    private RecyclerView planListRV;
    private PlanListAdapter plan_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list_exhibiton);
        Context context = getApplicationContext();

        TextView plannedNumber = findViewById(R.id.planned_number);

        plan_adapter = new PlanListAdapter();
        plan_adapter.setHasStableIds(true);

        planListRV = findViewById(R.id.planned_list);
        planListRV.setLayoutManager(new LinearLayoutManager(this));
        planListRV.setAdapter(plan_adapter);

        Intent i = getIntent();
        ArrayList<String> planList = i.getStringArrayListExtra("key");
        ArrayList<String> ItemList = idToString(context, planList);
        Integer num = planList.size();
        plannedNumber.setText("Added Exhibits: (" + num.toString() + ")");
        plan_adapter.setPlanListItems(ItemList);

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PlanListExhibiton.this, SearchListActivity.class);
                intent.putStringArrayListExtra("key", planList);
                intent.putStringArrayListExtra("key1", ItemList);
                startActivity(intent);
            }
        });
    }

    //Retrieve names based on the list of IDs
    public ArrayList<String> idToString(Context context, ArrayList<String> ids) {
        ArrayList<String> names = new ArrayList<>();
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        for(String id : ids) {
            names.add(vInfo.get(id).name);
        }
        return names;
    }

}