
package com.example.pixelmanipulation.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelmanipulation.R;
import com.example.pixelmanipulation.model.DataViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<DataViewHolder> {

    private List<DataViewHolder> listDatos;
    private Context context;

    public ListViewAdapter(Context context, ArrayList<DataViewHolder> listDatos) {
        super(context, R.layout.list_adapter, listDatos);
        this.context = context;
        this.listDatos = listDatos;
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
        if(type.equalsIgnoreCase("Pacientes")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.avatar5));
        } else if (type.equalsIgnoreCase("Estudios")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.studies));
        } else if (type.equalsIgnoreCase("Series")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.series));
        }

        return mView;
    }
}