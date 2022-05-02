package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private List<SearchListItem> searchItems = Collections.emptyList();
    private List<PlanListItem> planItems = Collections.emptyList();
    private Consumer<PlanListItem> onAddClickedHandler;

    public void setSearchListItems(List<SearchListItem> newSearchItems){
        this.searchItems.clear();
        this.searchItems = newSearchItems;
        notifyDataSetChanged();
    }

    public void setOnAddClickedHandler(Consumer<PlanListItem> onAddClickedHandler) {
        this.onAddClickedHandler = onAddClickedHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSearchItem(searchItems.get(position));
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

//    public String getItemId(int position) {
//        return searchItems.get(position).id;
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView add_btn;
        private SearchListItem searchItem;
        private PlanListItem planItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.search_item_text);
            this.add_btn = itemView.findViewById(R.id.add_btn);

            this.add_btn.setOnClickListener(view -> {
                onAddClickedHandler.accept(planItem);
            });
        }

        public SearchListItem getSearchItem() { return searchItem;}

        public void setSearchItem(SearchListItem searchItem) {
            this.searchItem = searchItem;
            this.textView.setText(searchItem.name);
        }
    }
}
