package it.univaq.disim.mwt.android_native_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;

public class TvShowDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    //private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);

        toolbar = findViewById(R.id.tv_show_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //textView = findViewById(R.id.tvShowDetailsTextView);
        //textView.setText(((TvShowPreview)getIntent().getParcelableExtra("data")).getName());
    }
}
