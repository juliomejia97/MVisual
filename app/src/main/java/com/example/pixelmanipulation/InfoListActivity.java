package com.example.pixelmanipulation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.datastore.generated.model.AmplifyModelProvider;
import com.amplifyframework.datastore.generated.model.PatientsProvider;
import com.example.pixelmanipulation.adapters.ListViewAdapter;
import com.example.pixelmanipulation.model.DataViewHolder;

public class InfoListActivity extends AppCompatActivity {

    private PatientsProvider provider;
    private ListViewAdapter mListInfoAdapter;
    private ImageView ivPrevious, ivInfo;
    private TextView tvInfoPrincipal, tvInfoSecond, tvInfoThird;
    private ListView mlista;
    private DataViewHolder info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
        AmplifyModelProvider.getInstance(getApplicationContext());
        provider = PatientsProvider.getInstance();

        ivPrevious = findViewById(R.id.ivPrevious);
        ivInfo = findViewById(R.id.ivInfo);
        tvInfoPrincipal = findViewById(R.id.tvInfoPrincipal);
        tvInfoSecond = findViewById(R.id.tvInfoSecond);
        tvInfoThird = findViewById(R.id.tvInfoThird);
        mlista = findViewById(R.id.lvInfo);

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
        String type = getIntent().getStringExtra("Type");
        String id = getIntent().getStringExtra("Id");
        if(type.equalsIgnoreCase("Pacientes")){
            info = provider.getPatientById(id);
        } else if(type.equalsIgnoreCase("Estudios")){
            info = provider.getStudyById(id);
        } else if(type.equalsIgnoreCase("Series")){
            info = provider.getSeriesById(id);
        }

        initData();

    }

    public void initData(){

        /*
        TODO: LA IMAGEN DE LA ACTIVIDAD DEBE SER LA ASOCIADA AL NOMBRE DESDE AMPLIFY
         */
        tvInfoPrincipal.setText(info.getInfo());

        if(info.getType().equalsIgnoreCase("Pacientes")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.avatar5));
            mListInfoAdapter = new ListViewAdapter(InfoListActivity.this, provider.getStudiesByPatient(info.getId()));
            mlista.setAdapter(mListInfoAdapter);
        } else if (info.getType().equalsIgnoreCase("Estudios")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.folder));
            tvInfoSecond.setText(provider.getStudyPatient(info.getId()).getInfo());
            mListInfoAdapter = new ListViewAdapter(InfoListActivity.this, provider.getSeriesByStudy(info.getId()));
            mlista.setAdapter(mListInfoAdapter);
        } else if (info.getType().equalsIgnoreCase("Series")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.scan));
            DataViewHolder study = provider.getSeriesStudy(info.getId());
            DataViewHolder patient = provider.getStudyPatient(study.getId());
            tvInfoSecond.setText(study.getInfo());
            tvInfoThird.setText(patient.getInfo());
            /*
            TODO: YA SE MOSTRARIAN LAS IMAGENES GUARDADAS PERTENECIENTES A DICHA SERIE
             */
        }
    }
}