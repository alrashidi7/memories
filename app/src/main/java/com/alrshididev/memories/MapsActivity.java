package com.alrshididev.memories;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static double latlng ;

    //varables in needed
    private GoogleMap mMap;
    int requestLocation = 1;
    Location myLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    LatLng latLng;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //check if SDK > 23
        checkPermission();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

    }
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestLocation);
                return;
            }
        }
        //if SDK <23 go to location
        getLocation();
    }

    //onrequestPermissionResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == requestLocation) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                getLocation();
            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //the finction will excute to get locatio
    public void getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,getPendingIntent());
        final Task location = fusedLocationProviderClient.getLastLocation();

        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {

                    Toast.makeText(MapsActivity.this, "onComplete: found location!", Toast.LENGTH_SHORT).show();
                    myLocation = (Location) task.getResult();
                    assert myLocation != null;
                     latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(latLng).title("me")
                            .snippet("this your location").rotation(14f));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
                    // and longitude,latitude to database;
                    reference.child("latlng").setValue(myLocation);


                } else {
                    Toast.makeText(MapsActivity.this, "unable to get current location now please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private PendingIntent getPendingIntent(){
        Intent intent = new Intent(MapsActivity.this,MyService.class);
        intent.setAction(MyService.ACTION_PROCESS);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

    }

}


