package com.example.pixelmanipulation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.providers.FirebaseProvider;
import com.example.pixelmanipulation.adapters.ListViewAdapter;

public class CategoryListActivity extends AppCompatActivity {

    private FirebaseProvider provider;
    private ListViewAdapter mListInfoAdapter;
    private ImageView ivCenter, ivPrevious;
    private ListView mlista;
    private TextView tvTypeName;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        provider = FirebaseProvider.getInstance();
        ivCenter = findViewById(R.id.center);
        ivPrevious = findViewById(R.id.ivPreviousCategory);
        tvTypeName = findViewById(R.id.tvTypeName);
        mlista = findViewById(R.id.lvData);

        ivPrevious.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView(getIntent().getStringExtra("Type"));
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();
    }

    public void initView(String type) {

        int level = 1;

        if(type.equalsIgnoreCase("pacientes")){

            tvTypeName.setText("Pacientes");
            ivCenter.setImageDrawable(getResources().getDrawable(R.drawable.patients_folder));
            mListInfoAdapter = new ListViewAdapter(CategoryListActivity.this, provider.getAllPatients(), level);
            mlista.setAdapter(mListInfoAdapter);

        } else if (type.equalsIgnoreCase("estudios")){

            tvTypeName.setText("Estudios");
            ivCenter.setImageDrawable(getResources().getDrawable(R.drawable.estudies_joume));
            mListInfoAdapter = new ListViewAdapter(CategoryListActivity.this, provider.getAllStudies(), level);
            mlista.setAdapter(mListInfoAdapter);

        } else if (type.equalsIgnoreCase("series")){

            tvTypeName.setText("Series");
            ivCenter.setImageDrawable(getResources().getDrawable(R.drawable.heart_folder));
            mListInfoAdapter = new ListViewAdapter(CategoryListActivity.this, provider.getAllSeries(), level);
            mlista.setAdapter(mListInfoAdapter);

        } else if (type.equalsIgnoreCase("procesadas")){

            tvTypeName.setText("Imágenes Procesadas");
            ivCenter.setImageDrawable(getResources().getDrawable(R.drawable.process_folder));
            mListInfoAdapter = new ListViewAdapter(CategoryListActivity.this, provider.getAllProcessed(), level);
            mlista.setAdapter(mListInfoAdapter);

        }
    }
}