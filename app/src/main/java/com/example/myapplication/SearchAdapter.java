package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.function.Consumer;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList<SearchItem> ItemList;
    private Context context;
    private Consumer<SearchItem> onAddBtnClicked;
    private SearchItem searchItem;

    public SearchAdapter(ArrayList<SearchItem> ItemList, Context context) {
        this.ItemList = ItemList;
        this.context = context;
    }
    public void filterList(ArrayList<SearchItem> filterllist) {
        ItemList = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchItem item = ItemList.get(position);
        holder.itemName.setText(item.getName());
    }

    public void setOnAddBtnClickHandler(Consumer<SearchItem> onAddBtnClicked) {
        this.onAddBtnClicked = onAddBtnClicked;
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public String getItemID(int position) {
        return ItemList.get(position).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private TextView add_btn;
        private ArrayList<String>chosenExhibits = new ArrayList<>();
        private ArrayList<String>chosenExhibitsIDS = new ArrayList<>();
        private String s;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.search_item_text);
            add_btn = itemView.findViewById(R.id.add_btn);

            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s = itemName.getText().toString();
                    if(chosenExhibits.contains(s))
                        return;
                    for(SearchItem item : ItemList) {
                        if(item.getName() == s)
                            chosenExhibitsIDS.add(item.getId());
                        chosenExhibits.add(s);
                    }
//                    Test to see what IDs are in the list
//                    for(String item : chosenExhibitsIDS) {
//                        Log.d("2",item);
//                    }
                }
            });
//            for(SearchItem item : ItemList) {
//                Log.d("3", item.getId());
//            }
        }
    }
}
