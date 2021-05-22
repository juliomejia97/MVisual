package com.example.pixelmanipulation.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pixelmanipulation.InfoListActivity;
import com.example.pixelmanipulation.ProcessedImageActivity;
import com.example.pixelmanipulation.R;
import com.example.pixelmanipulation.model.DataViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.providers.CpPluginsProvider;
import com.providers.FirebaseProvider;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmListAdapter extends ArrayAdapter<String> {
    private CpPluginsProvider provider;
    private List<String> listDatos;
    private Context context;
    private String imageId;
    private byte[] initialBuffer, editedBuffer;
    private int W, H;

    public AlgorithmListAdapter(Context context, ArrayList<String> listDatos, String imageId, byte[] initialBuffer, byte[] editedBuffer, int W, int H) {
        super(context, R.layout.algorithm_list_adapter, listDatos);
        this.provider = CpPluginsProvider.getInstance();
        this.context = context;
        this.listDatos = listDatos;
        this.imageId = imageId;
        this.initialBuffer = initialBuffer;
        this.editedBuffer = editedBuffer;
        this.W = W;
        this.H = H;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.from(this.context).inflate(R.layout.algorithm_list_adapter, parent, false);
        TextView algorithmName = mView.findViewById(R.id.txtViewAlgorithmAdapter);
        algorithmName.setText(this.listDatos.get(position));
        ImageView image = mView.findViewById(R.id.imgViewAlgorithmAdapter);
        image.setImageDrawable(mView.getResources().getDrawable(R.drawable.neural));
        LinearLayout llData = mView.findViewById(R.id.llDataInfoAlgorithm);
        llData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject data = provider.createJSON(H, W, initialBuffer, editedBuffer, listDatos.get(position));
                //provider.sendPOSTRequestCpPlugins(context, data, imageId);
                Intent intent = new Intent(context, ProcessedImageActivity.class);
                intent.putExtra("Buffer", editedBuffer);
                intent.putExtra("imageId", imageId);
                intent.putExtra("arrival", "CpPlugins");
                intent.putExtra("title", "nueva_imagen_procesada");
                context.startActivity(intent);
            }
        });

        return mView;
    }
}
