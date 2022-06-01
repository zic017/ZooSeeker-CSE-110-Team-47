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
import java.util.Objects;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList<SearchItem> ItemList;
    private Context context;
    private ArrayList<String> listOfIDs = new ArrayList<>();
    public ArrayList<String> listOfNames = new ArrayList<>();

    public SearchAdapter(ArrayList<SearchItem> ItemList, Context context) {
        this.ItemList = ItemList;
        this.context = context;

    }
    public void filterList(ArrayList<SearchItem> filterllist) {
        ItemList = filterllist;
        notifyDataSetChanged();
    }
    public void clearList(){
        listOfNames.clear();
        listOfIDs.clear();
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

        private String currentExhibit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.search_item_text);
            add_btn = itemView.findViewById(R.id.add_btn);

            /*
                Function: Once user hits '+' this code will execute and add the exhibit to our planned list.
                          The planned list will contain the IDs of all the exhibits the user clicked '+' on.
             */
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // s is the name of the exhibit the user is currently trying to add
                    currentExhibit = itemName.getText().toString();

                    // Checks to see if the exhibit is already in the list, if so don't add it again
                    if (listOfNames.contains(currentExhibit))
                        return;

                    // Retrieves the id of the exhibit because we only have the name
                    for (SearchItem item : ItemList) {
                        if (Objects.equals(item.getName(), currentExhibit)) {
                            listOfIDs.add(item.getId());
                            listOfNames.add(currentExhibit);
                            Log.d("Added correctly", currentExhibit);
                        }
                    }

                    // Update the exhibit list
                    if(context instanceof SearchListActivity) {
                        ((SearchListActivity)context).updateDisplayList(currentExhibit);
                        ((SearchListActivity)context).savePreference();
                    }
                }
            });
        }
    }

    public ArrayList<String> getListOfIds() {
        return listOfIDs;
    }

    public ArrayList<String> getListOfNames(){ return listOfNames;}
}
