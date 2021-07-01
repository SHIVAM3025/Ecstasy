package in.ecstasy.app.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;

import in.ecstasy.app.MainActivity;
import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private int RC_SIGN_IN = 762;
    private ApiInterface apiInterface;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String USER_TYPE = "Audience";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

    }

    public void handleLogin(View view) {
        switch (view.getId()) {
            case R.id.artist_login:
                USER_TYPE = "Artist";
                //artist login
                break;
            case R.id.audience_login:
                USER_TYPE = "Audience";
                //audience login
                break;
        }

/*
        Intent intent = new Intent(LoginActivity.this, ChooseLoginActivity.class);
        startActivity(intent);
        finish();*/


        List<AuthUI.IdpConfig> providers = Arrays.asList(
                //new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.AppleBuilder().build()
        );

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                //set logo and privacy policy
                .setTheme(R.style.Theme_Ecstasy)
                .build(), RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN", null);
        if (idToken != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "Logged In Successfully ");
                startActivity(new Intent(LoginActivity.this, ViewThemeVideoActivity.class));
                subscribeUserToFirebaseMessaging();
                assert user != null;
                user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Log.d(TAG, "IdToken: " + getTokenResult.getToken());
                        SharedPreferences.Editor editor = getSharedPreferences("Ecstasy", MODE_PRIVATE).edit();
                        editor.putString("ID_TOKEN", getTokenResult.getToken());
                        editor.apply();
                        if(response.isNewUser()){
                            registerUser(getTokenResult.getToken());
                        }else{
                            Toast.makeText(LoginActivity.this, "Welcome Back " + user.getDisplayName() , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            } else {
                if(response!= null){
                    Log.e(TAG, "LoginError: " + response.getError().toString());
                    Toast.makeText(this, "Error Logging In", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void subscribeUserToFirebaseMessaging() {
        String uuid = FirebaseAuth.getInstance().getUid();
        FirebaseMessaging.getInstance().subscribeToTopic(uuid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "FirebaseMessaging: "+ task.isSuccessful());
                    }
                });
    }

    private void registerUser(String idToken) {
        Call<Object> call = apiInterface.registerUser(
                idToken, firebaseAuth.getCurrentUser().getDisplayName(), USER_TYPE,
                firebaseAuth.getCurrentUser().getPhotoUrl().toString()
        );
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(!response.isSuccessful()) return;
                Toast.makeText(LoginActivity.this, "Welcome " + firebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "RegisterApi: " + t.getLocalizedMessage() );
            }
        });
    }

    public void handleLoginVideo(View view) {
        startActivity(new Intent(LoginActivity.this, ViewThemeVideoActivity.class));
    }

}