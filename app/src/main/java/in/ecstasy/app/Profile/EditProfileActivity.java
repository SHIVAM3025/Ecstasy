package in.ecstasy.app.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import in.ecstasy.app.Objects.User;
import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    ApiInterface apiInterface;
    User currentUser;
    TextInputLayout name, username, bio, phone, web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        name = findViewById(R.id.profile_editName_view);
        username = findViewById(R.id.profile_editUserName_view);
        bio = findViewById(R.id.profile_editBio_view);
        phone = findViewById(R.id.profile_editPhone_view);
        web = findViewById(R.id.profile_editWeb_view);
        getCurrentUser();


    }

    private void getCurrentUser() {
        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN",null);
        /*Call<User> call = apiInterface.getCurrentUserProfile(idToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "CurrentUserProfile: " + response.body());
                    currentUser = response.body();
                    name.getEditText().setText(currentUser.getName());
                    username.getEditText().setText(currentUser.getUsername());
                    bio.getEditText().setText(currentUser.getBio());
                    phone.getEditText().setText(currentUser.getPhonenumber());
                    web.getEditText().setText(currentUser.getWeb());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "CurrentUserProfileRequest: " + t.getLocalizedMessage());
            }
        });*/
    }

    public void handleEditProfile(View view) {
        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN",null);
        if(name.getEditText().getText().toString().trim().equals("")){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(username.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(bio.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter bio", Toast.LENGTH_SHORT).show();
            return;
        }
   /*     if(phone.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter phone", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(web.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter web address", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<Object> call = apiInterface.editProfile(idToken,
                name.getEditText().getText().toString().trim(),
                username.getEditText().getText().toString().trim(),
                bio.getEditText().getText().toString().trim(),
                phone.getEditText().getText().toString().trim(),
                web.getEditText().getText().toString().trim());

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.body().toString().equals("success")){
                    Log.d(TAG, "EditProfile: "+response.body());
                    Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d(TAG, "EditProfile: "+t.getLocalizedMessage());
            }
        });
    }


}