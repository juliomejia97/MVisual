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
        byte[] preview = getIntent().getByteArrayExtra("BufferPreview");
        byte[] initial = getIntent().getByteArrayExtra("BufferInitial");
        byte[] edited = getIntent().getByteArrayExtra("BufferEdited");
        String imageId = getIntent().getStringExtra("imageId");
        int W = getIntent().getIntExtra("W", 0);
        int H = getIntent().getIntExtra("H", 0);
        initData(preview, initial, edited, imageId, W, H);
    }

    private void initData(byte[] previewBuffer, byte[] initialBuffer, byte[] editedBuffer, String imageId, int W, int H){
        Bitmap editedImage = BitmapFactory.decodeByteArray(previewBuffer, 0, previewBuffer.length);
        imageView.setImageBitmap(editedImage);
        mAdapter = new AlgorithmListAdapter(ProcessingMethodActivity.this, provider.sendGETRequestCpPlugins(), imageId, initialBuffer, editedBuffer, W, H);
        mLista.setAdapter(mAdapter);
    }
}