package com.example.pasitosappv2;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.Format;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Controller controller;
    private LatLng inicial = new LatLng(37.375483, -5.959308);
    private ImageButton borrar;
    private int seg = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        controller = new Controller(this);

        borrar = findViewById(R.id.imageButton);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.eliminarPasitos();
                mMap.clear();
                rellenarMapa(controller.obtenerPasos());
            }
        });


    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicial, 15));
        ArrayList<POGOPasos> listaPasos = controller.obtenerPasos();

        if(listaPasos.size()!=0){
            rellenarMapa(listaPasos);
            POGOPasos paso = listaPasos.get(listaPasos.size()-1);
            LatLng marca = new LatLng(paso.getLatitud(), paso.getLongitud());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marca, 15));
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });

    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Localizar Local = new Localizar();
        Local.setMainActivity(this);
        final boolean gpsEnabled =
                mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, seg*1000, 0,  Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, seg*1000, 0, Local);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == 1000){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }



    public class Localizar implements LocationListener {
        MapsActivity mapsActivity;

        public MapsActivity getMainActivity() {
            return mapsActivity;
        }
        public void setMainActivity(MapsActivity mainActivity) {
            this.mapsActivity = mainActivity;
        }

        public void onLocationChanged(Location loc) {
            loc.getLatitude();
            loc.getLongitude();
            if (loc.getLatitude()!=0.0 && loc.getLongitude()!=0.0) {
                this.mapsActivity.setLocation(loc);
                setMarca(loc.getLatitude(), loc.getLongitude());
            }

        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider){}
        @SuppressWarnings("deprecation")
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case 0:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case 1:
                    Log.d("debug",
                            "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
                case 2:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
            }
        }
    }

    public void setLocation(Location loc)
    {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setMarca(double latitud, double longitud) {
        IntentFilter filter = new
                IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, filter);
        Integer bateria = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        String hora = String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm:ss")));
        POGOPasos newPasos = new POGOPasos(null, hora, bateria, latitud, longitud);
        controller.nuevoPaso(newPasos);
        marcar(latitud, longitud, bateria, hora);
    }

    private void marcar(double latitud, double longitud, Integer bateria,
                        String hora) {
        LatLng pn = new LatLng(latitud, longitud);
        MarkerOptions marker = new MarkerOptions().position(pn).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(bateria + "%" + " - " + hora);
        mMap.addMarker(marker).showInfoWindow();
    }

    private void rellenarMapa(ArrayList<POGOPasos> listaPasos) {
        for(int i = 0; i<listaPasos.size(); i++){
            POGOPasos paso = listaPasos.get(i);
            marcar(paso.getLatitud(), paso.getLongitud(), paso.getBateria(), paso.getFecha());
        }
    }

}