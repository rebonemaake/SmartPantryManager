package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryAlerts;
    private RadioGroup radioUnits;
    private RadioButton radioMetric;
    private RadioButton radioSimple;
    private Button btnSaveSettings;
    private Button btnBackFromSettings;

    private SharedPreferences preferences;

    private static final String PREFS_NAME = "smart_pantry_settings";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_UNIT_MODE = "unit_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        radioUnits = findViewById(R.id.radioUnits);
        radioMetric = findViewById(R.id.radioMetric);
        radioSimple = findViewById(R.id.radioSimple);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnBackFromSettings = findViewById(R.id.btnBackFromSettings);

        preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        loadSettings();

        btnSaveSettings.setOnClickListener(v ->
                saveSettings()
        );

        btnBackFromSettings.setOnClickListener(v ->
                finish()
        );
    }

    private void loadSettings() {

        boolean expiryAlertsEnabled =
                preferences.getBoolean(
                        KEY_EXPIRY_ALERTS,
                        true
                );

        String unitMode =
                preferences.getString(
                        KEY_UNIT_MODE,
                        "metric"
                );

        switchExpiryAlerts.setChecked(
                expiryAlertsEnabled
        );

        if ("simple".equals(unitMode)) {
            radioSimple.setChecked(true);
        } else {
            radioMetric.setChecked(true);
        }
    }

    private void saveSettings() {

        boolean expiryAlertsEnabled =
                switchExpiryAlerts.isChecked();

        String unitMode = "metric";

        if (radioSimple.isChecked()) {
            unitMode = "simple";
        }

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putBoolean(
                KEY_EXPIRY_ALERTS,
                expiryAlertsEnabled
        );

        editor.putString(
                KEY_UNIT_MODE,
                unitMode
        );

        editor.apply();

        Toast.makeText(
                this,
                "Settings saved ♡",
                Toast.LENGTH_SHORT
        ).show();
    }
}