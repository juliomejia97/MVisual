package com.example.pixelmanipulation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.datastore.generated.model.AmplifyModelProvider;
import com.example.pixelmanipulation.adapters.ListViewAdapter;

public class InfoListActivity extends AppCompatActivity {

    private ListViewAdapter mListInfoAdapter;
    private ImageView ivPrevious, ivInfo;
    private TextView tvInfoName;
    private ListView mlista;
    private String type, infoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
        AmplifyModelProvider.getInstance(getApplicationContext());

        type = getIntent().getStringExtra("Type");
        infoName = getIntent().getStringExtra("Info");

        ivPrevious = findViewById(R.id.ivPrevious);
        ivInfo = findViewById(R.id.ivInfo);
        tvInfoName = findViewById(R.id.tvInfoName);
        mlista = findViewById(R.id.lvData);

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.putExtra("Type", type);
                startActivity(intent);
                finish();
            }
        });

        initData();

    }

    public void initData(){

        /*
        TODO: LA IMAGEN DE LA ACTIVIDAD DEBE SER LA ASOCIADA AL NOMBRE DESDE AMPLIFY
         */
        tvInfoName.setText(infoName);

        if(type.equalsIgnoreCase("Pacientes")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.avatar5));
        } else if (type.equalsIgnoreCase("Estudios")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.folder));
        } else if (type.equalsIgnoreCase("Series")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.scan));
        }
    }
}