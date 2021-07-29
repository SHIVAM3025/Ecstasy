package in.ecstasy.app.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    TextInputLayout otp, phone_number;
    ApiInterface apiInterface;
    String phoneNumber, username, name, about;
    String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        phone_number = findViewById(R.id.profile_editPhone_view);
        otp = findViewById(R.id.profile_otpPhone_view);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        name = getIntent().getStringExtra("name");
        about = getIntent().getStringExtra("about");
        username = getIntent().getStringExtra("username");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

    }


    public void handleEditProfile(View view) {

        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN", null);
        if (phone_number.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (otp.getVisibility() == View.VISIBLE) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("sid", sid);
            jsonObject.addProperty("otp", otp.getEditText().getText().toString().trim());
            jsonObject.addProperty("phonenumber", "+91"+phoneNumber);

            apiInterface.verifyOTP(jsonObject).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(OTPActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                        switch (response.body().get("status").getAsString()) {
                            case "Error":
                                Toast.makeText(OTPActivity.this, "Server Error\n Try again after sometime.", Toast.LENGTH_SHORT).show();
                                break;
                            case "pending":
                                Toast.makeText(OTPActivity.this, "Otp is Wrong \n Please Retry", Toast.LENGTH_SHORT).show();
                                break;
                            case "approved":
                                Toast.makeText(OTPActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(OTPActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phonenumber", "+91" + phone_number.getEditText().getText().toString().trim());

        apiInterface.getOtp(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    sid = response.body().get("service_id").getAsString();
                    otp.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(OTPActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}