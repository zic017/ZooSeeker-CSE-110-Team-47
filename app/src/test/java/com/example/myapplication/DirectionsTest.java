package com.example.myapplication;

import static org.junit.Assert.assertNotEquals;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class DirectionsTest {

    @Before
    public void init() {

    }

    @Test //Test that the next button works
    public void test_next_directions() {
        // Create a "scenario" to move through the activity lifecycle.
        // https://developer.android.com/guide/components/activities/activity-lifecycle
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DirectionsActivity.class);
        ArrayList<String> plannedList = new ArrayList<>();
        plannedList.add("koi");
        plannedList.add("flamingo");
        plannedList.add("gorilla");
        intent.putStringArrayListExtra("key", plannedList);

        // Make sure the activity is in the created state (so onCreated is called).

        // When it's ready, we're ready to test inside this lambda (anonymous inline function).
        ActivityScenario.launch(intent).onActivity(activity -> {


            TextView directions = activity.findViewById(R.id.detailed_directions);
            TextView currentLocation = activity.findViewById(R.id.currentLocation);
            Button next_exhibit = activity.findViewById(R.id.next_button);
            String start_directions = directions.getText().toString();
            String first_destination = currentLocation.getText().toString();

            next_exhibit.performClick();

            String new_directions = directions.getText().toString();
            String new_destination = currentLocation.getText().toString();

            assertNotEquals(start_directions, new_directions);
            assertNotEquals(first_destination, new_destination);
        });
    }

    @Test //Test that users are navigated back to the exit after visiting all exhibits.
    public void test_exit() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DirectionsActivity.class);
        ArrayList<String> plannedList = new ArrayList<>();
        plannedList.add("koi");
        plannedList.add("flamingo");
        plannedList.add("gorilla");
        intent.putStringArrayListExtra("key", plannedList);

        // Make sure the activity is in the created state (so onCreated is called).

        // When it's ready, we're ready to test inside this lambda (anonymous inline function).
        ActivityScenario.launch(intent).onActivity(activity -> {

            TextView directions = activity.findViewById(R.id.detailed_directions);
            TextView currentLocation = activity.findViewById(R.id.currentLocation);
            Button next_exhibit = activity.findViewById(R.id.next_button);

            while(next_exhibit.getVisibility() == View.VISIBLE) {
                next_exhibit.performClick();
            }
            String output = directions.getText().toString();

            assert(output.equals("Route Completed. Thank you for visiting!"));
            assert(currentLocation.getVisibility() == View.INVISIBLE);
            assert(next_exhibit.getVisibility() == View.INVISIBLE);

        });
    }

    @Test //Test that brief and detailed directions are different
    public void test_detailed_vs_brief() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DirectionsActivity.class);
        ArrayList<String> plannedList = new ArrayList<>();
        plannedList.add("gorilla");
        intent.putStringArrayListExtra("key", plannedList);

        // Make sure the activity is in the created state (so onCreated is called).

        // When it's ready, we're ready to test inside this lambda (anonymous inline function).
        ActivityScenario.launch(intent).onActivity(activity -> {

            TextView detailed_directions = activity.findViewById(R.id.detailed_directions);
            TextView currentLocation = activity.findViewById(R.id.currentLocation);
            Button next_exhibit = activity.findViewById(R.id.next_button);
            TextView brief_directions = activity.findViewById(R.id.brief_directions);


            assert(currentLocation.getText().toString().equals("Gorillas"));
            assert(!detailed_directions.getText().toString().equals(brief_directions.getText().toString()));
        });

    }
}
