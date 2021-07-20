package in.ecstasy.app.Profile;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
import com.abedelazizshe.lightcompressorlibrary.config.Configuration;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class UploadVideoActivity extends AppCompatActivity {

    private static final String TAG = "UploadVideoActivity";
    TextInputLayout postTitle, description;
    Button uploadBtn;
    Uri videoUri;
    String uid;
    VideoView videoView;
    ProgressBar progressBar;
    MediaController mediaController;
    ApiInterface apiInterface;
    String fileName = "compressedVideo.mp4";
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        postTitle = findViewById(R.id.upload_title_view);
        description = findViewById(R.id.upload_desc_view);
        videoView = findViewById(R.id.upload_video_view);
        progressBar = findViewById(R.id.upload_progress_bar);
        uploadBtn = findViewById(R.id.video_upload_btn);
        videoUri = (Uri) getIntent().getExtras().get("VideoPath");
        uid = (String) getIntent().getExtras().get("uid");
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        compressVideo();

    }

    private void compressVideo() {
        File dir = getFilesDir();
        file = new File(dir, fileName);
        try {
            file.createNewFile();
            Log.d(TAG, "compressedVideo: New File " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        VideoCompressor.start(this, videoUri, null, file.getAbsolutePath(), new CompressionListener() {
            @Override
            public void onStart() {
                Log.d(TAG, "VideoCompression: Started");
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "VideoCompression: Completed");
                progressBar.setVisibility(View.INVISIBLE);
                uploadBtn.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.fromFile(file));
                videoView.setMediaController(mediaController);
                videoView.start();
            }

            @Override
            public void onFailure(String failureMessage) {
                Log.e(TAG, "VideoCompression: " + failureMessage);
            }

            @Override
            public void onProgress(float v) {
                Log.d(TAG, "VideoCompression: Progress " + v);

            }

            @Override
            public void onCancelled() {
                Log.d(TAG, "VideoCompression: Cancelled");

            }
        }, new Configuration(
                VideoQuality.MEDIUM,
                false,
                false,
                null /*videoHeight: double, or null*/,
                null /*videoWidth: double, or null*/,
                null /*videoBitrate: int, or null*/
        ));

    }


    public void handleCancelPress(View view) {
        finish();
    }

    public void handleUploadPress(View view) {
        if (postTitle.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.getEditText().getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Uploading..", Toast.LENGTH_SHORT).show();
        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN", null);
        RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), file);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Videos").child(uid).child(System.currentTimeMillis() + " " + ".mp4");
        UploadTask uploadTask = storageReference.putFile(Uri.fromFile(file));

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUrl = task.getResult() ;
                    uploadVideo(idToken,downloadUrl.toString());
                   // finish();

                }
            }
        });


      /*  MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), postTitle.getEditText().getText().toString().trim());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), description.getEditText().getText().toString().trim());*/
/*
        Call<okhttp3.ResponseBody> call = apiInterface.uploadVideo(idToken , requestBody , title , desc);

        call.enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                try {
                    Log.d(TAG, "VideoUpload: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(UploadVideoActivity.this, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Log.e(TAG, "VideoUploadRequest: " + t.getLocalizedMessage());
                Toast.makeText(UploadVideoActivity.this, "Video Upload Failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });*/




    }


    public void uploadVideo( String idToken, String url){
                Call<okhttp3.ResponseBody> call = apiInterface.uploadVideo(idToken , url , postTitle.getEditText().getText().toString()
                , description.getEditText().getText().toString());

        call.enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {

                try {
                    Log.d(TAG, "VideoUpload: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(UploadVideoActivity.this, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Log.e(TAG, "VideoUploadRequest: " + t.getLocalizedMessage());
                Toast.makeText(UploadVideoActivity.this, "Video Upload Failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}