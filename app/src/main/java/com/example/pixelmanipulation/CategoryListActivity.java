package com.example.pixelmanipulation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.providers.FirebaseProvider;
import com.example.pixelmanipulation.adapters.ListViewAdapter;

public class CategoryListActivity extends AppCompatActivity {

    private FirebaseProvider provider;
    private ListViewAdapter mListInfoAdapter;
    private ImageView ivCenter, ivPrevious;
    private Spinner spinner;
    private ListView mlista;
    private TextView tvTypeName;
    private String[] arrayForSpinner = {"Ordenar por", "Nombre", "Apellido"};

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        provider = FirebaseProvider.getInstance();
        ivCenter = findViewById(R.id.center);
        ivPrevious = findViewById(R.id.ivPreviousCategory);
        tvTypeName = findViewById(R.id.tvTypeName);
        mlista = findViewById(R.id.lvData);
        spinner = findViewById(R.id.spinnerList);

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FilesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoryListActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayForSpinner);
        spinner.setAdapter(adapter);
        initView(getIntent().getStringExtra("Type"));
    }

    public void initView(String type) {

        int level = 1;

        if(type.equalsIgnoreCase("pacientes")){

            tvTypeName.setText("Por Paciente");
            ivCenter.setImageDrawable(getResources().getDrawable(R.drawable.patient));
            mListInfoAdapter = new ListViewAdapter(CategoryListActivity.this, provider.getAllPatients(), level);
            mlista.setAdapter(mListInfoAdapter);

        } else if (type.equalsIgnoreCase("estudios")){

            tvTypeName.setText("Por Estudios");
            ivCenter.setImageDrawable(getResources().getDrawable(R.drawable.studies));
            mListInfoAdapter = new ListViewAdapter(CategoryListActivity.this, provider.getAllStudies(), level);
            mlista.setAdapter(mListInfoAdapter);

        } else if (type.equalsIgnoreCase("series")){

            tvTypeName.setText("Por Series");
            ivCenter.setImageDrawable(getResources().getDrawable(R.drawable.series));
            mListInfoAdapter = new ListViewAdapter(CategoryListActivity.this, provider.getAllSeries(), level);
            mlista.setAdapter(mListInfoAdapter);
        }
    }
}