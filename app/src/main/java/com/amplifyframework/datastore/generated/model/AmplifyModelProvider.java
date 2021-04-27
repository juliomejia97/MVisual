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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/** 
 *  Contains the set of model classes that implement {@link Model}
 * interface.
 */

public final class AmplifyModelProvider implements ModelProvider {
  private static final String AMPLIFY_MODEL_VERSION = "1e935267ff3bf73800b32443538139cb";
  private static AmplifyModelProvider amplifyGeneratedModelInstance;
  private AmplifyModelProvider(Context context) {
      try {
          // Add these lines to add the AWSApiPlugin plugins
          Amplify.addPlugin(new AWSApiPlugin());
          Amplify.addPlugin(new AWSCognitoAuthPlugin());

          Amplify.addPlugin(new AWSS3StoragePlugin());
          Amplify.configure(context);

          Log.i("MyAmplifyApp", "Initialized Amplify");
      } catch (AmplifyException error) {
          Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
      }
  }
  
  public static AmplifyModelProvider getInstance(Context context) {
    if (amplifyGeneratedModelInstance == null) {
      amplifyGeneratedModelInstance = new AmplifyModelProvider(context);
    }
    return amplifyGeneratedModelInstance;
  }
  
  /** 
   * Get a set of the model classes.
   * 
   * @return a set of the model classes.
   */
  @Override
   public Set<Class<? extends Model>> models() {
    final Set<Class<? extends Model>> modifiableSet = new HashSet<>(
          Arrays.<Class<? extends Model>>asList(Todo.class)
        );
    
        return Immutable.of(modifiableSet);
        
  }
  
  /** 
   * Get the version of the models.
   * 
   * @return the version string of the models.
   */
  @Override
   public String version() {
    return AMPLIFY_MODEL_VERSION;
  }
}
