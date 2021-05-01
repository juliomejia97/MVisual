package com.amplifyframework.datastore.generated.model;

import android.content.Context;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.amplifyframework.util.Immutable;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelProvider;
import com.example.pixelmanipulation.model.DataViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/**
 *  Contains the set of model classes that implement {@link Model}
 * interface.
 */

public final class PatientsProvider {

    private static PatientsProvider patientsInstance;
    private static ArrayList<DataViewHolder> patients;
    private static ArrayList<DataViewHolder> studies;
    private static ArrayList<DataViewHolder> series;


    private PatientsProvider() {

        patients = new ArrayList<DataViewHolder>();
        studies = new ArrayList<DataViewHolder>();
        series = new ArrayList<DataViewHolder>();

        DataViewHolder p1 = new DataViewHolder("1", "Juan Camilo Chafloque Mesia", "Pacientes");
        DataViewHolder p2 = new DataViewHolder("2", "Abel Santiago Cortes Avedaño", "Pacientes");
        DataViewHolder p3 = new DataViewHolder("3", "Julio Andres Mejía Vera", "Pacientes");
        DataViewHolder p4 = new DataViewHolder("4", "Juan Sebastian Osorio Garcia", "Pacientes");
        DataViewHolder p5 = new DataViewHolder("5","Leonardo Flórez Valencia", "Pacientes");
        DataViewHolder p6 = new DataViewHolder("6","Pepito Perez Perez", "Pacientes");
        DataViewHolder p7 = new DataViewHolder("7","Bob Hernandez Fernandez", "Pacientes");
        DataViewHolder p8 = new DataViewHolder("8","Carlos Fernandez Gutierrez", "Pacientes");
        DataViewHolder p9 = new DataViewHolder("9","David Gutierrez Fernandez", "Pacientes");

        DataViewHolder e1 = new DataViewHolder("10", "Estudio #1", "Estudios");
        DataViewHolder e2 = new DataViewHolder("11", "Estudio #2", "Estudios");
        DataViewHolder e3 = new DataViewHolder("12", "Estudio #3", "Estudios");
        DataViewHolder e4 = new DataViewHolder("13", "Estudio #4", "Estudios");
        DataViewHolder e5 = new DataViewHolder("14", "Estudio #5", "Estudios");
        DataViewHolder e6 = new DataViewHolder("15", "Estudio #6", "Estudios");
        DataViewHolder e7 = new DataViewHolder("16", "Estudio #7", "Estudios");
        DataViewHolder e8 = new DataViewHolder("17", "Estudio #8", "Estudios");
        DataViewHolder e9 = new DataViewHolder("18", "Estudio #9", "Estudios");
        DataViewHolder e10 = new DataViewHolder("19", "Estudio #10", "Estudios");
        DataViewHolder e11 = new DataViewHolder("20", "Estudio #11", "Estudios");

        DataViewHolder s1 = new DataViewHolder("21", "Serie #1", "Series");
        DataViewHolder s2 = new DataViewHolder("22", "Serie #2", "Series");
        DataViewHolder s3 = new DataViewHolder("23", "Serie #3", "Series");
        DataViewHolder s4 = new DataViewHolder("24", "Serie #4", "Series");
        DataViewHolder s5 = new DataViewHolder("25", "Serie #5", "Series");
        DataViewHolder s6 = new DataViewHolder("26", "Serie #6", "Series");
        DataViewHolder s7 = new DataViewHolder("27", "Serie #7", "Series");
        DataViewHolder s8 = new DataViewHolder("28", "Serie #8", "Series");
        DataViewHolder s9 = new DataViewHolder("29", "Serie #9", "Series");
        DataViewHolder s10 = new DataViewHolder("30", "Serie #10", "Series");
        DataViewHolder s11 = new DataViewHolder("31", "Serie #11", "Series");

        p1.addData(e11);
        p2.addData(e10);
        p3.addData(e9);
        p4.addData(e8);
        p5.addData(e7);
        p6.addData(e6);
        p7.addData(e5);
        p8.addData(e4);
        p8.addData(e3);
        p9.addData(e2);
        p9.addData(e1);

        e1.addData(s1);
        e2.addData(s2);
        e3.addData(s3);
        e4.addData(s4);
        e5.addData(s5);
        e6.addData(s6);
        e7.addData(s7);
        e8.addData(s8);
        e9.addData(s9);
        e10.addData(s10);
        e11.addData(s11);

        patients.add(p1);
        patients.add(p2);
        patients.add(p3);
        patients.add(p4);
        patients.add(p5);
        patients.add(p6);
        patients.add(p7);
        patients.add(p8);
        patients.add(p9);

        studies.add(e1);
        studies.add(e2);
        studies.add(e3);
        studies.add(e4);
        studies.add(e5);
        studies.add(e6);
        studies.add(e7);
        studies.add(e8);
        studies.add(e9);
        studies.add(e10);
        studies.add(e11);

        series.add(s1);
        series.add(s2);
        series.add(s3);
        series.add(s4);
        series.add(s5);
        series.add(s6);
        series.add(s7);
        series.add(s8);
        series.add(s9);
        series.add(s10);
        series.add(s11);
    }

    public static PatientsProvider getInstance() {
        if (patientsInstance == null) {
            patientsInstance = new PatientsProvider();
        }
        return patientsInstance;
    }

    public static ArrayList<DataViewHolder> getAllPatients(){
        return patients;
    }

    public static ArrayList<DataViewHolder> getAllStudies(){
        return studies;
    }

    public static ArrayList<DataViewHolder> getAllSeries(){
        return series;
    }

    public static DataViewHolder getPatientById(String id){
        for(DataViewHolder patient: patients){
            if(patient.getId().equalsIgnoreCase(id)){
                return patient;
            }
        }
        return null;
    }

    public static DataViewHolder getStudyById(String id){
        for(DataViewHolder study: studies){
            if(study.getId().equalsIgnoreCase(id)){
                return study;
            }
        }
        return null;
    }

    public static DataViewHolder getSeriesById(String id){
        for(DataViewHolder serie: series){
            if(serie.getId().equalsIgnoreCase(id)){
                return serie;
            }
        }
        return null;
    }

    public static DataViewHolder getStudyPatient(String id){
        for(DataViewHolder patient: patients){
            for(DataViewHolder study: patient.getData()){
                if(study.getId().equalsIgnoreCase(id)){
                    return patient;
                }
            }
        }
        return null;
    }

    public static DataViewHolder getSeriesStudy(String id){
        for(DataViewHolder study: studies){
            for(DataViewHolder serie: study.getData()){
                if(serie.getId().equalsIgnoreCase(id)){
                    return study;
                }
            }
        }
        return null;
    }

    public static ArrayList<DataViewHolder> getStudiesByPatient(String id){
        for(DataViewHolder patient: patients){
            if(patient.getId().equalsIgnoreCase(id)){
                return patient.getData();
            }
        }
        return null;
    }

    public static ArrayList<DataViewHolder> getSeriesByStudy(String id){
        for(DataViewHolder study: studies){
            if(study.getId().equalsIgnoreCase(id)){
                return study.getData();
            }
        }
        return null;
    }

}
