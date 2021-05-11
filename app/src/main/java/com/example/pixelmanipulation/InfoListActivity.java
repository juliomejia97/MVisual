package com.example.pixelmanipulation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.providers.FirebaseProvider;
import com.example.pixelmanipulation.adapters.ListViewAdapter;
import com.example.pixelmanipulation.model.DataViewHolder;

import java.io.File;
import java.io.IOException;

public class InfoListActivity extends AppCompatActivity {

    private FirebaseProvider provider;
    private ListViewAdapter mListInfoAdapter;
    private ImageView ivPrevious, ivInfo;
    private TextView tvInfoPrincipal, tvInfoSecond, tvInfoThird;
    private ListView mlista;
    private DataViewHolder info, infoSecondary, infoThird;
    private StorageReference mStorage;
    private int level;
    private String idCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
        provider = FirebaseProvider.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();

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
        idCurrent = getIntent().getStringExtra("Id");
        level = getIntent().getIntExtra("Level", -1);
        if(type.equalsIgnoreCase("pacientes")){
            info = provider.getPatientById(idCurrent);
            infoSecondary = null;
            infoThird = null;
        } else if(type.equalsIgnoreCase("estudios")){
            info = provider.getStudyById(idCurrent);
            infoSecondary = provider.getStudyPatient(info.getId());
        } else if(type.equalsIgnoreCase("series")){
            info = provider.getSeriesById(idCurrent);
            infoSecondary = provider.getSeriesStudy(info.getId());
            infoThird = provider.getStudyPatient(infoSecondary.getId());
        }

        initData();

    }

    public void initData(){

        tvInfoPrincipal.setText(info.getInfo());

        if(info.getType().equalsIgnoreCase("pacientes")){
            try {
                downloadProfileImage(idCurrent, ivInfo);
                mListInfoAdapter = new ListViewAdapter(InfoListActivity.this, provider.getStudiesByPatient(info.getId()), (level + 1));
                mlista.setAdapter(mListInfoAdapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (info.getType().equalsIgnoreCase("estudios")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.folder));
            tvInfoSecond.setText(infoSecondary.getInfo());
            mListInfoAdapter = new ListViewAdapter(InfoListActivity.this, provider.getSeriesByStudy(info.getId()), (level + 1));
            mlista.setAdapter(mListInfoAdapter);
        } else if (info.getType().equalsIgnoreCase("series")){
            ivInfo.setImageDrawable(getResources().getDrawable(R.drawable.scan));
            tvInfoSecond.setText(infoSecondary.getInfo());
            tvInfoThird.setText(infoThird.getInfo());
            mListInfoAdapter = new ListViewAdapter(InfoListActivity.this, provider.getImagesBySeries(info.getId()), (level + 1));
            mlista.setAdapter(mListInfoAdapter);
        }
    }

    private void downloadProfileImage(String idPatient, final ImageView photo) throws IOException {
        final File localFile = File.createTempFile("images", "png");
        StorageReference imageRef = mStorage.child("Profile/" + idPatient + "/Profile.png");
        imageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        photo.setImageURI(Uri.fromFile(localFile));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }
}