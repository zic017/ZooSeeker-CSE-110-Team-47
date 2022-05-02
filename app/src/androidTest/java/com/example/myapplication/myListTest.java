package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class myListTest {
    @Test
    public void testSearchBarExistence(){
        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {
                assertEquals(activity.myList.size(), 6);
            });
        }
    }

}