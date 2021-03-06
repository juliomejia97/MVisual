package com.providers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pixelmanipulation.ProcessedImageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CpPluginsProvider {

    private static CpPluginsProvider cpPluginsProvider;
    private final String url = "http://150.136.161.199:5000/api/v1.0/pipeline";

    private CpPluginsProvider(){ }

    public static CpPluginsProvider getInstance() {
        if (cpPluginsProvider == null) {
            cpPluginsProvider = new CpPluginsProvider();
        }
        return cpPluginsProvider;
    }

    public JSONObject createJSON(int H, int W, byte[] initialBuffer, byte[] editedBuffer, String algorithm){

        try {
            JSONObject restJSON = new JSONObject();

            //Packages JSON
            //restJSON.put("packages", "itk");

            //Description JSON
            restJSON.put("filter_name", algorithm);

            //Parameters JSON
            JSONArray paramArray = new JSONArray();
            JSONObject param1 = new JSONObject();
            JSONObject param2 = new JSONObject();
            JSONObject param3 = new JSONObject();
            param1.put("name", "NumberOfHistogramBins");
            param1.put("value", "255");
            param2.put("name", "ReturnBinMidPoint");
            param2.put("value", "True");
            param3.put("name", "NumberOfThresholds");
            param3.put("value", "3");
            paramArray.put(param1);
            paramArray.put(param2);
            paramArray.put(param3);

            restJSON.put("parameters", paramArray);

            //Inputs JSON
            JSONArray inputsArray = new JSONArray();
            JSONObject input1 = new JSONObject();
            JSONObject input2 = new JSONObject();
            input1.put("name", "Input");
            input1.put("data_format", "scalar");
            input1.put("data_type", "short");
            input1.put("dimensions",  W + "," + H);
            input1.put("origin", "0,0");
            input1.put("spacing", "1,1");
            input1.put("direction", "1,0,0,1");
            input1.put("raw_buffer", Base64.encodeToString(initialBuffer, Base64.DEFAULT));

            /*input2.put("name", "Mask");
            input2.put("data_format", "rgba");
            input2.put("data_type", "uchar");
            input2.put("dimensions", W + "," + H);
            input2.put("origin", "0,0");
            input2.put("spacing", "1,1");
            input2.put("direction", "1,0,0,1");
            input2.put("raw_buffer", Base64.encodeToString(editedBuffer, Base64.DEFAULT));*/

            inputsArray.put(input1);
            //inputsArray.put(input2);


            restJSON.put("inputs", inputsArray);
            Log.i("JSON", restJSON.toString());
            return restJSON;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> sendGETRequestCpPlugins(Context context) {

        ArrayList<String> algorithms = new ArrayList<String>();
        /*algorithms.add("AbortCheckEvent");
        algorithms.add("AbortEvent");
        algorithms.add("AbsImageFilter");
        algorithms.add("AmoebaOptimizer");
        algorithms.add("DelaunayConformingQuadEdgeMeshFilter");
        algorithms.add("EdgePotentialImageFilter");
        algorithms.add("GaussianDerivativeOperatorEnums");
        algorithms.add("GrayscaleErodeImageFilter");
        algorithms.add("ITKIOMeshFreeSurfer");
        algorithms.add("MaskNegatedImageFilter");
        algorithms.add("OtsuMultipleThresholdsImageFilter");
        algorithms.add("ParticleSwarmOptimizer");
        algorithms.add("PowellOptimizer");*/

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("CpPlugins GET OK", response.toString());
                try {
                    for(int i = 0; i < response.length(); i++){
                        algorithms.add(response.getString(i));
                    }
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

        return algorithms;
    }

    public void sendPOSTRequestCpPlugins(Context context, JSONObject data, String imageId) {

        ProgressDialog pDialog;
        pDialog = ProgressDialog.show(context, "Procesando Imagen...", "Por favor espere", true,false);

        Log.i("CpPlugins", "Entered POST request...");

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CpPlugins POST OK", response);
                pDialog.dismiss();
                readPOSTJson(response, imageId, context);
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("CpPlugins POST Error", error.toString());
                pDialog.dismiss();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setMessage("Error procesando la imagen en CpPlugins.")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialogInterface, i) -> { });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return data.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                return headers;
            }
        };
        queue.add(request);
    }

    public void readPOSTJson(String new_buffer, String imageId, Context context){
        //String raw_buffer = (String) json.get("raw_buffer");
        Intent intent = new Intent(context, ProcessedImageActivity.class);
        intent.putExtra("Buffer", Base64.decode(new_buffer, Base64.DEFAULT));
        intent.putExtra("imageId", imageId);
        intent.putExtra("arrival", "CpPlugins");
        intent.putExtra("title", "nueva_imagen_procesada");
        context.startActivity(intent);
    }


}
