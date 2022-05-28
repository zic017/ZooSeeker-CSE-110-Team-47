package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DirectionsActivity extends AppCompatActivity {

    private TextView detailed_directions;
    private TextView brief_directions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);


        Intent i = getIntent();
        ArrayList<String> input = i.getStringArrayListExtra("key");
        Context context = getApplicationContext();

        DirectionsAlgorithm dirAlgo = new DirectionsAlgorithm(input, context);

        Button nextButton = findViewById(R.id.next_button);

        detailed_directions = (TextView) findViewById(R.id.detailed_directions);
        brief_directions = (TextView) findViewById(R.id.brief_directions);

        TextView currentLocation = (TextView) findViewById(R.id.currentLocation);
        TextView directionsTo = (TextView) findViewById((R.id.directionsTo));

        dirAlgo.getNext();
        detailed_directions.setText(dirAlgo.getDetailedDirections());
        brief_directions.setText(dirAlgo.briefDirectionsLine);

        currentLocation.setText(dirAlgo.currentName);

        detailed_directions.setMovementMethod(new ScrollingMovementMethod());
        brief_directions.setMovementMethod(new ScrollingMovementMethod());

        detailed_directions.setVisibility(View.INVISIBLE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                detailed_directions.scrollTo(0,0);
                brief_directions.scrollTo(0, 0);

                dirAlgo.getNext();
                if(dirAlgo.currentName != "DONE") {
                    currentLocation.setText(dirAlgo.currentName);

                    detailed_directions.setText(dirAlgo.getDetailedDirections());
                    brief_directions.setText(dirAlgo.getBriefDirections());
                }
                else {
                    currentLocation.setVisibility(View.INVISIBLE);
                    brief_directions.setVisibility(View.INVISIBLE);
                    detailed_directions.setVisibility(View.VISIBLE);
                    detailed_directions.setText(R.string.thank_you);
                    brief_directions.setText(R.string.thank_you);
                    brief_directions.setTextSize(24);
                    directionsTo.setVisibility(View.INVISIBLE);
                    detailed_directions.setTextSize(24);
                    nextButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.directions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.brief) {
            brief_directions.setVisibility(View.VISIBLE);
            detailed_directions.setVisibility(View.INVISIBLE);
            return true;
        }
        else {
            brief_directions.setVisibility(View.INVISIBLE);
            detailed_directions.setVisibility(View.VISIBLE);
            return true;
        }
    }
}