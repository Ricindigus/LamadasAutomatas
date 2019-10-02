package com.example.lamadasautomatas;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class FinalizeWorker extends Worker {

    Context context;

    public FinalizeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        ITelephony telephonyService;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method m = tm.getClass().getDeclaredMethod("getITelephony");

            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(tm);

            telephonyService.endCall();
            Toast.makeText(context, "Ending the call", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}
