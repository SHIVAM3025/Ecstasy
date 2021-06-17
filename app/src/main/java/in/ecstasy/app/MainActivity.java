package in.ecstasy.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import in.ecstasy.app.Home.HomeFragment;
import in.ecstasy.app.Login.LoginActivity;
import in.ecstasy.app.Notification.NotificationFragment;
import in.ecstasy.app.Objects.User;
import in.ecstasy.app.People.PeopleFragment;
import in.ecstasy.app.Profile.ProfileFragment;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import in.ecstasy.app.Search.SearchFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener ,  FirebaseAuth.AuthStateListener{

    private static final String TAG = "MainActivity";
    public static String ID_TOKEN ;
    public static User currentUser ;
    ApiInterface apiInterface;
    ProgressDialog progressDialog;
    Handler handler;
    private final int REFRESH_TOKEN_DELAY = 1800000;
    private final int REQUEST_READ_CONTACTS = 768;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ID_TOKEN = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN", null);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        refreshIdToken();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_home :
                selectedFragment = new HomeFragment();
                break;
            case R.id.nav_search :
                selectedFragment = new SearchFragment();
                break;
            case R.id.nav_add_people :
                if(!checkForContactsAccessPermission()) return false;
                selectedFragment = new PeopleFragment();
                break;
            case R.id.nav_notifications :
                selectedFragment = new NotificationFragment();
                break;
            case R.id.nav_profile :
                selectedFragment = new ProfileFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,selectedFragment).commit();

        return true;
    }

    private boolean checkForContactsAccessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
                return false;
            }
        }
        return true;
    }

    private void refreshIdToken() {
        handler = new Handler(getMainLooper());
        handler.postDelayed(getIdToken, REFRESH_TOKEN_DELAY);
    }

    private final Runnable getIdToken = new Runnable() {
        @Override
        public void run() {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        @Override
                        public void onSuccess(GetTokenResult getTokenResult) {
                            ID_TOKEN = getTokenResult.getToken();
                            SharedPreferences.Editor editor = getSharedPreferences("Ecstasy", MODE_PRIVATE).edit();
                            editor.putString("ID_TOKEN", getTokenResult.getToken());
                            editor.apply();
                        }
                    });
            handler.postDelayed(getIdToken,REFRESH_TOKEN_DELAY);
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Reading contacts Permission granted
                }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
        getCurrentUserProfile();
    }

    private void getCurrentUserProfile() {
        Call<User> call = apiInterface.getCurrentUserProfile(ID_TOKEN);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "GetUserProfileInfo: " + response.body());
                if(response.isSuccessful()){
                    currentUser = response.body();
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        initialiseHomeFragment();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "GetUserProfileInfoRequest: " + t.getLocalizedMessage());
            }
        });
    }

    private void initialiseHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance(null)).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(getIdToken);
    }


    @Override
    public void onAuthStateChanged(@NonNull  FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}