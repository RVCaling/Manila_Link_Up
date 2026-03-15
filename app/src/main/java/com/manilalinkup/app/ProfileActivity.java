package com.manilalinkup.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1️⃣ Initialize BottomNavigationView
        bottomNav = findViewById(R.id.bottomNav);

        // 2️⃣ Highlight current tab
        bottomNav.setSelectedItemId(R.id.nav_profile);

        // 3️⃣ Set navigation listener
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, JobSeekerDashboardActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;
            } else if (id == R.id.nav_saved) {
                startActivity(new Intent(this, SavedJobsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;
            } else if (id == R.id.nav_calendar) {
                startActivity(new Intent(this, ScheduleActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;
            } else if (id == R.id.nav_profile) {
                // Already on Profile, do nothing
                return true;
            }

            return false;
        });
    }
}