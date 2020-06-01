package it.univaq.disim.mwt.android_native_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;

public class TvShowDetailsActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);

        textView = findViewById(R.id.tvShowDetailsTextView);
        textView.setText(((TvShowPreview)getIntent().getParcelableExtra("data")).getName());
    }
}
