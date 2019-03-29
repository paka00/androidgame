package com.example.kaisa.androidproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.os.Looper.getMainLooper;


public class JoggingFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    String startbuttontxt = "Start";
    Button startButton;
    public static final int RequestPermissionCode = 1;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest = null;
    Location locationOld = null;
    Location locationNew = null;
    float distance = 0;
    float distance2 = 0;
    private SensorManager sensorManager;
    private Sensor sensor;
    float gravity[] = {0,0,0};
    float linear_acceleration[]= {0,0,0};
    double totalacceleration = 0;
    TextView tv1 = null;
    TextView tv2 = null;
    LocationCallback mLocationCallback = null;


        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_jogging, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv1= getView().findViewById(R.id.tv1);
        tv2= getView().findViewById(R.id.tv2);


        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addOnConnectionFailedListener(this).
                        addConnectionCallbacks(this).addApi(LocationServices.API).build();


        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        startButton = getView().findViewById(R.id.start_jog_button);
        startButton.setText(startbuttontxt);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(startbuttontxt.equals("Start"))
                {
                    MainActivity.navigation.setVisibility(View.INVISIBLE);
                    MainActivity.imageButton.setEnabled(false);
                    startbuttontxt = "Stop";
                    startButton.setText(startbuttontxt);
                    requestLocationUpdates();
                }
                else{
                    MainActivity.navigation.setVisibility(View.VISIBLE);
                    MainActivity.imageButton.setEnabled(true);
                    startbuttontxt ="Start";
                    startButton.setText(startbuttontxt);
                    fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                    resetValues();


                }

            }
        });


        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {


                final float alpha = (float) 0.8;

                // Isolate the force of gravity with the low-pass filter.
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                // Remove the gravity contribution with the high-pass filter.
                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];

                double x = Math.pow(linear_acceleration[0],2);
                double y = Math.pow(linear_acceleration[1],2);
                double z = Math.pow(linear_acceleration[2],2);
                totalacceleration = Math.sqrt(x + y + z);





            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);



    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();

        } else {


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {

                                tv1.setText("longitude " + location.getLongitude() + " latitudi " + location.getLatitude());


                            }
                        }
                    });
        }


    }

    public void resetValues(){
            locationNew = null;
            locationOld = null;
            distance = 0;
            distance2 = 0;

    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void requestLocationUpdates(){
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(2000);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();

        }

        else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    updategps();

                    super.onLocationResult(locationResult);
                }
            },getMainLooper());
        }

    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }
    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();

    }


    public void updategps() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        else {
            fusedLocationProviderClient.getLastLocation()

                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                locationNew = location;
                                if (locationOld == null)
                                {
                                    tv1.setText("distance: "+"0");
                                    tv2.setText("longitude " + location.getLongitude() + " latitudi " + location.getLatitude());
                                    locationOld = locationNew;
                                }
                                else {
                                    if(totalacceleration>0.7) {
                                        distance = locationNew.distanceTo(locationOld);
                                        distance2 = distance + distance2;
                                        locationOld = locationNew;
                                       tv2.setText("longitude " + location.getLongitude() + " latitudi " + location.getLatitude()+" nopeus " +totalacceleration);
                                       tv1.setText("Distance:" + distance2);


                                    }
                                    else{

                                       tv1.setText("Distance:" + distance2 );
                                       tv2.setText("longitude " + location.getLongitude() + " latitudi " + location.getLatitude()+" nopeus " +totalacceleration);



                                    }
                                }
                            }
                        }
                    });
        }

    }





}
