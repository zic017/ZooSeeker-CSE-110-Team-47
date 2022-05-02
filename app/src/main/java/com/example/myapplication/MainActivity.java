package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
/*
    AutoCompleteTextView suggestionBox;
    Spinner Items;

    ArrayList<String> foods = new ArrayList<>();


 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        suggestionBox = findViewById(R.id.suggestion_box);
        Items = findViewById(R.id.items);

        foods.add("Apple");
        foods.add("Banana");
        foods.add("Cucumber");
        foods.add("Guava");
        foods.add("Watermelon");
        foods.add("Grapes");
        foods.add("Almonds");
        foods.add("Pineapple");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,foods);
        //ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,foods);

        suggestionBox.setAdapter(adapter);

        Items.setAdapter(adapter1);

         */

        Intent intent = new Intent(this,SearchListActivity.class);
        startActivity(intent);


    }
}