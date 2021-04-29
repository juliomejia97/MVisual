package com.example.pixelmanipulation;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FilesActivity extends AppCompatActivity {

    LinearLayout llPacientes, llSeries, llEstudios;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        llPacientes = findViewById(R.id.llPacientes);
        llEstudios = findViewById(R.id.llEstudios);
        llSeries = findViewById(R.id.llSeries);
        llPacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Layout", "Pressed Patient");
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("Type", "Pacientes");
                startActivity(intent);
            }
        });

        llEstudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Layout", "Pressed Studies");
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("Type", "Estudios");
                startActivity(intent);
            }
        });

        llSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Layout", "Pressed Series");
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("Type", "Series");
                startActivity(intent);
            }
        });
    }
}