package com.providers;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CpPluginsProvider {

    private static CpPluginsProvider cpPluginsProvider;

    private CpPluginsProvider(){ }

    public static CpPluginsProvider getInstance() {
        if (cpPluginsProvider == null) {
            cpPluginsProvider = new CpPluginsProvider();
        }
        return cpPluginsProvider;
    }

    public JSONObject createJSON(int height, int width, byte[] initialBuffer, byte[] editedBuffer){

        try {
            JSONObject restJSON = new JSONObject();

            //Packages JSON
            restJSON.put("packages", "itk");

            //Description JSON
            restJSON.put("xml_description", "itk.OtsuMultipleThresholdsImageFilter");

            //Parameters JSON
            JSONArray paramArray = new JSONArray();
            JSONObject param1 = new JSONObject();
            JSONObject param2 = new JSONObject();
            param1.put("name", "NumberOfHistogramBins");
            param1.put("value", "100");
            param2.put("name", "ReturnBinMidPoint");
            param2.put("value", "True");
            paramArray.put(param1);
            paramArray.put(param2);

            restJSON.put("parameters", paramArray);

            //Inputs JSON
            JSONArray inputsArray = new JSONArray();
            JSONObject input1 = new JSONObject();
            JSONObject input2 = new JSONObject();
            input1.put("name", "Input");
            input1.put("data_format", "scalar");
            input1.put("data_type", "short");
            input1.put("dimensions",  "512,512");
            input1.put("origin", "0,0");
            input1.put("spacing", "1,1");
            input1.put("direction", "1,0,0,1");
            input1.put("raw_buffer", "0101010101");
            //input1.put("raw_buffer", Base64.encodeToString(originalBuffer, Base64.DEFAULT));

            input2.put("name", "Mask");
            input2.put("data_format", "rgba");
            input2.put("data_type", "uchar");
            input2.put("dimensions", "512,512");
            input2.put("origin", "0,0");
            input2.put("spacing", "1,1");
            input2.put("direction", "1,0,0,1");
            input2.put("raw_buffer", "0101010101");

            inputsArray.put(input1);
            inputsArray.put(input2);

            restJSON.put("inputs", inputsArray);

            return restJSON;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendGETRequestCpPlugins(Context context) {

        String url = "http://150.136.161.199:5000/api/v1.0/pipeline";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("CpPlugins GET OK", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("CpPlugins GET Error", error.toString());
            }
        });

        queue.add(request);
    }

    public void sendPOSTRequestCpPlugins(Context context) {

        Log.i("CpPlugins", "Entered POST request...");
        JSONObject data = createJSON(0, 0, null, null);
        Log.i("JSON", data.toString());

        String url = "http://150.136.161.199:5000/api/v1.0/pipeline";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CpPlugins POST OK", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("CpPlugins POST Error", error.toString());
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

    public byte[] readPOSTJson(JSONObject json){
        try {
            String raw_buffer = (String) json.get("raw_buffer");
            return Base64.decode(raw_buffer, Base64.DEFAULT);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
