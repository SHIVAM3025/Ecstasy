package in.ecstasy.app.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.ecstasy.app.Objects.User;
import in.ecstasy.app.R;

/**
 * Created By Shivam Gupta on 28-06-2021 of package in.ecstasy.app.Profile
 */
public class FriendRequestRecyclerAdapter extends RecyclerView.Adapter<FriendRequestRecyclerAdapter.FriendRequestViewHolder>{


    Context context;
    List<User> friendRequestsList;
    OnClickFriendRequest onClickFriendRequest;

    public FriendRequestRecyclerAdapter(Context context, OnClickFriendRequest onClickFriendRequest) {
        this.context = context;
        friendRequestsList = new ArrayList<>();
        this.onClickFriendRequest = onClickFriendRequest;
    }

    public void updateRequestsList(List<User> requestsList) {
        friendRequestsList = requestsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request,parent,false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        holder.nameView.setText(friendRequestsList.get(position).getName());
        Glide.with(context).load(friendRequestsList.get(position).getPhotourl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return friendRequestsList.size();
    }

    public class FriendRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageButton acceptBtn, denyBtn;
        TextView nameView;
        ImageView imageView;
        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.friend_request_name_view);
            imageView = itemView.findViewById(R.id.friend_request_photo_view);
            acceptBtn = itemView.findViewById(R.id.friend_request_accept_btn);
            denyBtn = itemView.findViewById(R.id.friend_request_deny_btn);
            nameView.setOnClickListener(this);
            denyBtn.setOnClickListener(this);
            acceptBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.friend_request_name_view:
                    onClickFriendRequest.onNameClick(getAdapterPosition());
                    break;
                case R.id.friend_request_accept_btn:
                    onClickFriendRequest.onAcceptClick(getAdapterPosition());
                    break;
                case R.id.friend_request_deny_btn:
                    onClickFriendRequest.onDenyClick(getAdapterPosition());
                    break;
            }
        }
    }
    public interface OnClickFriendRequest {
        void onNameClick(int position);
        void onAcceptClick(int position);
        void onDenyClick(int position);
    }


}
