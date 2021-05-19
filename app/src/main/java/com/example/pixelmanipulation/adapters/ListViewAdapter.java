
package com.example.pixelmanipulation.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
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
import com.providers.FirebaseProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<DataViewHolder> {

    private FirebaseProvider provider;
    private List<DataViewHolder> listDatos;
    private Context context;
    private StorageReference mStorage;
    private int level;

    public ListViewAdapter(Context context, ArrayList<DataViewHolder> listDatos, int level) {
        super(context, R.layout.list_adapter, listDatos);
        this.provider = FirebaseProvider.getInstance();
        this.context = context;
        this.listDatos = listDatos;
        this.level = level;
        this.mStorage = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.from(this.context).inflate(R.layout.list_adapter, parent, false);
        TextView infoText = mView.findViewById(R.id.txtViewAdapter);
        ImageView image = mView.findViewById(R.id.imgViewAdapter);
        String type = this.listDatos.get(position).getType();
        infoText.setText(this.listDatos.get(position).getInfo());
        LinearLayout llData = mView.findViewById(R.id.llDataInfo);
        llData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equalsIgnoreCase("pacientes") || type.equalsIgnoreCase("estudios") || type.equalsIgnoreCase("series")){
                    Intent intent = new Intent(getContext(), InfoListActivity.class);
                    intent.putExtra("Id", listDatos.get(position).getId());
                    intent.putExtra("Type", type);
                    intent.putExtra("Level", level);
                    view.getContext().startActivity(intent);
                } else if(type.equalsIgnoreCase("imagenes")) {
                    String id = listDatos.get(position).getId();
                    String mhdName = listDatos.get(position).getInfo();
                    String rawName = mhdName.replace(".mhd", ".raw");
                    mhdName = mhdName.replace(".mhd", "");
                    rawName = rawName.replace(".raw", "");
                    provider.loadImage(mhdName, rawName, id, view.getContext());
                } else if(type.equalsIgnoreCase("procesadas")){
                    Intent intent = new Intent(getContext(), ProcessedImageActivity.class);
                    intent.putExtra("Buffer", getBuffer(image));
                    intent.putExtra("imageId", listDatos.get(position).getId());
                    intent.putExtra("arrival", "ProcessedList");
                    intent.putExtra("title", listDatos.get(position).getInfo());
                    context.startActivity(intent);
                }
            }
        });

        if(type.equalsIgnoreCase("pacientes")){
            try {
                downloadProfileImage(this.listDatos.get(position).getId(), image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equalsIgnoreCase("estudios")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.folder));
        } else if (type.equalsIgnoreCase("series")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.scan));
        } else if (type.equalsIgnoreCase("imagenes")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.processed_image));
        } else if (type.equalsIgnoreCase("procesadas")){
            try {
                downloadProcessedImage(this.listDatos.get(position).getId(), image, position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mView;
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

    private void downloadProcessedImage(String idImage, final ImageView photo, int position) throws IOException {
        final File localFile = File.createTempFile("images", "png");
        StorageReference imageRef = mStorage.child("Processed/" + idImage + "/" + this.listDatos.get(position).getInfo() + ".png");
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

    private byte[] getBuffer(ImageView image){
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}