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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessingMethodActivity extends AppCompatActivity {

    private ImageView imageView;
    private ListView mLista;
    private AlgorithmListAdapter mAdapter;
    private CpPluginsProvider provider;
    private JSONObject json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_method);
        provider = CpPluginsProvider.getInstance();
        imageView = findViewById(R.id.ivMethod);
        mLista = findViewById(R.id.lvMethod);
        json = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        byte[] preview = getIntent().getByteArrayExtra("BufferPreview");

        try {
            if(json == null){
                json = new JSONObject(read("storage.json"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageId = getIntent().getStringExtra("imageId");
        initData(preview, imageId, json);
    }

    private void initData(byte[] previewBuffer, String imageId, JSONObject jsonObject){
        Bitmap editedImage = BitmapFactory.decodeByteArray(previewBuffer, 0, previewBuffer.length);
        imageView.setImageBitmap(editedImage);
        mAdapter = new AlgorithmListAdapter(ProcessingMethodActivity.this, provider.sendGETRequestCpPlugins(), imageId, jsonObject);
        mLista.setAdapter(mAdapter);
    }

    private String read(String fileName){
        try {
            File jsonFile = new File(getFilesDir() + "/" + fileName);
            FileInputStream fis = this.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            jsonFile.delete();
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }
}