package com.example.pixelmanipulation;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.pixelmanipulation.model.DataViewHolder;
import com.providers.FirebaseProvider;

public class LandingPage extends AppCompatActivity {

    private TextView text_landing1,text_landing2;
    private ImageView nurse,footbar,top_bar;
    private TableLayout grid_buttons;
    private FirebaseProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        this.provider = FirebaseProvider.getInstance();

        text_landing1 = findViewById(R.id.title_landing1);
        text_landing2 = findViewById(R.id.title_landing2);
        nurse= findViewById(R.id.enfermera);
        footbar= findViewById(R.id.footbar);
        top_bar=findViewById(R.id.top_bar);
        grid_buttons=findViewById(R.id.grid_landing);

        text_landing1.setText(HtmlCompat.fromHtml(getString(R.string.test), HtmlCompat.FROM_HTML_MODE_LEGACY));


        text_landing1.animate()
                .alpha(1f)
                .setDuration(2000)
                .setListener(null);
        text_landing2.animate()
                .alpha(1f)
                .setDuration(4000)
                .setListener(null);

        ObjectAnimator animation = ObjectAnimator.ofFloat(nurse, "translationX", 550f);
        animation.setDuration(2500);
        animation.start();

        ObjectAnimator animation2 = ObjectAnimator.ofFloat(footbar, "translationY", -600f);
        animation2.setDuration(2500);
        animation2.start();

        ObjectAnimator animation3 = ObjectAnimator.ofFloat(footbar, "translationY", 600f);
        animation3.setDuration(2500);

        ObjectAnimator animation4 = ObjectAnimator.ofFloat(nurse, "translationX", -550f);
        animation4.setDuration(2500);

        ObjectAnimator animation5 = ObjectAnimator.ofFloat(top_bar, "translationY", 852f);
        animation5.setDuration(2500);

        footbar.setOnClickListener(v -> {
            animation3.start();
            animation4.start();
            animation5.start();

            text_landing1.animate()
                    .alpha(0f)
                    .setDuration(2000)
                    .setListener(null);
            text_landing2.animate()
                    .alpha(0f)
                    .setDuration(2000)
                    .setListener(null);

            grid_buttons.animate()
                    .alpha(1f)
                    .setDuration(2500)
                    .setListener(null);
            Runnable r = () -> {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);

                startActivity(intent);
            };

            Handler h = new Handler();
            h.postDelayed(r, 2500);
        });
    }
}