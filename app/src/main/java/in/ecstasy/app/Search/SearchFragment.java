package in.ecstasy.app.Search;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import in.ecstasy.app.ExploreActivity;
import in.ecstasy.app.Objects.Video;
import in.ecstasy.app.R;

import static in.ecstasy.app.MainActivity.currentUser;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, SearchVideoRecyclerAdapter.OnVideoClickListener {

    private static final String TAG = "Search";
    SearchVideoViewModel searchVideoViewModel;
    SearchView searchView;
    Context context;
    private SearchVideoRecyclerAdapter searchVideoRecyclerAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        recyclerView = getView().findViewById(R.id.search_recycler_view);
        searchView = getView().findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(this);
        searchView.setQuery("", false);
        showVideos();
    }


    private void showVideos() {
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        searchVideoRecyclerAdapter = new SearchVideoRecyclerAdapter(getContext(), this);
        recyclerView.setAdapter(searchVideoRecyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if (!s.equals("")) {
            //searchVideoRecyclerAdapter.getFilter().filter(s);
            searchVideoRecyclerAdapter.searchVideos(s);
        }
        return false;
    }

    @Override
    public void OnImageClick(int position) {
        Video video = searchVideoRecyclerAdapter.filteredVideoList.get(position);
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.video_dialog_layout);
        ImageView artistImage = dialog.findViewById(R.id.dialog_artist_photo_view);
        TextView artistName = dialog.findViewById(R.id.dialog_artist_name_view);
        TextView videoTitle = dialog.findViewById(R.id.dialog_videoTitle);
        VideoView videoView = dialog.findViewById(R.id.dialog_videoView);
        Glide.with(context).load(video.getPhoto()).into(artistImage);
        videoView.setVideoPath(video.getUrl());
        videoView.start();
        artistName.setText(video.getName());
        videoTitle.setText(video.getTitle());
        artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video.getId() == currentUser.getId()) return;
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtra("userId", video.getId());
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}