package com.example.student.tutor;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = (WebView) this.findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setAppCacheEnabled(true);

        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // load online by default

        if (!isNetworkAvailable()) { // loading offline
            myWebView.setVisibility(View.INVISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_No_Internet)
                    .setPositiveButton(R.string.open_settings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder.show();
        }

        //Loading page from server
        myWebView.loadUrl("http://ec2-54-245-72-223.us-west-2.compute.amazonaws.com:3000");


        LocationManager locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                System.out.println("LOC CHANGED");

                System.out.println("Lan : " + location.getLatitude());
                System.out.println("LON : " + location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("No Permissions.!!");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        //Sending SMS
        double longitude = 0;
        double latitude = 0;
        if(locationManager != null) {

            System.out.println("LOCATION MANAGER NOT NULL");
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                System.out.println("LOCATION IS NOT NULL");

                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Context context = getApplicationContext();
                CharSequence text = "Long : " + longitude + " Lati : " + latitude;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                latitude = 37.484515;
                longitude = -122.147799;
            }
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("6692949501", null,"Latitude:" + latitude + " Longitude:" + longitude , null, null);

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        List<String> locationList = new ArrayList<String>();

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";

                msgData += " " + cursor.getString(13);
                if(msgData.contains("Longitude") && msgData.contains("Latitude")) {
                    locationList.add(msgData);

                }
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        Intent intent = new Intent(MainActivity.this, Map.class);
        intent.putExtra("locationList", locationList.toString());
        startActivity(intent);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
