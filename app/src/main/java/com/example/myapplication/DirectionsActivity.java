package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DirectionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("gorillas");
        testArray.add("gators");
        testArray.add("elephant_odyssey");
        Context context = getApplicationContext();

        DirectionsAlgorithm d = new DirectionsAlgorithm(testArray,context);

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.directions);
                textView.setText(d.current);
                d.getNext();
            }
        });
    }
}