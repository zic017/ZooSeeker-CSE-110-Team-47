package com.example.myapplication;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RetainExhibitsTest {
    @Test
    public void testRetainExhibits() {
        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {

                scenario.moveToState(Lifecycle.State.DESTROYED);
            });
        }
    }
}
