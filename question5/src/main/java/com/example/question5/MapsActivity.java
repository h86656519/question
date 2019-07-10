package com.example.question5;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.logging.Logger;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private static final int REQUSETCODE = 100;
    Location location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(MapsActivity.LOCATION_SERVICE);
        getCurrentLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkPermission()) {
            if (location != null) {
                drawMarker(location);
            }
        } else {
            askPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
        Toast.makeText(MapsActivity.this, "還沒抓到位置，要在等一下", Toast.LENGTH_LONG).show();
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //gps有沒有開
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); //網路有沒有開
        location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Toast.makeText(MapsActivity.this, "沒網路?? 沒gps??", Toast.LENGTH_SHORT).show();
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                    ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            drawMarker(location);
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    public void askPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE},
                REQUSETCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUSETCODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermission()) {
                        Toast.makeText(MapsActivity.this, "權限已取得", Toast.LENGTH_SHORT).show();
                        mMap.setMyLocationEnabled(true);
                        if (location != null) {
                            drawMarker(location);
                        }
                    }
                } else {
                    //判斷 "不在詢問"
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        //沒按下前
                        Toast.makeText(MapsActivity.this, "請到設定開啟權限", Toast.LENGTH_SHORT).show();
                    } else {
                        //有按下後
                        Toast.makeText(MapsActivity.this, "若要使用次功能，請到設定開啟權限", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    private void drawMarker(Location location) {
        if (checkPermission()) {
            if (mMap != null) {
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("座標位置 : " + location.getLatitude() + "," + location.getLongitude())).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            }
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                drawMarker(location);
                mLocationManager.removeUpdates(mLocationListener); //移除監聽
                Toast.makeText(MapsActivity.this, "onLocationChanged", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MapsActivity.this, "還沒抓到位置，要在等一下", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    };
}
