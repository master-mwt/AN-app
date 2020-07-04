package it.univaq.disim.mwt.android_native_app;

import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import it.univaq.disim.mwt.android_native_app.dialogs.PositionPermissionDeniedDialogFragment;
import it.univaq.disim.mwt.android_native_app.utils.LocationPermission;

public class InfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    // TODO: logo ?
    private Toolbar toolbar;
    private GoogleMap map;
    private Marker marker;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        toolbar = findViewById(R.id.info_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(supportMapFragment != null){
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Universita degli Studi dell'Aquila
        LatLng position = new LatLng(42.36779, 13.352897);

        MarkerOptions options = new MarkerOptions();
        options.title(getString(R.string.university_of_laquila));
        options.position(position);
        marker = map.addMarker(options);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12f));

        if(LocationPermission.isLocationPermissionGranted(this)){
            // geolocation
            getUserPosition();
        } else {
            LocationPermission.requestLocationPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LocationPermission.REQUEST_PERMISSION_CODE){
            if(LocationPermission.isLocationPermissionGranted(this)){
                // geolocation
                getUserPosition();
            } else {
                // permissions not granted
                showPermissionNotGrantedDialog();
            }
        }
    }

    private void getUserPosition(){
        if(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS){
            // Google Play Services available
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLocationAvailability().addOnCompleteListener(new OnCompleteListener<LocationAvailability>() {
                @Override
                public void onComplete(@NonNull Task<LocationAvailability> task) {
                    LocationAvailability locationAvailability = task.getResult();

                    if(locationAvailability == null || !(locationAvailability.isLocationAvailable())){
                        // location not available already
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                setUserLocation(locationResult.getLastLocation());
                                fusedLocationProviderClient.removeLocationUpdates(this);
                            }
                        }, Looper.getMainLooper());
                    } else {
                        // location available already
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                setUserLocation(location);
                            }
                        });
                    }

                }
            });
        }
    }

    private void setUserLocation(Location location){
        if(location != null){
            LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());

            MarkerOptions options = new MarkerOptions();
            options.title("current user");
            options.position(userPosition);
            map.addMarker(options);

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boundsBuilder.include(userPosition).include(marker.getPosition());

            map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));
        }
    }

    private void showPermissionNotGrantedDialog(){
        PositionPermissionDeniedDialogFragment fragment = new PositionPermissionDeniedDialogFragment();
        fragment.setCancelable(false);
        fragment.show(getSupportFragmentManager(), "position_denied_dialog");
    }
}
