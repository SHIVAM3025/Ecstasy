package in.ecstasy.app.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.ecstasy.app.Objects.Video;
import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ecstasy.app.MainActivity.ID_TOKEN;

/**
 * Created By Shivam Gupta on 17-06-2021 of package in.ecstasy.app.Home
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.PostViewHolder>{

    private static final String TAG = "HomeRecyclerAdapter";
    Context context;
    OnPostClickListener listener;
    List<Video> postList;
    MediaController mediaController;
    ApiInterface apiInterface;
    PostViewHolder viewHolder;

    public HomeRecyclerAdapter(Context context, OnPostClickListener onPostClickListener) {
        this.context = context;
        this.listener = onPostClickListener;
        postList = new ArrayList<>();
        mediaController = new MediaController(context);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull  HomeRecyclerAdapter.PostViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        viewHolder = holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  HomeRecyclerAdapter.PostViewHolder holder, int position) {
        Video video = postList.get(position);

        holder.artistName.setText(video.getName());
        holder.videoView.setVideoPath(video.getUrl());
        holder.videoView.start();
        holder.postTitle.setText(video.getTitle());
        holder.postDesc.setText(video.getDesc());
        holder.postViews.setText(video.getView());
        switch (video.getVideoStatus()) {
            case 0:
                break;
            case 1:
                holder.likePost.setImageDrawable(context.getDrawable(R.drawable.ic_liked));
                break;
            case 2:
                holder.dislikePost.setImageDrawable(context.getDrawable(R.drawable.ic_disliked));
                break;
        }
        holder.postLikes.setText(video.getLikes());
        holder.postDislikes.setText(video.getDislikes());
        holder.postShares.setText(video.getShares());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
   /* CommentRecyclerAdapter commentRecyclerAdapter;
    PostViewHolder viewHolder;*/

    public void updatePostList(List<Video>videoList) {
        for(Video video:videoList) {
            postList.add(postList.size(),video);
            notifyItemChanged(postList.size());
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        VideoView videoView;
        TextView artistName, postTitle, postDesc, postViews, postLikes, postDislikes, postShares;
        ImageButton likePost, dislikePost, sharePost;
        RecyclerView commentRecyclerView;
        TextView captionsHeading;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.post_artist_name_view);
            videoView = itemView.findViewById(R.id.post_video_view);
            postTitle = itemView.findViewById(R.id.post_title_view);
            postLikes = itemView.findViewById(R.id.post_like_count_view);
            postDislikes = itemView.findViewById(R.id.post_dislike_count_view);
            postShares = itemView.findViewById(R.id.post_share_count_view);
            postDesc = itemView.findViewById(R.id.post_desc_view);
            postViews = itemView.findViewById(R.id.post_viewsCount_view);
            sharePost = itemView.findViewById(R.id.post_share_btn);
            likePost = itemView.findViewById(R.id.post_like_btn);
            dislikePost = itemView.findViewById(R.id.post_dislike_btn);
            captionsHeading = itemView.findViewById(R.id.captionsHeading);
            commentRecyclerView = itemView.findViewById(R.id.comment_recycler_view);
            captionsHeading.setOnClickListener(this);
            artistName.setOnClickListener(this);
              sharePost.setOnClickListener(this);
            likePost.setOnClickListener(this);
            dislikePost.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.post_artist_name_view:
                    listener.OnArtistNameClick(getAdapterPosition());
                    break;
                case R.id.post_share_btn:
                    listener.OnShareClick(getAdapterPosition());
                    break;
                case R.id.post_like_btn:
                    listener.OnLikeClick(getAdapterPosition());
                    break;
                case R.id.post_dislike_btn:
                    listener.OnDislikeClick(getAdapterPosition());
                    break;
                case R.id.captionsHeading:
                    listener.OnCaptionsClick( postList.get(getAdapterPosition()).getId(),postList.get(getAdapterPosition()).getVnum() ,getAdapterPosition());
                    break;
            }
        }
    }

    public interface OnPostClickListener {
        void OnArtistNameClick(int position);
        void OnShareClick(int position);
        void OnLikeClick(int position);
        void OnDislikeClick(int position);
        void OnCaptionsClick(String id , String vnum , int position);
    }

    public void handleLikeDislikeView(int integer){
        if(integer == 0){
            viewHolder.likePost.setImageResource(R.drawable.ic_like);
            viewHolder.dislikePost.setImageResource(R.drawable.ic_dislike);
        } else if(integer == 1){
            viewHolder.likePost.setImageResource(R.drawable.ic_liked);
            viewHolder.dislikePost.setImageResource(R.drawable.ic_dislike);
        } else if(integer == 2){
            viewHolder.likePost.setImageResource(R.drawable.ic_like);
            viewHolder.dislikePost.setImageResource(R.drawable.ic_disliked);
        }
    }

    public void addVideoView(int position) {
        Call<Object> call = apiInterface.addVideoView(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "VideoView: " + response.body());
                if(response.body().equals("success")){
                    int currentViews = Integer.parseInt(postList.get(position).getView());
                    currentViews ++;
                    postList.get(position).setView(String.valueOf(currentViews));
                    viewHolder.postViews.setText(String.valueOf(currentViews));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "VideoViewRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    public void shareVideo(int position, String caption) {
        Call<Object> call = apiInterface.shareVideo(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum(), caption, getRandomString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "ShareVideo: " + response.body());
                if(response.body().equals("success")){
                    int currentShares = Integer.parseInt(postList.get(position).getShares());
                    currentShares ++;
                    postList.get(position).setShares(String.valueOf(currentShares));
                    viewHolder.postShares.setText(String.valueOf(currentShares));
                  //  commentRecyclerAdapter.updateCommentList();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "ShareVideoRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    private String getRandomString() {
        int n= 15;
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    public void checkLikesVideo(int position) {
        Call<Object> call = apiInterface.likesVideo(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "LikesVideo: " + response.body());
                if(response.body().equals(true)){
                    postList.get(position).setVideoStatus(1);
                    handleLikeDislikeView(1);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "LikesVideoRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    public void likeVideo(int position) {
        Call<Object> call = apiInterface.likeVideo(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "LikeVideo: " + response.body());
                if(response.body().equals("success")){
                    postList.get(position).setVideoStatus(1);
                    handleLikeDislikeView(1);
                    int currentLikes = Integer.parseInt(postList.get(position).getLikes());
                    currentLikes ++;
                    postList.get(position).setLikes(String.valueOf(currentLikes));
                    viewHolder.postLikes.setText(String.valueOf(currentLikes));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "LikeVideoRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    public void removeLikeFromVideo(int position){
        Call<Object> call = apiInterface.removeVideoLike(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "RemoveLikeFromVideo: " + response.body());
                if(response.body().equals("success")){
                    postList.get(position).setVideoStatus(0);
                    handleLikeDislikeView(0);
                    int currentLikes = Integer.parseInt(postList.get(position).getLikes());
                    currentLikes --;
                    postList.get(position).setLikes(String.valueOf(currentLikes));
                    viewHolder.postLikes.setText(String.valueOf(currentLikes));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "RemoveLikeFromVideo: " + t.getLocalizedMessage() );
            }
        });
    }

    public void flipToLike(int position){
        Call<Object> call = apiInterface.removeVideoDislike(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "RemoveDislikeFromVideo: " + response.body());
                if(response.body().equals("success")){
                    postList.get(position).setVideoStatus(0);
                    handleLikeDislikeView(0);
                    int currentDislikes = Integer.parseInt(postList.get(position).getDislikes());
                    currentDislikes --;
                    postList.get(position).setDislikes(String.valueOf(currentDislikes));
                    viewHolder.postDislikes.setText(String.valueOf(currentDislikes));
                    likeVideo(position);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "RemoveDislikeFromVideoRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    public void checkDislikesVideo(int position) {
        Call<Object> call = apiInterface.dislikesVideo(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "DislikesVideo: " + response.body());
                if(response.body().equals(true)){
                    postList.get(position).setVideoStatus(2);
                    handleLikeDislikeView(2);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "DislikesVideoRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    public void dislikeVideo(int position) {
        Call<Object> call = apiInterface.dislikeVideo(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "DislikeVideo: " + response.body());
                if(response.body().equals("success")){
                    postList.get(position).setVideoStatus(2);
                    handleLikeDislikeView(2);
                    int currentDislikes = Integer.parseInt(postList.get(position).getDislikes());
                    currentDislikes ++;
                    postList.get(position).setDislikes(String.valueOf(currentDislikes));
                    viewHolder.postDislikes.setText(String.valueOf(currentDislikes));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "DislikeVideoRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    public void removeDislikeFromVideo(int position){
        Call<Object> call = apiInterface.removeVideoDislike(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "RemoveDislikeFromVideo: " + response.body());
                if(response.body().equals("success")){
                    postList.get(position).setVideoStatus(0);
                    handleLikeDislikeView(0);
                    int currentDislikes = Integer.parseInt(postList.get(position).getDislikes());
                    currentDislikes --;
                    postList.get(position).setDislikes(String.valueOf(currentDislikes));
                    viewHolder.postDislikes.setText(String.valueOf(currentDislikes));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "RemoveDislikeFromVideoRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    public void flipToDislike(int position){
        Call<Object> call = apiInterface.removeVideoLike(ID_TOKEN, postList.get(position).getId(), postList.get(position).getVnum()) ;
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "RemoveLikeFromVideo: " + response.body());
                if(response.body().equals("success")){
                    postList.get(position).setVideoStatus(0);
                    handleLikeDislikeView(0);
                    int currentLikes = Integer.parseInt(postList.get(position).getLikes());
                    currentLikes --;
                    postList.get(position).setLikes(String.valueOf(currentLikes));
                    viewHolder.postLikes.setText(String.valueOf(currentLikes));
                    dislikeVideo(position);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "RemoveLikeFromVideo: " + t.getLocalizedMessage() );
            }
        });
    }


}
