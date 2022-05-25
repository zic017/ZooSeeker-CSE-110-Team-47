package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.app.Activity;
import android.content.Intent;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.textview.MaterialTextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class planlistTest {
    @Rule
    public ActivityScenarioRule<SearchListActivity> scenarioRule = new ActivityScenarioRule<>(SearchListActivity.class);

    @Test //Test that the next button works
    public void test_next_directions() {

        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {
                RecyclerView planList = activity.findViewById(R.id.plan_list_view);
                activity.updateDisplayList("Gorillas");
                TextView count = activity.findViewById(R.id.plan_count);
                assertEquals(1, planList.getAdapter().getItemCount());
            });
        }

        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {
                RecyclerView planList = activity.findViewById(R.id.plan_list_view);
                activity.updateDisplayList("Gorillas");
                activity.updateDisplayList("Lions");
                assertEquals(2, planList.getAdapter().getItemCount());
            });
        }
    }
}
