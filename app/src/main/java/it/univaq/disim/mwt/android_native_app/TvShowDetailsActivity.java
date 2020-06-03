package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.TvShowDetails;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;
import it.univaq.disim.mwt.android_native_app.utils.VolleyRequest;

public class TvShowDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TvShowDetails tvShowDetails;
    private ImageView imageView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_DETAILS:
                        // progressBar.setVisibility(View.INVISIBLE);
                        tvShowDetails = (TvShowDetails) intent.getSerializableExtra(DataParserService.EXTRA);
                        setTvShowImage(tvShowDetails.getPoster_path());

                        break;
                    default:
                        break;
                }
            }
        }
    };

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

        // recycler view, progressbar and other view obj

        imageView = findViewById(R.id.tv_show_details_toolbar_image);

    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOW_DETAILS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);

        // progressBar.setVisibility(View.VISIBLE);

        long tv_show_id = getIntent().getLongExtra("data", -1);

        if(tv_show_id != -1){
            TMDB.requestRemoteTvShowDetails(this, tv_show_id);
        } else {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void setTvShowImage(String imageUrl){

        String baseUrl = getString(R.string.tmdb_image_baselink);

        if(imageUrl != null && !"".equals(imageUrl) && !"null".equals(imageUrl)){
            String requestUrl = baseUrl + imageUrl;

            VolleyRequest.getInstance(getApplicationContext()).getImageLoader().get(requestUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.getLogger(TvShowDetailsActivity.class.getName()).log(Level.SEVERE, (error.getCause() != null) ? error.getCause().getMessage() : error.getMessage() );
                }
            });
        }
    }
}
