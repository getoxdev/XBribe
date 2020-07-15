package com.xbribe.ui.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.File;

public class MyReceiver extends BroadcastReceiver
{
    private static int countPowerOff = 0;

    public MyReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_OFF");
            countPowerOff++;
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_ON");
        }
        else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT))
        {
            Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");
            if (countPowerOff > 2)
            {
                countPowerOff = 0;
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(cameraIntent);
            }
        }
    }

}
