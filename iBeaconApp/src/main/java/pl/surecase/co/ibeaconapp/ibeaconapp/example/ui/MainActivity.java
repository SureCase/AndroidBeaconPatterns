package pl.surecase.co.ibeaconapp.ibeaconapp.example.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.surecase.co.ibeaconapp.ibeaconapp.R;

public class MainActivity extends Activity {

    private Button btnSetup;
    private Button btnClassicPattern;
    private Button btnKalmanPattern;
    private Button btnSmoothingPattern;
    private Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSetup = (Button) findViewById(R.id.btnSetupBeacon);
        btnClassicPattern = (Button) findViewById(R.id.btnPatternClassic);
        btnKalmanPattern = (Button) findViewById(R.id.btnPattern1);
        btnSmoothingPattern = (Button) findViewById(R.id.btnPattern2);
        btnSettings = (Button) findViewById(R.id.btnSettings);

        setupButtons();
    }

    private void setupButtons() {
        btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetupBeaconActivity.class);
                startActivity(intent);
            }
        });

        btnClassicPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClassicPatternActivity.class);
                startActivity(intent);
            }
        });

        btnKalmanPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KalmanPatternActivity.class);
                startActivity(intent);
            }
        });

        btnSmoothingPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SmoothingPatternActivity.class);
                startActivity(intent);
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}

