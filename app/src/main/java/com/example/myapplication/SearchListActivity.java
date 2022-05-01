package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class SearchListActivity extends AppCompatActivity {
    // Exposed for testing purposes later...
    public RecyclerView recyclerView;

//    private SearchListViewModel viewModel;
    //private TodoListItem todoListItem;
//    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);


//        viewModel = new ViewModelProvider(this)
//                .get(TodoListViewModel.class);

        SearchListAdapter adapter = new SearchListAdapter();
        adapter.setHasStableIds(true);
//        adapter.setOnAddClickedHandler(viewModel::setDeleted);
//        viewModel.getTodoListItems().observe(this, adapter::setTodoListItems);

        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


//        addTodoButton.setOnClickListener(this::onAddTodoClicked);

        adapter.setSearchListItems(SearchListItem.loadJSON(this, "sample_node_info.json"));
    }

}