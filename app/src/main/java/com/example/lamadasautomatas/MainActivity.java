package com.example.lamadasautomatas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
                requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
            }
        }

        Constraints constraints = new Constraints.Builder()
                .build();

        Data.Builder data1 = new Data.Builder();
        data1.putString("number","994595937");

        Data.Builder data2 = new Data.Builder();
        data2.putString("number","999999222");

        OneTimeWorkRequest compressionWork1 =
                new OneTimeWorkRequest.Builder(CallWorker.class)
                        .setConstraints(constraints)
                        .setInputData(data1.build())
                        .build();

        WorkManager.getInstance(this)
                .enqueue(compressionWork1);

        OneTimeWorkRequest compressionWork2 =
                new OneTimeWorkRequest.Builder(CallWorker.class)
                        .setConstraints(constraints)
                        .setInputData(data2.build())
                        .setInitialDelay(15, TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this)
                .enqueue(compressionWork2);

        OneTimeWorkRequest finalizeWork1 =
                new OneTimeWorkRequest.Builder(FinalizeWorker.class)
                        .setConstraints(constraints)
                        .setInitialDelay(30,TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this)
                .enqueue(finalizeWork1);

        OneTimeWorkRequest finalizeWork2 =
                new OneTimeWorkRequest.Builder(FinalizeWorker.class)
                        .setConstraints(constraints)
                        .setInitialDelay(60,TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this)
                .enqueue(finalizeWork2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted: " +
                            PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission NOT granted: " +
                            PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
                }

                return;
            }
        }
    }
}
