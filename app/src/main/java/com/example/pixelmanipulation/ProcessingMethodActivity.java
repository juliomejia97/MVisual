package com.example.pixelmanipulation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pixelmanipulation.adapters.AlgorithmListAdapter;
import com.providers.CpPluginsProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class ProcessingMethodActivity extends AppCompatActivity {

    private ImageView imageView;
    private ListView mLista;
    private AlgorithmListAdapter mAdapter;
    private CpPluginsProvider provider;
    private JSONObject json;
    private ArrayList<String> algorithms;
    private final String url = "http://150.136.161.199:5000/api/v1.0/pipeline";

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
                json = new JSONObject(Objects.requireNonNull(read("storage.json")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageId = getIntent().getStringExtra("imageId");
        initAlgorithms(imageId, json);
        initData(preview, imageId, json);
    }

    private void initData(byte[] previewBuffer, String imageId, JSONObject jsonObject){
        Bitmap editedImage = BitmapFactory.decodeByteArray(previewBuffer, 0, previewBuffer.length);
        imageView.setImageBitmap(editedImage);
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
        } catch (IOException fileNotFound) {
            return null;
        }
    }

    public void initAlgorithms(String imageId, JSONObject jsonObject){
        algorithms = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("CpPlugins GET OK", response.toString());
                try {
                    for(int i = 0; i < response.length(); i++){
                        Log.i("JSON", response.getString(i));
                        algorithms.add(response.getString(i));
                    }
                    mAdapter = new AlgorithmListAdapter(ProcessingMethodActivity.this, algorithms, imageId, jsonObject);
                    mLista.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("CpPlugins GET Error", error.toString());
            }
        });

        queue.add(request);
        Log.i("JSON", "" + algorithms.size());
    }
}