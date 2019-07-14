package com.linklab.emmanuelogunjirin.wearable_heartrate;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import java.io.File;

@SuppressWarnings("ALL")
public class MainActivity extends WearableActivity
{
    private final Preferences Preferences = new Preferences();      // Gets a reference to the preferences class
    private final SystemInformation SystemInformation = new SystemInformation();        // Gets an instance from the system information module
    private Button HeartRateMonitor;         // This is the HeartRate ranger
    private Vibrator vibrator;      // This is the vibrator
    private int HapticFeedback = Preferences.HapticFeedback;        // This is the haptic feedback that the system uses.
    private final String Heart_Rate = Preferences.Heart_Rate;     // Gets the sensors from preferences.
    private final String Subdirectory_Heartrate = Preferences.Subdirectory_HeartRate;        // This is where the Heart Rate is kept
    private boolean Switch = false;     // This is the switch that goes back and forth for the button.

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);         // This is the creation of an instance of the app
        setContentView(R.layout.activity_main);     // Starts the device with the layout specified

        CheckPermissions();     // Calls the check permission module
        CheckFiles();       // Calls the check files module.

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);     // Initiates an instance of the wakelock
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyWakeLockTag:");       // Makes the screen stay awake as long as the app is running
        wakeLock.acquire();     // Acquires the wakelock and hold it

        Log.i("Main Activity", "Application Initiated");     // Logs on Console.

        HeartRateMonitor = findViewById(R.id.HeartRateMonitor);        // This is the Heart Rate ranger button
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);       // This is the vibrator instance from the system.

        final Intent HRService = new Intent(getBaseContext(), HeartRateSensor.class);        // Creates an intent for calling the Heart Rate Timer service.
        final Intent AccelService = new Intent(getBaseContext(), AccelerometerSensor.class);        // Creates an intent for calling the Heart Rate Timer service.
        final Intent GyroService = new Intent(getBaseContext(), GyroscopeSensor.class);        // Creates an intent for calling the Heart Rate Timer service.

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();      // Gets the bluetooth system on the watch
        if (!bluetooth.isEnabled())     // If the bluetooth is not enabled on the watch
        {
            bluetooth.enable();     // Enable it.
        }

        View.OnClickListener ClickRange = new View.OnClickListener()        // Listens for the button to be clicked
        {
            @SuppressLint("SetTextI18n")
            public void onClick(View v)     // When the button is clicked
            {
                Log.i("Main Activity", "Ranging Button Clicked");     // Logs on Console.

                vibrator.vibrate(HapticFeedback);       // Vibrate for the set amount listed in preferences.
                Switch = !Switch;       // Switch the boolean value associated with

                if (Switch)     // If the switch is true
                {
                    String data =  ("Main Activity Start Range Button Clicked at" + SystemInformation.getTimeStamp());       // This is the format it is logged at.
                    DataLogger datalog = new DataLogger(Subdirectory_Heartrate, Heart_Rate, data);      // Logs it into a file called System Activity.
                    datalog.LogData();      // Saves the data into the directory.

                    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);       // This is an animator that
                    animator.setDuration(2000);     // Keeps the current color fading for the listed time in milliseconds

                    final float[] hsv;      // A list of possible colors
                    final int[] runColor = new int[1];      // A new list of colors parsed through
                    hsv = new float[3];     // A new list of color to got through
                    hsv[1] = 1;     // The first color to choose.
                    hsv[2] = 1;     // The last color to choose

                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()       // Listen for an update from the colors
                    {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation)      // On a change from the color list
                        {
                            hsv[0] = 360 * animation.getAnimatedFraction();     // Get the color to be changed to
                            runColor[0] = Color.HSVToColor(hsv);        // Run the color through the animator
                            HeartRateMonitor.setBackgroundColor(runColor[0]);     // Sets the Heart Rate ranger button the color given
                        }
                    });

                    animator.setRepeatCount(Animation.INFINITE);        // Lets the animator to run through the colors given forever.
                    animator.start();       // Starts the animation

                    HeartRateMonitor.setText("Collecting Data");     // Sets the text on the screen

                    startService(HRService);     // Starts the service
                    startService(AccelService);     // Starts the service
                    startService(GyroService);     // Starts the service
                }

                else
                {
                    String data =  ("Main Activity Stop Range Button Clicked at" + SystemInformation.getTimeStamp());       // This is the format it is logged at.
                    DataLogger datalog = new DataLogger(Subdirectory_Heartrate, Heart_Rate, data);      // Logs it into a file called System Activity.
                    datalog.LogData();      // Saves the data into the directory.

                    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);       // This is an animator that
                    animator.setDuration(2000);     // Keeps the current color fading for the listed time in milliseconds

                    final float[] hsv;      // A list of possible colors
                    final int[] runColor = new int[1];      // A new list of colors parsed through
                    hsv = new float[3];     // A new list of color to got through
                    hsv[1] = 1;     // The first color to choose.
                    hsv[2] = 1;     // The last color to choose

                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()       // Listen for an update from the colors
                    {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation)      // On a change from the color list
                        {
                            hsv[0] = 360 * animation.getAnimatedFraction();     // Get the color to be changed to
                            runColor[0] = Color.HSVToColor(hsv);        // Run the color through the animator
                            HeartRateMonitor.setBackgroundColor(Color.BLACK);     // Sets the Heart Rate ranger button the color given
                        }
                    });

                    animator.setRepeatCount(Animation.INFINITE);        // Lets the animator to run through the colors given forever.
                    animator.start();       // Starts the animation

                    HeartRateMonitor.setText("Click to Start Data Collection");      // Sets the text on the text view.

                    stopService(HRService);      // Stops the service
                    stopService(AccelService);      // Stops the service
                    stopService(GyroService);      // Stops the service
                }
            }
        };

        HeartRateMonitor.setOnClickListener(ClickRange);      // Ties the Heart Rate ranging button the the click view

        setAmbientEnabled();        // Enables Always-on
    }

    public void CheckPermissions()      // Checks the permissions associated with the need for access to hardware on the device.
    {
        Log.i("Main Activity", "Checking Permissions");     // Logs on Console.

        String[] Required_Permissions =     // Checks if Device has permission to work on device.
                {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,     // This is to access the storage
                        Manifest.permission.READ_EXTERNAL_STORAGE,      // This is to access the storage
                        Manifest.permission.BODY_SENSORS,       // This is to access the sensors of the device
                        Manifest.permission.ACCESS_COARSE_LOCATION,     // This is to access the location in a general sense
                        Manifest.permission.ACCESS_FINE_LOCATION,       // This is to access the location in a more specific manner
                        Manifest.permission.BLUETOOTH,      // This is to access th bluetooth
                        Manifest.permission.BLUETOOTH_ADMIN     // This is access the bluetooth and allow changes
                };

        boolean needPermissions = false;        // To begin the permission is set to false.

        for (String permission : Required_Permissions)     // For each of the permission listed above.
        {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)       // Check if they have permission to work on the device.
            {
                needPermissions = true;     // if they do, grant them permission
                CheckFiles();       // Calls the check files module.
            }
        }

        if (needPermissions)        // When they have permission
        {
            ActivityCompat.requestPermissions(this, Required_Permissions,0);     // Allow them to work on device.
        }
    }

    private void CheckFiles()
    {
        Log.i("Main Activity", "Checking Files");     // Logs on Console.

        File HRSensors = new File(Preferences.Directory + SystemInformation.Heart_Rate_Path);     // Gets the path to the Sensors from the system.
        if (!HRSensors.exists())      // If the file exists
        {
            Log.i("Heart Rate Sensor", "Creating Header Main");     // Logs on Console.

            DataLogger dataLogger = new DataLogger(Subdirectory_Heartrate, Heart_Rate, Preferences.Heart_Rate_Data_Headers);        /* Logs the Sensors data in a csv format */
            dataLogger.LogData();       // Saves the data to the directory.
        }
    }

    private boolean isRunningHeartRate()        // A general file that checks if Heart Rate is running.
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);     // Starts the activity manager to check the service called.
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))        // For each service called by the running service.
        {
            if (HeartRateSensor.class.getName().equals(service.service.getClassName()))      // It checks if it is running.
            {
                return true;        // Returns true
            }
        }
        return false;       // If not, it returns false.
    }

    @Override
    public void onResume()      // When the system resumes
    {
        CheckPermissions();     // Calls the check permission method.
        CheckFiles();       // Calls the check files method

        super.onResume();       // Forces the resume.
    }

    @Override
    protected void onStop()     // To stop the activity.
    {
        if (isRunningHeartRate())        // If the Heart Rate service is running.
        {
            HeartRateMonitor.performClick();      // Click the button to end the service.
        }
        super.onStop();     // It stops the activity.
    }
}
