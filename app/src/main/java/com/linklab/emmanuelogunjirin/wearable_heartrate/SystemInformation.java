package com.linklab.emmanuelogunjirin.wearable_heartrate;

// Imports

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ALL")
class SystemInformation     // Class that acquires the current time from the system and saves it.
{
    private Preferences Preference = new Preferences();     // Gets an instance from the preferences module.

    private String DeviceID = Preference.DeviceID;       // Gets the Device ID from preferences
    private String HeartRate = Preference.Heart_Rate;       // Gets the HeartRate file from preferences

    /* File path for Adding Headers to Individual File Name */
    public String Heart_Rate_Path = Preference.Subdirectory_HeartRate + "/" + DeviceID + "_" + HeartRate;      // This is the HeartRate File path

    /* Subdirectories to be made by the system */
    List <String> Subdirectories = new ArrayList<>(Arrays.asList        // Creates a list of the subdirectories to be created.
            (
                    Preference.Subdirectory_HeartRate      // This is where the HeartRate is kept
            )
    );

    String getTime()        // This gets only the current time from the system
    {
        DateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);      // The time format is called in US format.
        Date current = new Date();      // The current date and timer is set.
        return timeFormat.format(current);       // The current time is set to show on the time text view.
    }

    String getTimeStamp()        // Puts the system time acquired into the desired format wanted.
    {
        DateFormat datetimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS", Locale.US);      // Specified format of the time, in US style.
        Date current = new Date();      // Calls the current date from the system.
        return datetimeFormat.format(current);  // Returns the date and time the system is in.
    }

    String getTimeMilitary()        // Gets the current time in military format
    {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);      // The time format military wise is called in US format.
        Date current = new Date();      // The current date and timer is set.
        return timeFormat.format(current);       // The current time in military format is returned
    }

    String getDate()        // This gets only the current date from the system
    {
        Date current = new Date();      // The current date and timer is set.
        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);     // The date is called in US format.
        return dateFormat.format(current);       // The current date is set to show on the date text view.
    }

    String getDateStamp()       // Gets the data stamp from the system
    {
        Date current = new Date();      // The current date and timer is set.
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd", Locale.US);     // The date is called in US format.
        return dateFormat.format(current);       // The current date is set to show on the date text view.
    }
}
