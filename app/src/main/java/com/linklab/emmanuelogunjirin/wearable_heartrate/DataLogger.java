package com.linklab.emmanuelogunjirin.wearable_heartrate;

// Imports
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

// src: https://developer.android.com/training/data-storage/files.html#WriteExternalStorage
@SuppressWarnings("ALL")    // Service wide suppression for the data logger names.
public class DataLogger     // A function that runs the data logging data
{
    private String FileName, Content, Subdirectory;        // Variable names for the file characters and contents.
    private Preferences Preference = new Preferences();     // Gets an instance from the preferences module.
    private String Directory = Preference.Directory;     // Gets the directory from the preferences class.

    public DataLogger(String subdirectory, String filename ,String content)      // This just includes all the variable for the data logger function
    {
        FileName = Preference.DeviceID + "_" + filename;        // Initiates a variable for the filename from preferences
        Content = content;      // Initiates a variable for the content of the file name
        Subdirectory = subdirectory + "/";        // Saves the file into this subdirectory
    }

    private boolean isExternalStorageWritable()     /* Checks if external storage is available for read and write */
    {
        String state = android.os.Environment.getExternalStorageState();        // Checks if the sdcard can be written to.
        return android.os.Environment.MEDIA_MOUNTED.equals(state);      // Returns the state of the sdcard.
    }

    public boolean isExternalStorageReadable()    /* Checks if external storage is available to at least read */
    {
        String state = android.os.Environment.getExternalStorageState();        // Checks if the sdcard can be read from
        return android.os.Environment.MEDIA_MOUNTED.equals(state) || android.os.Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);      // Returns the state of the sdcard.
    }

    public void LogData()       // This is what is run when logdata is called.
    {
        if (isExternalStorageWritable())        // Checks if the storage is writable
        {
            try     // Tries to do this.
            {
                File BESI_directory = new File(Directory);    // Path to file in the storage of the device

                if (!BESI_directory.isDirectory())    // If there is no directory with that name
                {
                    Log.i("Data Logger", "Making a directory called " + BESI_directory);     // Logs on Console.
                    BESI_directory.mkdirs();        // Make a directory with the name.
                }

                File myFile = new File(Directory+Subdirectory+FileName);     // Adds the filename to the path of the file
                myFile.createNewFile();     // Cretates the new file
                FileOutputStream fileOut = new FileOutputStream(myFile,true);       // This is what the file outputs.
                OutputStreamWriter myOutWriter =new OutputStreamWriter(fileOut);        // Enters the new line in the file
                myOutWriter.append(Content+"\n");       // Appends the content to the file
                myOutWriter.close();        // Closes the file
                fileOut.close();        // Closes the directory.
            }
            catch (IOException e)       // If it does not write the file, imform us it failed.
            {
                Log.i("Data Logger", "Failed to make Directory");     // Logs on Console.
            }
            catch (Exception ex)        // Else it does this
            {
                Log.i("Data Logger", "Failed to make Directory");     // Logs on Console.
            }
        }

        else        // If we canot make the directory
        {
            Log.i("Data Logger", "Failed to make Directory");     // Logs on Console.
        }
    }

    public void WriteData()     // This writes the data to the sdcard.
    {
        if (isExternalStorageWritable())        // Checks if we can write data to the card.
        {
            try     // Tries to do this
            {
                File BESI_directory = new File(Directory);    // Path to file in the storage of the device

                if (!BESI_directory.isDirectory())        // If there is no directory with the name
                {
                    Log.i("Data Logger", "Making a directory called " + BESI_directory);     // Logs on Console.
                    BESI_directory.mkdirs();        // Do nothing.
                }

                File myFile = new File(Directory+Subdirectory+FileName);     // Adds the filename to the path of the file
                myFile.createNewFile();     // Cretates the new file
                FileOutputStream fileOut = new FileOutputStream(myFile,false);       // This is what the file outputs.
                OutputStreamWriter myOutWriter =new OutputStreamWriter(fileOut);        // Enters the new line in the file
                myOutWriter.write(Content);       // Appends the content to the file
                myOutWriter.close();        // Closes the file
                fileOut.close();        // Closes the directory.
            }
            catch (IOException e)       // If it does not write the file, imform us it failed.
            {
                Log.i("Data Logger", "Failed to make Directory");     // Logs on Console.
            }
            catch (Exception ex)
            {
                Log.i("Data Logger", "Failed to make Directory");     // Logs on Console.
            }
        }

        else        // If we canot make the directory
        {
            Log.i("Data Logger", "Failed to make Directory");     // Logs on Console.
        }
    }

    public String ReadData()    // This reads the data from the sdcard
    {
        StringBuilder text = new StringBuilder();       // This is the new string that is built
        try     // Tries to run the following.
        {
            File file = new File(Directory+Subdirectory+FileName);       // Creates a filename with the new filename
            BufferedReader bufferedReaderr = new BufferedReader(new FileReader(file));      // Reads the buffer in the system
            String line;        // Creates a new line.

            while ((line = bufferedReaderr.readLine()) != null)     // While the line is not blank
            {
                text.append(line);      // Append the text to the line
                text.append('\n');      // Start a new line.
            }
            bufferedReaderr.close() ;       // Close the buffer reader.
        }
        catch (IOException e)   // Catch statement
        {
            e.printStackTrace();        // Ignore this.
        }
        return text.toString();     // Return the text to the string.
    }
}
