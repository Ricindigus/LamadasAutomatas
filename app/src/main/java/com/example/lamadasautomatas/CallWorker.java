package com.example.lamadasautomatas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CallWorker extends Worker {

    public CallWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Intent i1 = new Intent(Intent.ACTION_CALL);
        String number = getInputData().getString("number");
        i1.setData(Uri.parse("tel:"+number));
        getApplicationContext().startActivity(i1);
        return Result.success();
    }
}
