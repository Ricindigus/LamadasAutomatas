package com.example.lamadasautomatas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText edt1, edt2, edtDuracion, edtHora, edtMinuto,edtSegundo;
    private Button btn1, btn2;
    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;

    private String numberSaliente, numberEntrante;
    private int hora,minuto,segundo;
    private int duracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edtHora = findViewById(R.id.edtHora);
        edtMinuto = findViewById(R.id.edtMinuto);
        edtSegundo = findViewById(R.id.edtSegundo);
        edtDuracion = findViewById(R.id.edtDuracion);
        btn1 = findViewById(R.id.btnSaliente);
        btn2 = findViewById(R.id.btnEntrante);




        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ANSWER_PHONE_CALLS};
                requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
            }
        }

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

    public void configurarEntrante(View view) {
        numberEntrante = edt2.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("llamadas_automatas_preference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("number_entrante", numberEntrante);
        editor.commit();
    }

    public void programaSaliente(View view) {

        numberSaliente = edt1.getText().toString();
        duracion = Integer.parseInt(edtDuracion.getText().toString());
        Constraints constraints = new Constraints.Builder()
                .build();

        Data.Builder data1 = new Data.Builder();
        data1.putString("number",numberSaliente);

        Calendar calendario = Calendar.getInstance();
        int h =calendario.get(Calendar.HOUR_OF_DAY);
        int m = calendario.get(Calendar.MINUTE);
        int s = calendario.get(Calendar.SECOND);

        hora = Integer.parseInt(edtHora.getText().toString());
        minuto = Integer.parseInt(edtMinuto.getText().toString());
        segundo = Integer.parseInt(edtSegundo.getText().toString());


        long totalSegundosActuales = h * 3600 + m * 60 + s;
        long totalSegundosFuturos = hora * 3600 + minuto * 60 + segundo;

        long resultadoSegundosWait = totalSegundosFuturos-totalSegundosActuales;




        OneTimeWorkRequest compressionWork1 =
                new OneTimeWorkRequest.Builder(CallWorker.class)
                        .setConstraints(constraints)
                        .setInputData(data1.build())
                        .setInitialDelay(resultadoSegundosWait,TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this)
                .enqueue(compressionWork1);

        OneTimeWorkRequest finalizeWork1 =
                new OneTimeWorkRequest.Builder(FinalizeWorker.class)
                        .setConstraints(constraints)
                        .setInitialDelay(duracion*60 + 15,TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this)
                .enqueue(finalizeWork1);

    }




}
