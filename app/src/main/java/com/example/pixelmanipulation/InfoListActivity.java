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
    private DataViewHolder info, infoSecondary, infoThird;
    private int level;

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
                if (level == 1){
                    Intent intent = new Intent(InfoListActivity.this, CategoryListActivity.class);
                    intent.putExtra("Type", info.getType());
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(InfoListActivity.this, InfoListActivity.class);
                    intent.putExtra("Type", infoSecondary.getType());
                    intent.putExtra("Id", infoSecondary.getId());
                    intent.putExtra("Level", (level - 1));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String type = getIntent().getStringExtra("Type");
        String id = getIntent().getStringExtra("Id");
        level = getIntent().getIntExtra("Level", -1);
        if(type.equalsIgnoreCase("Pacientes")){
            info = provider.getPatientById(id);
            infoSecondary = null;
            infoThird = null;
        } else if(type.equalsIgnoreCase("Estudios")){
            info = provider.getStudyById(id);
            infoSecondary = provider.getStudyPatient(info.getId());
        } else if(type.equalsIgnoreCase("Series")){
            info = provider.getSeriesById(id);
            infoSecondary = provider.getSeriesStudy(info.getId());
            infoThird = provider.getStudyPatient(infoSecondary.getId());
        }

        initData();

    }

    public void initData(){

        /*
        TODO: LA IMAGEN DE LA ACTIVIDAD DEBE SER LA ASOCIADA AL NOMBRE DESDE AMPLIFY
         */
        /*
        TODO: YA SE MOSTRARIAN LAS IMAGENES GUARDADAS PERTENECIENTES A DICHA SERIE
        */
        tvInfoPrincipal.setText(info.getInfo());

        if(info.getType().equalsIgnoreCase("Pacientes")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.avatar5));
            mListInfoAdapter = new ListViewAdapter(InfoListActivity.this, provider.getStudiesByPatient(info.getId()), (level + 1));
            mlista.setAdapter(mListInfoAdapter);
        } else if (info.getType().equalsIgnoreCase("Estudios")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.folder));
            tvInfoSecond.setText(infoSecondary.getInfo());
            mListInfoAdapter = new ListViewAdapter(InfoListActivity.this, provider.getSeriesByStudy(info.getId()), (level + 1));
            mlista.setAdapter(mListInfoAdapter);
        } else if (info.getType().equalsIgnoreCase("Series")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.scan));
            tvInfoSecond.setText(infoSecondary.getInfo());
            tvInfoThird.setText(infoThird.getInfo());
            mListInfoAdapter = new ListViewAdapter(InfoListActivity.this, provider.getImagesBySeries(info.getId()), (level + 1));
            mlista.setAdapter(mListInfoAdapter);
        }
    }
}