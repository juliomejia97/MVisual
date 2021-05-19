package com.example.pixelmanipulation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.providers.FirebaseProvider;

public class Home extends AppCompatActivity {

    TextView title_home1,title_home2,title_home3;
    LinearLayout llPatients, llStudies, llSeries, llProcessed;
    TableLayout tlHomeMenu;
    private ImageView top_bar_home;
    FloatingActionButton btnFloating ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title_home1 = findViewById(R.id.title_home1);
        title_home2 = findViewById(R.id.title_home2);
        title_home3 = findViewById(R.id.title_home3);
        llPatients = findViewById(R.id.llPatients);
        llStudies = findViewById(R.id.llStudies);
        llSeries = findViewById(R.id.llSeries);
        llProcessed = findViewById(R.id.llProcesed);
        tlHomeMenu = findViewById(R.id.tlHomeMenu);
        top_bar_home =findViewById(R.id.top_bar_home);
        btnFloating = findViewById(R.id.btnFloating);

        title_home1.bringToFront();
        title_home2.bringToFront();
        title_home3.bringToFront();

        title_home1.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);

        title_home2.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);
        title_home3.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);

        ObjectAnimator topBarAnimation = ObjectAnimator.ofFloat(top_bar_home, "translationY", -852f);
        topBarAnimation.setDuration(1000);



        llPatients.setOnClickListener(view -> {
            hideAnimations(topBarAnimation,"Patients");
            Runnable r = () -> {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("Type", "Pacientes");
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
                startActivity(intent);
            };

            Handler h = new Handler();
            h.postDelayed(r, 850);

        });


        llStudies.setOnClickListener(view -> {

                hideAnimations(topBarAnimation, "Studies");
            Runnable r = () -> {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.putExtra("Type", "Estudios");
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
                startActivity(intent);
            };

            Handler h = new Handler();
            h.postDelayed(r, 850);

        });

        llSeries.setOnClickListener(view -> {
            hideAnimations(topBarAnimation, "Series");
            Runnable r = () -> {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("Type", "Series");
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
                startActivity(intent);
            };

            Handler h = new Handler();
            h.postDelayed(r, 850);
        });
        llProcessed.setOnClickListener(view -> {
            hideAnimations(topBarAnimation, "Processed");
            llPatients.animate()
                    .scaleYBy(-0.5f)
                    .scaleXBy(-0.5f)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(1000);

            tlHomeMenu.animate()
                    .alpha(0f)
                    .setDuration(1800)
                    .setListener(null);

        });
    }
    private void hideAnimations(ObjectAnimator topBarAnimation,String folder) {
        topBarAnimation.start();
        btnFloating.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(null);

        tlHomeMenu.animate()
                .scaleYBy(-0.58f)
                .scaleXBy(-0.58f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500);

        switch(folder) {
            case "Patients":
                tlHomeMenu.animate().translationY(-1060f).setDuration(850);
                tlHomeMenu.animate().translationX(-210f).setDuration(850);

                llStudies.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llSeries.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);

                llProcessed.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                break;
            case "Studies":
                tlHomeMenu.animate().translationY(-1060f).setDuration(850);
                tlHomeMenu.animate().translationX(-415f).setDuration(850);
                llPatients.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llSeries.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);

                llProcessed.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                break;
            case "Series":
                tlHomeMenu.animate().translationY(-1280f).setDuration(850);
                tlHomeMenu.animate().translationX(-210f).setDuration(850);
                llPatients.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llStudies.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llProcessed.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                break;
            case "Processed":
                tlHomeMenu.animate().translationY(-1280f).setDuration(1500);
                tlHomeMenu.animate().translationX(-415f).setDuration(1500);
                llPatients.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llStudies.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                llSeries.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .setListener(null);
                break;
            default:
        }

    }
}