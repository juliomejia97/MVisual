package com.example.pixelmanipulation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.amplifyframework.datastore.generated.model.AmplifyModelProvider;
import com.example.pixelmanipulation.adapters.ListViewAdapter;
import com.example.pixelmanipulation.model.DataViewHolder;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private ListViewAdapter mListInfoAdapter;
    private Spinner spinner;
    private ListView mlista;
    private String[] arrayForSpinner = {"Order by", "Name", "Surname"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_list, container,false);
        AmplifyModelProvider.getInstance(mView.getContext());
        mlista = mView.findViewById(R.id.recycleViewInfoList);
        spinner = mView.findViewById(R.id.spinnerList);
        initView(this.getArguments().getString("Type"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_spinner_dropdown_item, arrayForSpinner);
        spinner.setAdapter(adapter);

        return mView;
    }


    public void initView(String type) {

        Log.i("Type", type);

        if(type.equalsIgnoreCase("Pacientes")){
            ArrayList<DataViewHolder> peopleList = new ArrayList<>();
            peopleList.add(new DataViewHolder("Juan Camilo Chafloque Mesia", type));
            peopleList.add(new DataViewHolder("Abel Santiago Cortes Avedaño", type));
            peopleList.add(new DataViewHolder("Julio Andres Mejía Vera", type));
            peopleList.add(new DataViewHolder("Juan Sebastian Osorio Garcia", type));

            mListInfoAdapter = new ListViewAdapter(getActivity(), peopleList);
            mlista.setAdapter(mListInfoAdapter);
        } else if (type.equalsIgnoreCase("Estudios")){
            ArrayList<DataViewHolder> studyList = new ArrayList<>();
            studyList.add(new DataViewHolder("Estudio #1", type));
            studyList.add(new DataViewHolder("Estudio #2", type));
            studyList.add(new DataViewHolder("Estudio #3", type));
            studyList.add(new DataViewHolder("Estudio #4", type));
            studyList.add(new DataViewHolder("Estudio #5", type));
            studyList.add(new DataViewHolder("Estudio #6", type));
            studyList.add(new DataViewHolder("Estudio #7", type));
            studyList.add(new DataViewHolder("Estudio #8", type));
            mListInfoAdapter = new ListViewAdapter(getActivity(), studyList);
            mlista.setAdapter(mListInfoAdapter);

        } else if (type.equalsIgnoreCase("Series")){
            ArrayList<DataViewHolder> seriesList = new ArrayList<>();
            seriesList.add(new DataViewHolder("Serie #1", type));
            seriesList.add(new DataViewHolder("Serie #2", type));
            seriesList.add(new DataViewHolder("Serie #3", type));
            seriesList.add(new DataViewHolder("Serie #4", type));
            seriesList.add(new DataViewHolder("Serie #5", type));
            seriesList.add(new DataViewHolder("Serie #6", type));
            seriesList.add(new DataViewHolder("Serie #7", type));
            seriesList.add(new DataViewHolder("Serie #8", type));
            mListInfoAdapter = new ListViewAdapter(getActivity(), seriesList);
            mlista.setAdapter(mListInfoAdapter);
        }
    }
}