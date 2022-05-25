package com.example.myapplication;

import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class DisplayExhibitsTest {
    @Rule
    public ActivityScenarioRule<SearchListActivity> scenarioRule = new ActivityScenarioRule<>(SearchListActivity.class);
    @Test
    public void test_add() {
        ActivityScenario<SearchListActivity> scenario = scenarioRule.getScenario();
        // Make sure the activity is in the created state (so onCreated is called).
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            RecyclerView RV = activity.findViewById(R.id.search_list);
            assert(RV.getVisibility() == View.INVISIBLE);

        });
    }
}
