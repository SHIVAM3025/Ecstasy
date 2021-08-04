package in.ecstasy.app.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.ecstasy.app.Objects.Video;
import in.ecstasy.app.R;

/**
 * Created By Shivam Gupta on 28-06-2021 of package in.ecstasy.app.Profile
 */
public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileViewHolder>{

    OnVideoClickListener mListener;
    Context context;
    List<Video> videoList;

    public ProfileRecyclerAdapter(Context context, OnVideoClickListener listener) {
        mListener = listener;
        this.context = context;
        videoList = new ArrayList<>();
    }

    public void updateVideoList(List<Video> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    public Video getVideo(int psoition){
        return videoList.get(psoition);
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_grid_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Glide.with(context).load(videoList.get(position).getThumbnailphoto()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ProfileViewHolder(@NonNull View itemView) {
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
