package com.example.kaisa.androidproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Looper.getMainLooper;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class JoggingFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //yeet
    Button startButton;
    public static final int RequestPermissionCode = 1;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest = null;
    Location locationOld = null;
    Location locationNew = null;
    float distance = 0;
    float distance2 = 0;
    boolean locationservices = false;
    private SensorManager sensorManager;
    private Sensor sensor;
    float gravity[] = {0, 0, 0};
    float linear_acceleration[] = {0, 0, 0};
    double totalacceleration = 0;
    TextView tv1 = null;
    TextView tv2 = null;
    TextView previousWalk = null;
    LocationCallback mLocationCallback = null;
    SensorEventListener sensorlistener = null;
    Date startTime = null;
    Date stopTime = null;
    boolean jogStarted = false;
    String elapsedTime;
    String currentDate;
    NonSwipeableViewPager testPager;
    MainActivity context;
    DbModel model = null;
    User user = null;
    int startsteps = 0;
    int stopsteps = 0;
    double dbdistance = 0;
    double dbwalktime = 0;
    String dbwalkdate = null;
    double jogtimeseconds = 0;
    SharedPreferences prefs = null;
    Typeface pixelFont = null;
    private boolean isVisible;
    private boolean isStarted;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = (MainActivity) container.getContext();
        return inflater.inflate(R.layout.fragment_jogging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        pixelFont = Typeface.createFromAsset(getContext().getAssets(),  "fonts/smallest_pixel-7.ttf");
        testPager = context.viewPager;
        initializedb();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (jogStarted == true) {
                            Toast.makeText(getActivity(), "Please press stop before you exit", Toast.LENGTH_SHORT).show();
                        } else {
                            ((MainActivity) getActivity()).setFragmentToHome();

                        }
                        return true;
                    }
                }
                return false;
            }
        });
        tv1 = getView().findViewById(R.id.tv1);
        tv2 = getView().findViewById(R.id.tv2);
    googleApiClient = new GoogleApiClient.Builder(getContext())
            .addOnConnectionFailedListener(this).
                    addConnectionCallbacks(this).addApi(LocationServices.API).build();
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        final int locationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        checkLocationStatus();
        previousWalk = getActivity().findViewById(R.id.prev_walk_stats);
        startButton = getView().findViewById(R.id.start_jog_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                    Toast.makeText(getActivity(), "You need to permit location to use jog functionality", Toast.LENGTH_SHORT).show();
                } else {
                    statusCheck();
                    if(locationservices==true){
                    if (jogStarted==false) {
                        context.navigation.setVisibility(View.INVISIBLE);
                        context.imageButton.setEnabled(false);
                        testPager.disableScroll(true);
                        startButton.setBackgroundResource(R.drawable.stop_run_button);
                        requestLocationUpdates();
                        startSensor();
                        getTime();
                        jogStarted = true;
                        startsteps = user.getSteps();

                    } else {
                        context.navigation.setVisibility(View.VISIBLE);
                        context.imageButton.setEnabled(true);
                        stopsteps = user.getSteps();
                        startButton.setBackgroundResource(R.drawable.start_run_button);
                        testPager.disableScroll(false);
                        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                        compareTime();
                        jogStarted = false;
                        savedatatodb();
                        resetValues();
                        locationservices=false;
                    }
                }
                else{
                        Toast.makeText(getActivity(), "Please enable GPS to use jog feature", Toast.LENGTH_LONG).show();
                    }
                }
            }

        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && isStarted) {
            createDialog();
        }
    }

    public void createDialog() {
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        if (!prefs.getBoolean("appHasRunBeforeWalk", false)) {
            LayoutInflater inflater = (LayoutInflater) this
                    .getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialoglayout = inflater.inflate(R.layout.instruction_dialog_layout, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialoglayout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.tekstilaatikko));
            alertDialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
            ImageButton doneBtn = dialoglayout.findViewById(R.id.dialog_done_btn);
            TextView titleText = dialoglayout.findViewById(R.id.dialog_welcome_text);
            titleText.setTypeface(pixelFont);
            titleText.setText("Go outside and walk!");
            TextView bodyText = dialoglayout.findViewById(R.id.dialog_instruction_text);
            bodyText.setTypeface(pixelFont);
            bodyText.setText("But before starting your walk or run outside, press the start run button. When you're finished press the button again. " +
                    "This way the stats from your walk or run will be saved. " +
                    "You can also view the stats from your previous walk or run below the button.");
            doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            prefs.edit().putBoolean("appHasRunBeforeWalk", true).apply();
            Log.d("homefragment", "firstrun");
        }
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

    public void startSensor() {
        sensorManager.registerListener(sensorlistener = new SensorEventListener() {
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

                double x = Math.pow(linear_acceleration[0], 2);
                double y = Math.pow(linear_acceleration[1], 2);
                double z = Math.pow(linear_acceleration[2], 2);
                totalacceleration = Math.sqrt(x + y + z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);
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
    public void resetValues() {
        locationNew = null;
        locationOld = null;
        distance = 0;
        distance2 = 0;
        sensorManager.unregisterListener(sensorlistener, sensor);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void requestLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(2000);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();

        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    updategps();

                    super.onLocationResult(locationResult);
                }
            }, getMainLooper());
        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
        isStarted = true;
        if (isVisible) {
            createDialog();
        }

    }

    @Override
    public void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        /*
        if (jogStarted==true) {
            context.navigation.setVisibility(View.VISIBLE);
            context.imageButton.setEnabled(true);
            stopsteps = user.getSteps();
            testPager.disableScroll(false);
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            compareTime();
            jogStarted = false;
            savedatatodb();
            resetValues();
            locationservices = false;
        }
        */
        super.onStop();
        isStarted = false;
    }

    public void updategps() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()

                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                locationNew = location;
                                if (locationOld == null) {
                                    tv1.setText("distance: " + "0");
                                    tv2.setText("longitude " + location.getLongitude() + " latitudi " + location.getLatitude());
                                    locationOld = locationNew;
                                } else {
                                    if (totalacceleration > 0.7) {
                                        distance = locationNew.distanceTo(locationOld);
                                        distance2 = distance + distance2;
                                        locationOld = locationNew;
                                        tv2.setText("longitude " + location.getLongitude() + " latitudi " + location.getLatitude() + " nopeus " + totalacceleration);
                                        tv1.setText("Distance:" + distance2);
                                    } else {

                                        tv1.setText("Distance:" + distance2);
                                        tv2.setText("longitude " + location.getLongitude() + " latitudi " + location.getLatitude() + " nopeus " + totalacceleration);
                                    }
                                }
                            }
                        }
                    });
        }

    }

    public void getTime() {
        startTime = Calendar.getInstance().getTime();
    }

    public void compareTime() {
        stopTime = Calendar.getInstance().getTime();

        long mills = stopTime.getTime() - startTime.getTime();
        int hours = (int) (mills / (1000 * 60 * 60));
        int mins = (int) (mills / (1000 * 60)) % 60;
        int sec = (int) (mills / 1000);
        elapsedTime = hours + ":" + mins + ":" + sec;
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv1.setText("elapsed time: " + elapsedTime + " " + currentDate);
        jogtimeseconds = sec + (mins * 60) + (hours / 60 / 60);


    }

    public void initializedb() {
        model = new DbModel(getContext());

        if(!model.checkIfTableEmpty("user")) {
            try {
                user = model.readUserFromDb();


            } catch (SQLiteException e) {
                if (e.getMessage().contains("no such table")) {
                    Log.v("stepsdb", "table doesn't exist");
                }
            }
        } else {

        }
    }

    public void savedatatodb() {

        dbdistance = user.getTotalDistance();
        user.setDailyDistance(distance2);
        dbdistance = dbdistance + distance2;
        dbdistance = dbdistance - ((stopsteps - startsteps) * 0.5);
        user.setTotalDistance(dbdistance);

        if (user.getWalkTime().length() < 1) {
            double totalwalktime = jogtimeseconds;
            user.setWalkTime(Double.toString(totalwalktime));

        } else {
            dbwalktime = Double.valueOf(user.getWalkTime());
            double totalwalktime = jogtimeseconds + dbwalktime;
            user.setWalkTime(Double.toString(totalwalktime));
        }
        user.setWalkDate(currentDate);
        model.updateUser(user);
        tv1.setText(distance2 + " " + Double.toString(user.getTotalDistance()) + " aika> " + user.getWalkTime() + "< " + Double.toString(jogtimeseconds));
    }

    public void checkLocationStatus() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        lm.addGpsStatusListener(new android.location.GpsStatus.Listener() {
            public void onGpsStatusChanged(int event) {
                switch (event) {
                    case GPS_EVENT_STARTED:
                        // do your tasks
                        break;
                    case GPS_EVENT_STOPPED:
                        if (jogStarted==true) {
                        context.navigation.setVisibility(View.VISIBLE);
                        context.imageButton.setEnabled(true);
                        stopsteps = user.getSteps();
                        testPager.disableScroll(false);
                        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                        compareTime();
                        jogStarted = false;
                        savedatatodb();
                        resetValues();
                        locationservices = false;
                            Toast.makeText(getActivity(), "Please enable GPS", Toast.LENGTH_LONG).show();

                        }


                        break;
                }
            }
        });

    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            locationservices = true;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("You need to enable gps to use jog feature2")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}