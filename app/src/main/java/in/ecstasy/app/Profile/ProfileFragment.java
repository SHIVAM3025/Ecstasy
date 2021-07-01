package in.ecstasy.app.Profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ecstasy.app.Objects.Video;
import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static in.ecstasy.app.MainActivity.ID_TOKEN;
import static in.ecstasy.app.MainActivity.currentUser;


public class ProfileFragment extends Fragment implements ProfileRecyclerAdapter.OnVideoClickListener{

    private static final String TAG = "ProfileFragment";
    Context context;
    ApiInterface apiInterface;
    View v;
    BottomSheetDialog bottomSheetDialog;
    ProfileRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    CircleImageView circleImageView;
    TextView name, username, shares, admiring;
    ImageView upload_video_btn;
    ImageView moreBtn;
    private final int REQUEST_UPLOAD_VIDEO = 173;
    private final int SELECT_VIDEO = 768;
    private static final int READ_PERMISSION_CODE = 1;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v= inflater.inflate(R.layout.fragment_profile, container, false) ;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        context = getContext();
        circleImageView = v.findViewById(R.id.profile_photo_view);
        recyclerView = v.findViewById(R.id.profile_recycler_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        recyclerAdapter = new ProfileRecyclerAdapter(context, (ProfileRecyclerAdapter.OnVideoClickListener) this);
        recyclerView.setAdapter(recyclerAdapter);
        name = v.findViewById(R.id.profile_name_view);
        username = v.findViewById(R.id.profile_userName_view);
        shares = v.findViewById(R.id.profile_share_count_view);
        admiring = v.findViewById(R.id.profile_admiring_count_view);
        moreBtn = v.findViewById(R.id.profile_more_button);
        upload_video_btn = v.findViewById(R.id.profile_video_add_btn);
        initBottomSheet();

        if(currentUser.getType().toLowerCase().equals("artist")){
            upload_video_btn.setVisibility(View.VISIBLE);
        }
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
        upload_video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadVideo();
            }
        });

        Glide.with(context).load(currentUser.getPhotourl()).into(circleImageView);
        name.setText(currentUser.getName());
        username.setText("@"+currentUser.getUsername());
        shares.setText(currentUser.getSharescount());
        admiring.setText(currentUser.getAdmirerscount());
     /*   if(currentUser.getAdmiring() != null) {
            admiring.setText(String.valueOf(currentUser.getAdmiring().size()));
        }*/

        updateVideoList();

        return v;
    }


    private void uploadVideo() {
        checkForPermissions();
    }

    private void checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(getContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //If permission is granted
                promptVideoUploadOptions();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
            }
        } else {
            //no need to check permissions in android versions lower then marshmallow
            promptVideoUploadOptions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    promptVideoUploadOptions();
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void promptVideoUploadOptions() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Video "), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                Uri selectedVideoUri = data.getData();
                if(!checkVideoDuration(selectedVideoUri)) return;
                Intent intent = new Intent(context, UploadVideoActivity.class);
                intent.putExtra("VideoPath", selectedVideoUri);
                startActivity(intent);
            }
        }
    }

    private boolean checkVideoDuration(Uri selectedVideoUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context,selectedVideoUri);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        long timeInMillisec = Long.parseLong(time);
        if(timeInMillisec/60000 >5) {
            Toast.makeText(context, "Video Duration exceeding 5 minutes", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.profile_more_layout);
        bottomSheetDialog.findViewById(R.id.profile_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://ecstasystage.com/";
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url)));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.findViewById(R.id.profile_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.ecstasystage.com/community-guidelines";
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url)));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.findViewById(R.id.profile_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.ecstasystage.com/privacy-policy";
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url)));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.findViewById(R.id.profile_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, EditProfileActivity.class));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.findViewById(R.id.profile_friendRequests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, FriendRequestActivity.class));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.findViewById(R.id.profile_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(ID_TOKEN);
                FirebaseAuth.getInstance().signOut();
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void updateVideoList() {
        Call<List<Video>> call = apiInterface.getUserSpecificVideos(ID_TOKEN, currentUser.getId());
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "UserSpecificVideos: " + response.body());
                  //  recyclerAdapter.updateVideoList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "UserSpecificVideosRequest" + t.getLocalizedMessage());
            }
        });
    }


    @Override
    public void OnImageClick(int position) {

    }
}