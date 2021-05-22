package com.example.pixelmanipulation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.pixelmanipulation.adapters.AlgorithmListAdapter;
import com.example.pixelmanipulation.adapters.ListViewAdapter;
import com.providers.CpPluginsProvider;

public class ProcessingMethodActivity extends AppCompatActivity {

    private ImageView imageView;
    private ListView mLista;
    private AlgorithmListAdapter mAdapter;
    private CpPluginsProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_method);
        provider = CpPluginsProvider.getInstance();
        imageView = findViewById(R.id.ivMethod);
        mLista = findViewById(R.id.lvMethod);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData(getIntent().getByteArrayExtra("BufferPreview"), getIntent().getByteArrayExtra("BufferInitial"), getIntent().getByteArrayExtra("BufferEdited"), getIntent().getStringExtra("imageId"));
    }

    private void initData(byte[] previewBuffer, byte[] initialBuffer, byte[] editedBuffer, String imageId){
        Bitmap editedImage = BitmapFactory.decodeByteArray(previewBuffer, 0, previewBuffer.length);
        imageView.setImageBitmap(editedImage);
        mAdapter = new AlgorithmListAdapter(ProcessingMethodActivity.this, provider.sendGETRequestCpPlugins(), imageId, initialBuffer, editedBuffer);
        mLista.setAdapter(mAdapter);
    }
}