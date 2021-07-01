package in.ecstasy.app.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.ecstasy.app.Objects.Video;
import in.ecstasy.app.R;

/**
 * Created By Shivam Gupta on 23-06-2021 of package in.ecstasy.app.Search
 */
public class SearchVideoRecyclerAdapter extends RecyclerView.Adapter<SearchVideoRecyclerAdapter.SearchVideoViewHolder>{

    Context context;
    List<Video> videoList;
    List<Video> filteredVideoList;
    OnVideoClickListener mListener;
    SearchVideoViewModel searchVideoViewModel;

    public SearchVideoRecyclerAdapter(Context context, OnVideoClickListener listener) {
        videoList = new ArrayList<>();
        filteredVideoList = new ArrayList<>();
        this.context = context;
        this.mListener = listener;
        searchVideoViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SearchVideoViewModel.class);
        searchVideoViewModel.getVideoList().observe((LifecycleOwner) context, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                Collections.shuffle(videos);
                filteredVideoList = videos;
                videoList = videos;
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public SearchVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_grid_layout,parent,false);
        return new SearchVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVideoViewHolder holder, int position) {
        Glide.with(context).load(filteredVideoList.get(position).getThumbnailphoto()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return filteredVideoList.size();
    }

    public void searchVideos(String filter){
        searchVideoViewModel.findVideoList(filter);
    }


    public class SearchVideoViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public SearchVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.grid_image_view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnImageClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnVideoClickListener {
        void OnImageClick(int position);
    }


}
