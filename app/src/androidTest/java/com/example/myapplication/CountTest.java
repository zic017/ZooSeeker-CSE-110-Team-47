package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class CountTest {
    @Rule
    public ActivityScenarioRule<SearchListActivity> scenarioRule = new ActivityScenarioRule<>(SearchListActivity.class);
    @Test
    public void testCountInit(){
        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {
                TextView count = activity.findViewById(R.id.plan_count);
                assertEquals(count.getText().toString(), "0");
            });
        }
    }

    public void testCountAdd() {
        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {
                TextView count = activity.findViewById(R.id.plan_count);
                activity.updateDisplayList("Gorillas");
                assertEquals(count.getText().toString(), "1");
            });
        }
    }
}
