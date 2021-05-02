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

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class AmplifyModelProvider implements ModelProvider {

  private static final String AMPLIFY_MODEL_VERSION = "1e935267ff3bf73800b32443538139cb";
  private static AmplifyModelProvider amplifyGeneratedModelInstance;
  private static Context context;

  private AmplifyModelProvider(Context pContext) {
      try {
          // Add these lines to add the AWSApiPlugin plugins
          context = pContext;
          Amplify.addPlugin(new AWSApiPlugin());
          Amplify.addPlugin(new AWSCognitoAuthPlugin());

          Amplify.addPlugin(new AWSS3StoragePlugin());
          Amplify.configure(context);
          Log.i("Amplify", "Amplify Inicializado");
      } catch (AmplifyException error) {
          Log.e("Amplify", "No se pudo inicializar Amplify", error);
      }
  }
  
  public static AmplifyModelProvider getInstance(Context context) {
    if (amplifyGeneratedModelInstance == null) {
      amplifyGeneratedModelInstance = new AmplifyModelProvider(context);
    }
    return amplifyGeneratedModelInstance;
  }

  @Override
  public Set<Class<? extends Model>> models() {
    final Set<Class<? extends Model>> modifiableSet = new HashSet<>(
          Arrays.<Class<? extends Model>>asList(Todo.class)
        );
    
        return Immutable.of(modifiableSet);
        
  }

  @Override
  public String version() {
    return AMPLIFY_MODEL_VERSION;
  }

  public void loadImage(String mhdName, String rawName){

      Amplify.Storage.downloadFile(
              mhdName,
              new File(context.getFilesDir() + "/" + mhdName),
              result -> {
                  Log.i("Amplify", "Se generó el archivo mhd exitosamente: " + result.getFile().getName());
                  },
              error -> Log.e("Amplify",  "Error al descargar el archivo mhd", error)
      );

      Amplify.Storage.downloadFile(
              rawName,
              new File(context.getFilesDir() + "/" + rawName),
              result -> {
                  Log.i("Amplify", "Se generó el archivo raw exitosamente: " + result.getFile().getName());
                  Log.i("Test", "Download finished");
              },
              error -> Log.e("Amplify",  "Error al descargar el archivo raw", error)
      );
    }
}
