package com.example.pixelmanipulation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
                Bundle bundle = new Bundle();
                bundle.putString("Type", "Pacientes");
                Fragment mFragment= new ListFragment();
                mFragment.setArguments(bundle);
                FragmentManager manager = getChildFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_list_id, mFragment).commit();
            }
        });

        llEstudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Layout", "Pressed Studies");
                Bundle bundle = new Bundle();
                bundle.putString("Type", "Estudios");
                Fragment mFragment= new ListFragment();
                mFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_list_id, mFragment).commit();
            }
        });

        llSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Layout", "Pressed Series");
                Bundle bundle = new Bundle();
                bundle.putString("Type", "Series");
                Fragment mFragment= new ListFragment();
                mFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_list_id, mFragment).commit();
            }
        });
        return mView;

    }
}