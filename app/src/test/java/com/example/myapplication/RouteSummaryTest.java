package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class RouteSummaryTest {

    @Test
    public void test_clear() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), RoutePlanSummaryActivity.class);
        ArrayList<String> plannedListNames = new ArrayList<>();
        plannedListNames.add("koi");
        plannedListNames.add("flamingo");
        plannedListNames.add("gorilla");
        intent.putStringArrayListExtra("key", plannedListNames);

        ActivityScenario.launch(intent).onActivity(activity -> {
            Button clearList = activity.findViewById(R.id.routePlanClearBtn);
            TextView exhibitListCount = activity.findViewById(R.id.plannedExhibitCount);
            assertEquals(exhibitListCount.getText().toString(), "3");

            clearList.performClick();

            assertEquals(exhibitListCount.getText().toString(), "0");
        });
    }
}
