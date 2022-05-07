package com.example.myapplication;

import static org.junit.Assert.assertNotEquals;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DirectionsTest {
    @Rule
    public ActivityScenarioRule<DirectionsActivity> scenarioRule = new ActivityScenarioRule<>(DirectionsActivity.class);

    @Test
    public void test_next_directions() {
        // Create a "scenario" to move through the activity lifecycle.
        // https://developer.android.com/guide/components/activities/activity-lifecycle
        ActivityScenario<DirectionsActivity> scenario = scenarioRule.getScenario();

        // Make sure the activity is in the created state (so onCreated is called).
        scenario.moveToState(Lifecycle.State.CREATED);

        // When it's ready, we're ready to test inside this lambda (anonymous inline function).
        scenario.onActivity(activity -> {
            TextView directions = activity.findViewById(R.id.directions);
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

    @Test
    public void test_exit() {
        // Create a "scenario" to move through the activity lifecycle.
        // https://developer.android.com/guide/components/activities/activity-lifecycle
        ActivityScenario<DirectionsActivity> scenario = scenarioRule.getScenario();
        String track;
        // Make sure the activity is in the created state (so onCreated is called).
        scenario.moveToState(Lifecycle.State.CREATED);

        // When it's ready, we're ready to test inside this lambda (anonymous inline function).
        scenario.onActivity(activity -> {
            TextView directions = activity.findViewById(R.id.directions);
            TextView currentLocation = activity.findViewById(R.id.currentLocation);
            Button next_exhibit = activity.findViewById(R.id.next_button);

            while(!currentLocation.getText().toString().equals("Entrance and Exit Gate")) {
                next_exhibit.performClick();
            }
            next_exhibit.performClick();

            assert(directions.getText().toString().equals("Route Completed. Thank you for visiting!"));
            assert(currentLocation.getVisibility() == View.INVISIBLE);
            assert(next_exhibit.getVisibility() == View.INVISIBLE);

        });
    }
}
