package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;

public class EpisodeActivity extends AppCompatActivity {

    private Episode episode;
    private Episode episodeDetailed;
    private TextView episodeName;
    private TextView episodeOverview;
    private TextView episodeAirDate;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_EPISODE:
                        episodeDetailed = (Episode) intent.getSerializableExtra(DataParserService.EXTRA);

                        episodeName.setText(episodeDetailed.getName());
                        episodeOverview.setText(episodeDetailed.getOverview());
                        episodeAirDate.setText(episodeDetailed.getAir_date());

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
        setContentView(R.layout.activity_episode);

        episode = (Episode) getIntent().getSerializableExtra("data");

        episodeName = findViewById(R.id.episode_name);

        episodeOverview = findViewById(R.id.episode_overview);

        episodeAirDate = findViewById(R.id.episode_air_date);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOW_EPISODE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, intentFilter);

        if(episode.getTv_show_id() != 0 && episodeDetailed == null){
            TMDB.requestRemoteTvShowEpisode(getApplicationContext(), episode.getTv_show_id(), episode.getSeason_number(), episode.getEpisode_number());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
