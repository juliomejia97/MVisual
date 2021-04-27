package com.example.pixelmanipulation;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amplifyframework.datastore.generated.model.AmplifyModelProvider;

public class FilesFragment extends Fragment {

    LinearLayout llPacientes, llSeries, llEstudios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_files, container,false);
        llPacientes = mView.findViewById(R.id.llPacientes);
        llEstudios = mView.findViewById(R.id.llEstudios);
        llSeries = mView.findViewById(R.id.llSeries);
        llPacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Layout", "Pressed Patient");
            }
        });

        llEstudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Layout", "Pressed Studies");
            }
        });

        llSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Layout", "Pressed Series");
            }
        });
        return mView;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}