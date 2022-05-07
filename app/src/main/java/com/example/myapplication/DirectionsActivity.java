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
        TextView directions = (TextView) findViewById(R.id.directions);
        TextView currentLocation = (TextView) findViewById(R.id.currentLocation);
        TextView directionsTo = (TextView) findViewById((R.id.directionsTo));
        d.getNext();
        directions.setText(d.directionsLine);
        currentLocation.setText(d.currentName);

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.getNext();
                if(d.currentName != "DONE") {
                    currentLocation.setText(d.currentName);
                    directions.setText(d.directionsLine);
                }
                else {
                    currentLocation.setVisibility(View.INVISIBLE);
                    directions.setText(R.string.thank_you);
                    directionsTo.setVisibility(View.INVISIBLE);
                    directions.setTextSize(24);
                    nextButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}