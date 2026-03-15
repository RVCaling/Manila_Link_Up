package com.manilalinkup.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class JobSeekerDashboardActivity extends AppCompatActivity {

    RecyclerView suggestedRecycler, newRecycler;
    JobAdapter suggestedAdapter, newAdapter;
    List<Job> suggestedJobs = new ArrayList<>();
    List<Job> newJobs = new ArrayList<>();
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_dashboard);

        // Initialize views
        suggestedRecycler = findViewById(R.id.suggestedJobsRecycler);
        newRecycler = findViewById(R.id.newJobsRecycler);
        bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setItemIconTintList(getResources().getColorStateList(R.color.white, getTheme()));

        // Sample data for Suggested Jobs
        suggestedJobs.add(new Job(
                "Events/Catering Helper",
                "Eng Bee Tin",
                "Binondo, Manila",
                "₱700 / Day",
                "March 30, 2026"));

        suggestedJobs.add(new Job(
                "Delivery Rider",
                "Lalamove",
                "Makati City",
                "₱120 / Hour",
                "March 28, 2026"));

        // Sample data for New Jobs
        newJobs.add(new Job(
                "Barista",
                "Starbucks",
                "BGC, Taguig",
                "₱500 / Day",
                "March 29, 2026"));

        newJobs.add(new Job(
                "Cashier",
                "7-Eleven",
                "Quezon City",
                "₱450 / Day",
                "March 30, 2026"));

        // Set up adapters
        suggestedAdapter = new JobAdapter(this, suggestedJobs);
        newAdapter = new JobAdapter(this, newJobs);

        suggestedRecycler.setLayoutManager(new LinearLayoutManager(this));
        suggestedRecycler.setAdapter(suggestedAdapter);

        newRecycler.setLayoutManager(new LinearLayoutManager(this));
        newRecycler.setAdapter(newAdapter);

        // BottomNavigationView navigation
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Already on dashboard
                return true;
            } else if (id == R.id.nav_saved) {
                startActivity(new Intent(this, SavedJobsActivity.class));
                return true;
            } else if (id == R.id.nav_calendar) {
                startActivity(new Intent(this, ScheduleActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }
}