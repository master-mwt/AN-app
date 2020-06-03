package it.univaq.disim.mwt.android_native_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import it.univaq.disim.mwt.android_native_app.model.Season;

public class SeasonActivity extends AppCompatActivity {

    private TextView textView;
    private Season season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        textView = findViewById(R.id.season_name);

        season = (Season) getIntent().getSerializableExtra("data");
    }

    @Override
    protected void onStart() {
        super.onStart();

        textView.setText(season.getName());
    }
}
