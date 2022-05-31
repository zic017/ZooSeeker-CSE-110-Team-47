package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DirectionsActivity extends AppCompatActivity {

    private TextView detailed_directions;
    private TextView brief_directions;
    private EditText coords;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String provider;
    protected Double latitude, longitude;
    private final PermissionChecker permissionChecker = new PermissionChecker(this);
    public DirectionsAlgorithm dirAlgo;
    private Button nextButton;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        Intent i = getIntent();
        ArrayList<String> input = i.getStringArrayListExtra("key");

        if (permissionChecker.ensurePermissions());

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListenerGPS);

        dirAlgo = new DirectionsAlgorithm(input, this, 32.73459618, -117.14936);

        nextButton = findViewById(R.id.next_button);
        Button previousButton = findViewById(R.id.back_button);
        Button inputButton = findViewById(R.id.inputButton);
        Button skipButton = findViewById(R.id.skip_button);

        detailed_directions = (TextView) findViewById(R.id.detailed_directions);
        brief_directions = (TextView) findViewById(R.id.brief_directions);
        coords = (EditText) findViewById(R.id.location_input);

        TextView currentLocation = (TextView) findViewById(R.id.currentLocation);
        TextView directionsTo = (TextView) findViewById((R.id.directionsTo));

        dirAlgo.getNext();
        detailed_directions.setText(dirAlgo.getDetailedDirections());
        brief_directions.setText(dirAlgo.getBriefDirections());

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
                    previousButton.setVisibility(View.INVISIBLE);
                    coords.setVisibility(View.INVISIBLE);
                    inputButton.setVisibility(View.INVISIBLE);
                    skipButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dirAlgo.getPrevious();
                brief_directions.setText(dirAlgo.getBriefDirections());
                detailed_directions.setText(dirAlgo.getDetailedDirections());

                currentLocation.setText(dirAlgo.currentName);

            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String input = coords.getText().toString();
                coords.setText("");
                input = input.replaceAll(" ", "");
                int com = input.indexOf(",");
                if(com != -1) {
                    latitude = Double.parseDouble(input.substring(0, com));
                    longitude = Double.parseDouble(input.substring(com + 1));
                    dirAlgo.updateLocation(latitude, longitude);
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Remove exhibit from visited
                dirAlgo.replanSkip();
            }
        });



    }

    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            latitude=location.getLatitude();
            longitude=location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

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

    public void showReplan() {
        nextButton.performClick();
    }

    public void promptReplan() {
        AlertDialog.Builder replanPrompt = new AlertDialog.Builder(this);
        replanPrompt.setMessage("Off Track. Replan?");
        replanPrompt.setCancelable(true);
        replanPrompt.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dirAlgo.replan();
                    }
                });
        replanPrompt.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = replanPrompt.create();
        alert.show();
    }
}