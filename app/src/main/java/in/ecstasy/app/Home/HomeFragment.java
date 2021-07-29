package in.ecstasy.app.Home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Collections;
import java.util.List;

import in.ecstasy.app.ExploreActivity;
import in.ecstasy.app.Objects.Video;
import in.ecstasy.app.R;

import static in.ecstasy.app.MainActivity.currentUser;

public class HomeFragment extends Fragment implements HomeRecyclerAdapter.OnPostClickListener{

    private static final String TAG = "HomeFragment";
    private RecyclerView home_view_recylerView;
    private HomeRecyclerAdapter recyclerAdapter;
    private HomeViewModel homeViewModel;
    private Context context;
    String exploreUserId;

    public static HomeFragment newInstance(String exploreUserId) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("ExploreUserId", exploreUserId);
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            exploreUserId = getArguments().getString("ExploreUserId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        home_view_recylerView = getView().findViewById(R.id.home_view_recylerView);
        home_view_recylerView.setLayoutManager(new LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL , false));
       // home_view_recylerView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        recyclerAdapter = new HomeRecyclerAdapter(context,this);
        home_view_recylerView.setAdapter(recyclerAdapter);
        homeViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(HomeViewModel.class);
        homeViewModel.getPosts(exploreUserId).observe((LifecycleOwner) context, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videoList) {
                Collections.shuffle(videoList);
                recyclerAdapter.updatePostList(videoList);
            }
        });
    }

    public void handleNameClick(String id) {
        if(id == currentUser.getId()) return;
        Intent intent = new Intent(context, ExploreActivity.class);
        intent.putExtra("userId", id);
        //intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }


    @Override
    public void OnArtistNameClick(int position) {
        handleNameClick(recyclerAdapter.postList.get(position).getId());
    }

    @Override
    public void OnShareClick(int position) {
        showCaptionUploadDialog(position);
    }

    private void showCaptionUploadDialog(int position) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.caption_dialog_layout);
        TextInputLayout inputLayout = dialog.findViewById(R.id.caption_editText_view);
        Button uploadBtn = dialog.findViewById(R.id.caption_upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputLayout.getEditText().getText().toString().trim().equals("")){
                    Toast.makeText(getContext(), "Type Something", Toast.LENGTH_SHORT).show();
                }else{
                    recyclerAdapter.shareVideo(position, inputLayout.getEditText().getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void OnLikeClick(int position) {
        int status = recyclerAdapter.postList.get(position).getVideoStatus();
        //Toast.makeText(getContext(), "Like Clicked " + status, Toast.LENGTH_SHORT).show();
        if(status == 0){
            recyclerAdapter.likeVideo(position);
        }else if(status == 2){
            recyclerAdapter.flipToLike(position);
        } else {
            recyclerAdapter.removeLikeFromVideo(position);
        }
    }

    @Override
    public void OnDislikeClick(int position) {
        int status = recyclerAdapter.postList.get(position).getVideoStatus();
        //Toast.makeText(getContext(), "Dislike Clicked " + status, Toast.LENGTH_SHORT).show();
        if(status == 0){
            recyclerAdapter.dislikeVideo(position);
        }else if(status == 1){
            recyclerAdapter.flipToDislike(position);
        }else{
            recyclerAdapter.removeDislikeFromVideo(position);
        }
    }

}