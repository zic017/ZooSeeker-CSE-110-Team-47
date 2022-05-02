package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchListActivity extends AppCompatActivity {
    // Exposed for testing purposes later...
    public RecyclerView recyclerView;
    public AutoCompleteTextView suggestionBox;
    public Spinner spinner;
    public ArrayList<String> myList = new ArrayList<>();

//    private SearchListViewModel viewModel;
    //private TodoListItem todoListItem;

    /*
    ListView listView;

    // Define array adapter for ListView
    ArrayAdapter<String> adapter2;

    // Define array List for List View data


     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        suggestionBox = findViewById(R.id.suggestion_box);


//        viewModel = new ViewModelProvider(this)
//                .get(TodoListViewModel.class);

        SearchListAdapter adapter = new SearchListAdapter();
        adapter.setHasStableIds(true);
//        adapter.setOnAddClickedHandler(viewModel::setDeleted);
//        viewModel.getTodoListItems().observe(this, adapter::setTodoListItems);

//        recyclerView = findViewById(R.id.search_results);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //       recyclerView.setAdapter(adapter);


        adapter.setSearchListItems(SearchListItem.loadJSON(this, "sample_node_info.json"));

        //listView = findViewById(R.id.listView);

        // Add items to Array List
        myList.add("Entrance and Exit Gate");
        myList.add("Gorillas");
        myList.add("Alligators");
        myList.add("Lions");
        myList.add("Elephant Odyssey");
        myList.add("Arctic Foxes");

        ArrayAdapter<String> myListAdapter = new ArrayAdapter<String>(
                SearchListActivity.this, android.R.layout.simple_spinner_dropdown_item
                , myList
        );

        suggestionBox.setAdapter(myListAdapter);
/*
        // Set adapter to ListView
        adapter2
                = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mylist);
        listView.setAdapter(adapter2);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem
                = menu.findItem(R.id.search_bar);
        SearchView searchView
                = (SearchView) MenuItemCompat
                .getActionView(searchViewItem);

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        // If the list contains the search query
                        // than filter the adapter
                        // using the filter method
                        // with the query as its argument
                        if (mylist.contains(query)) {
                            adapter2.getFilter().filter(query);
                        }
                        else {
                            // Search query not found in List View
                            Toast
                                    .makeText(SearchListActivity.this,
                                            "Not found",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText)
                    {
                        adapter2.getFilter().filter(newText);
                        return false;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

 */
    }

}