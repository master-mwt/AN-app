package it.univaq.disim.mwt.android_native_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// TODO: geolocation
public class InfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    // TODO: Change variables name
    private TextView textView1;
    private TextView textView2;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        textView1 = findViewById(R.id.textView2);
        textView1.setText("Testo 1");

        textView2 = findViewById(R.id.textView3);
        textView2.setText("Testo 2");

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(supportMapFragment != null){
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // TODO: logic
        LatLng position = new LatLng(41.902782, 12.496366);

        MarkerOptions options = new MarkerOptions();
        options.title("Marker");
        options.position(position);
        map.addMarker(options);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12f));
    }
}
