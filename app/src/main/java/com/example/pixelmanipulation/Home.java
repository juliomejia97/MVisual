package com.example.pixelmanipulation;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {

    TextView title_home1,title_home2,title_home3;
    LinearLayout llPatients, llStudies, llSeries,llProcesed;
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
        llProcesed = findViewById(R.id.llProcesed);
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
        topBarAnimation.setDuration(2500);

        ObjectAnimator floatingBtnAnimation = ObjectAnimator.ofFloat(top_bar_home, "translationY", -852f);
        floatingBtnAnimation.setDuration(2500);


        tlHomeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO


            }
        });

        llPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAnimations(topBarAnimation);
               // Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
               // intent.putExtra("Type", "Pacientes");
               // startActivity(intent);
            }
        });


        llStudies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.putExtra("Type", "Estudios");
                startActivity(intent);
            }
        });

        llSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                intent.putExtra("Type", "Series");
                startActivity(intent);
            }
        });
    }
    private void hideAnimations(ObjectAnimator topBarAnimation) {
        topBarAnimation.start();
        btnFloating.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(null);
    }
}