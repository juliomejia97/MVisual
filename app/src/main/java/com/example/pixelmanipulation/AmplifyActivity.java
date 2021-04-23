package com.example.pixelmanipulation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Todo;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class AmplifyActivity extends AppCompatActivity {

    private TextView mTextView;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amplify);

        mTextView = (TextView) findViewById(R.id.text);
        imgView =(ImageView) findViewById(R.id.imagensita);

            try {
                // Add these lines to add the AWSApiPlugin plugins
                Amplify.addPlugin(new AWSApiPlugin());
                Amplify.addPlugin(new AWSCognitoAuthPlugin());

                Amplify.addPlugin(new AWSS3StoragePlugin());
                Amplify.configure(getApplicationContext());

                Log.i("MyAmplifyApp", "Initialized Amplify");
            } catch (AmplifyException error) {
                Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
            }


            //lee desde almacenamiento interno
            Bitmap myBitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir()+ "/downlojagd.jpeg");
            imgView.setImageBitmap(myBitmap);

            loadImage();
    }

    private void loadImage() {
        // descarga y lee desde aws
        Amplify.Storage.downloadFile(
                "tomografia.jpeg",
                new File(getApplicationContext().getFilesDir() + "/downlojagd.jpeg"),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getFile().getName());
                    //Bitmap myBitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir()+ "/download.jpeg");
                    //imgView.setImageBitmap(myBitmap);
                    },
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );

        Amplify.Storage.getUrl(
                "tomografia.jpeg",
                result -> Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl()),
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );
    }
}