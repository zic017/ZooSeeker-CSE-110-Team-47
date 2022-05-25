package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import androidx.appcompat.widget.SearchView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchSuggestionTest {
    @Test
    public void testSearchBarExistence(){
        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {
                SearchView search_bar = activity.findViewById(R.id.search_bar);
                assertEquals(search_bar.getQueryHint(), "Search for Exhibits");
            });
        }
    }
}
