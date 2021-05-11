
package com.example.pixelmanipulation.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.pixelmanipulation.R;
import com.example.pixelmanipulation.model.DataViewHolder;
import com.providers.FirebaseProvider;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<DataViewHolder> {

    private FirebaseProvider provider;
    private List<DataViewHolder> listDatos;
    private Context context;
    private int level;

    public ListViewAdapter(Context context, ArrayList<DataViewHolder> listDatos, int level) {
        super(context, R.layout.list_adapter, listDatos);
        this.provider = FirebaseProvider.getInstance();
        this.context = context;
        this.listDatos = listDatos;
        this.level = level;
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
                if(!type.equalsIgnoreCase("Imagenes")){
                    Intent intent = new Intent(getContext(), InfoListActivity.class);
                    intent.putExtra("Id", listDatos.get(position).getId());
                    intent.putExtra("Type", type);
                    intent.putExtra("Level", level);
                    view.getContext().startActivity(intent);
                } else {
                    String mhdName = listDatos.get(position).getInfo();
                    String rawName = mhdName.replace(".mhd", ".raw");
                    mhdName = mhdName.replace(".mhd", "");
                    rawName = rawName.replace(".raw", "");
                    provider.loadImage(mhdName, rawName, view.getContext());
                }
            }
        });

        /*
        TODO CUANDO SE TRAE LA INFO DE AMPLIFY TAMBIEN TRAER LA IMAGEN ASOCIADA A DICHA ENTIDAD
         */

        if(type.equalsIgnoreCase("Pacientes")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.avatar5));
        } else if (type.equalsIgnoreCase("Estudios")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.folder));
        } else if (type.equalsIgnoreCase("Series")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.scan));
        } else if (type.equalsIgnoreCase("Imagenes")){
            image.setImageDrawable(mView.getResources().getDrawable(R.drawable.processed_image));
        }

        return mView;
    }
}