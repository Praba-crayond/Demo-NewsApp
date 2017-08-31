package com.acceltest.newsapi;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by croyon11 on 8/30/2017.
 */

public class Apputil {

    /* checking the intrenet connection */
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
                /*Network connecting...*/
            return true;
        } else {
                /*oops!!! no network*/
            //   No_network_connection(context);
            Log.d("TAG", "Internet Connection Not Present");
            return false;
        }
    }

    /* Go to setting page -> weather their is no internet connection */
    public static void No_network_connection(final Context context) {
        android.support.v7.app.AlertDialog.Builder alert_build = new android.support.v7.app.AlertDialog.Builder(context);
        alert_build.setTitle("Network Information");
        alert_build.setMessage("Please check your Internet connection");
        alert_build.setCancelable(false);
        alert_build.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            /* Go to Settings page Intent */
                Intent i = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(i);
                dialog.dismiss();

            }
        });
        alert_build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alert_show = alert_build.create();
        alert_show.show();
    }

    public  static void animation(RecyclerView.ViewHolder holder, boolean value)
    {
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator translatey= ObjectAnimator.ofFloat(holder.itemView,"translationY",value==true ? 200:-200,0);
        translatey.setDuration(1000);
        animatorSet.playTogether(translatey);
        animatorSet.start();
    }


    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static String ConvertDateandTimeFromServer(String str) {

        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
        Date date = null;
        try {
            date = readDate.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat writeDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String date_time = writeDate.format(date);
        Log.e("date_time", "date_time : " + date_time);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String date_str = dateFormat.format(date);
        Log.e("date_str", "date_str : " + date_str);

        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String time_str = timeFormat.format(date);
        Log.e("time_str", "time_str : " + time_str);

        String dat=date_str+" "+time_str;

        return dat;
    }


}
