package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import android.widget.AutoCompleteTextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchListTest {
    @Test
    public void testSearchBarExistence(){
        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {
                assertEquals(activity.ItemList.size(), 27);
            });
        }
    }

}

