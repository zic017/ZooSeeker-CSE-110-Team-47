package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private ArrayList<String> PlanList;
    private Context context;

    public PlanAdapter(ArrayList<String> PlanList, Context context) {
        this.PlanList = PlanList;
        this.context = context;
    }

    public void updateDisplayList(ArrayList<String> updatedList) {
        for(String item : updatedList)
        {
            Log.d("7", item);
        }
        PlanList = updatedList;
        notifyDataSetChanged();
    }
    public void sayHi () { Log.d("Saying", "Hi"); }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_list_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = PlanList.get(position);
        holder.planItemName.setText(item);
    }
    @Override
    public int getItemCount() {
        return PlanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView planItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            planItemName = itemView.findViewById(R.id.plan_item_text);

        }
    }
}
