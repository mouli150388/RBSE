package com.tutorix.tutorialspoint.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import static android.R.attr.targetSdkVersion;

/**
 * Created by Employee on 5/8/2017.
 */

public class PermissionRequest {

    public static boolean checkPermissionCamera(Context ctx)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {

            if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                    ||ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                    ||ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }else
            {
                return true;
            }
        }else
        {
            return  true;
        }

    }
    public static boolean checkPermissionsAll(Context ctx)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {

            if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }else
            {
                return true;
            }
        }else
        {
            return  true;
        }

    }
    public static boolean checkPermissionsAll(final Activity activity,final String permission)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {

            if(ContextCompat.checkSelfPermission(activity, permission)!=PackageManager.PERMISSION_GRANTED)
            {

                if (activity.shouldShowRequestPermissionRationale(
                        permission)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(activity)
                            .setTitle("Permission required")
                            .setMessage("Permissions are required for this application to work ! ")
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity. requestPermissions(new String[]{permission},201);


                                }

                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            })
                            .show();

                } else {
                    // No explanation needed; request the permission
                    activity.requestPermissions(new String[]{permission},201);

                }
                return false;
            }else
            {
                return true;
            }
        }else
        {
            return  true;
        }

    }
    public static boolean selfPermissionGranted(Activity activity,String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = activity.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(activity, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }
    public static void callActivity(Activity ctx, Class clas)
    {
      Intent  in=new Intent(ctx, clas);
        in.putExtra("isApplive", true);
        ctx.startActivity(in);
    }
}
