package in.ecstasy.app.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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
    Button get_otp;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        phone_number = findViewById(R.id.profile_editPhone_view);
        otp = findViewById(R.id.profile_otpPhone_view);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        get_otp = findViewById(R.id.get_otp);

        user_id = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("USER_ID" ,"");

        //Use this when you need to get index from All_user

      /*  FirebaseDatabase.getInstance().getReference("ALLUSER").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                for(DataSnapshot currentDataSnapshot : dataSnapshot.getChildren()){
                    if (currentDataSnapshot.child("id").getValue().equals("user_id")){
                        Toast.makeText(OTPActivity.this, currentDataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });*/

        name = getIntent().getStringExtra("name");
        about = getIntent().getStringExtra("about");
        username = getIntent().getStringExtra("username");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("phonenumber", "+91" + phone_number.getEditText().getText().toString().trim());

                apiInterface.getOtp(jsonObject).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            sid = response.body().get("service_id").getAsString();
                            phoneNumber = phone_number.getEditText().getText().toString();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(OTPActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }


    public void handleEditProfile(View view) {

        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN", null);
        if (phone_number.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sid", sid);
        jsonObject.addProperty("otp", otp.getEditText().getText().toString().trim());
        jsonObject.addProperty("phonenumber", "+91" + phoneNumber);

        apiInterface.verifyOTP(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    HashMap<String  , Object> hashMap = new HashMap<>();
                    hashMap.put("phonenumber" , phoneNumber);

                    switch (response.body().get("status").getAsString()) {
                        case "Error":
                            Toast.makeText(OTPActivity.this, "Server Error\n Try again after sometime.", Toast.LENGTH_SHORT).show();
                            break;
                        case "pending":
                            Toast.makeText(OTPActivity.this, "Otp is Wrong \n Please Retry", Toast.LENGTH_SHORT).show();
                            break;
                        case "approved":
                            Toast.makeText(OTPActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("USER").child(user_id).updateChildren(hashMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                                    if(error == null){
                                        finish();
                                    }
                                }
                            });
                            break;
                    }
                }
            }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(OTPActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });



    }

}