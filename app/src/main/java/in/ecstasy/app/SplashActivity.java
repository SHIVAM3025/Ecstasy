package in.ecstasy.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import in.ecstasy.app.Login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    public static String ID_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences.Editor editor = getSharedPreferences("Ecstasy" , MODE_PRIVATE).edit();
        editor.putString("joker" , "joker");
        editor.commit();

        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN", null);
                if (idToken != null) {
                    FirebaseAuth.getInstance().getAccessToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        @Override
                        public void onSuccess(GetTokenResult getTokenResult) {
                            ID_TOKEN = getTokenResult.getToken();
                            Log.e("Splash ScreenToken", ID_TOKEN);
                            SharedPreferences.Editor editor = getSharedPreferences("Ecstasy", MODE_PRIVATE).edit();
                            editor.putString("ID_TOKEN", getTokenResult.getToken());
                            editor.apply();
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },2000);

    }
}