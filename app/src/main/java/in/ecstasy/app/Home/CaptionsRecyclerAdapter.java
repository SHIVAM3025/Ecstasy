package in.ecstasy.app.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ecstasy.app.Objects.Comment;
import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;

/**
 * Created By Shivam Gupta on 29-07-2021 of package in.ecstasy.app.Home
 */

/*
* Captions Recycler Adapter used for getting data from the Apis from server.
*
* */
public class CaptionsRecyclerAdapter extends RecyclerView.Adapter<CaptionsRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final List<Comment> commentList;
    ApiInterface apiInterface;

    public CaptionsRecyclerAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.captions_item_layout, parent, false);
        return new CaptionsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        Comment comment = commentList.get(position);
        //holder.username.setText(comment.getCommentID());
        holder.caption.setText(comment.getComments());
        holder.post_dislike_count.setText(comment.getDislikes());
        holder.post_like_count.setText(comment.getLikes());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView username, caption;
        TextView post_like_count, post_dislike_count;
        ImageView like_btn, dislike_btn;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.artist_image);
            username = itemView.findViewById(R.id.username);
            caption = itemView.findViewById(R.id.caption);
            post_like_count = itemView.findViewById(R.id.post_like_count_view);
            post_dislike_count = itemView.findViewById(R.id.post_dislike_count_view);
            like_btn = itemView.findViewById(R.id.post_like_btn);
            dislike_btn = itemView.findViewById(R.id.post_dislike_btn);
        }
    }

}
