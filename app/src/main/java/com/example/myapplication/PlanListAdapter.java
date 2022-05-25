package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {
    private ArrayList<String> ItemList = new ArrayList<>();
    private Context context;

    public void setPlanListItems(ArrayList<String> itemList){
        this.ItemList.clear();
        this.ItemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.plan_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanListAdapter.ViewHolder holder, int position) {
        holder.setPlanListItem(ItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private String planListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.plan_item_text);

        }

        public void setPlanListItem(String item){
            this.itemName.setText(item);
        }
    }

    public String getItem(int position) {
        return this.ItemList.get(position);
    }

}
