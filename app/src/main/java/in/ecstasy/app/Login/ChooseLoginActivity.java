package in.ecstasy.app.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import in.ecstasy.app.MainActivity;
import in.ecstasy.app.R;

public class ChooseLoginActivity extends AppCompatActivity {

    private String USER_TYPE = "Audience";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);
    }

    public void handleLogin(View view) {
        switch (view.getId()) {
            case R.id.google_login:
                USER_TYPE = "Artist";
                //artist login
                break;
            case R.id.facebook_login:
                USER_TYPE = "Audience";
                //audience login
                break;
            case R.id.apple_login:
                break;
        }

            Intent intent = new Intent(ChooseLoginActivity.this , MainActivity.class);
            startActivity(intent);
            finish();


    }


}