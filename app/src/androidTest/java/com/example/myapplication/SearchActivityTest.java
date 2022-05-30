package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {
    @Test
    public void testOnLoadComponentExistence() {
        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
            scenario.onActivity(activity -> {
                SearchView bar = activity.findViewById(R.id.search_bar);
                assertEquals(bar.getQueryHint(),"Search for Exhibits");
                RecyclerView searchList = activity.findViewById(R.id.search_list);
                assertEquals(searchList.getVisibility(),View.INVISIBLE);
                TextView planCount = activity.findViewById(R.id.plan_count);
                assertEquals(planCount.getText(), "0");
                RecyclerView planList = activity.findViewById(R.id.plan_list_view);
                assertEquals(planList.getVisibility(),View.VISIBLE);
            });
        }
    }


//    @Test
//    public void testPlanButton() {
//        try (ActivityScenario<SearchListActivity> scenario = ActivityScenario.launch(SearchListActivity.class)) {
//            scenario.onActivity(activity -> {
//
//            });
//        }
//
//    }
}

