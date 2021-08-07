package in.ecstasy.app.People;

import static in.ecstasy.app.MainActivity.currentUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.ecstasy.app.Objects.Friend;
import in.ecstasy.app.Objects.User;
import in.ecstasy.app.R;

/**
 * Created By Shivam Gupta on 23-06-2021 of package in.ecstasy.app.People
 */
public class PeopleItemRecyclerViewAdapter extends RecyclerView.Adapter<PeopleItemRecyclerViewAdapter.ViewHolder>{


    List<User> peopleList, requestSentPeopleList;
    Context context;
    OnPeopleClick onPeopleClick;
    ViewHolder viewHolder;
    Boolean state;

    public PeopleItemRecyclerViewAdapter(Context context, OnPeopleClick onPeopleClick, Boolean state) {
        this.context = context;
        this.onPeopleClick = onPeopleClick;
        peopleList = new ArrayList<>();
        this.state = state;
    }

    public void updatePeopleList(List<User>peopleList) {
        this.peopleList = peopleList;
        notifyDataSetChanged();
    }

    public void updateRequestSentPeopleList(List<User> userList) {
        this.requestSentPeopleList = userList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_people_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        this.viewHolder = holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       // Toast.makeText(context,"URL " + peopleList.get(position).getPhotourl(), Toast.LENGTH_SHORT).show();
        Glide.with(context).load(peopleList.get(position).getPhotourl()).into(holder.personPhoto);
        holder.personName.setText(peopleList.get(position).getName());
        if(currentUser.getFriends() != null) {
            for (Map.Entry<String, Friend> entry : currentUser.getFriends().entrySet()) {
                if (entry.getValue().getId().equals(peopleList.get(position).getId())) {
                    holder.friendStatus.setVisibility(View.INVISIBLE);
                }
            }
        }

        for(User user:requestSentPeopleList) {
            if(user.getId().equals(peopleList.get(position).getId())) {
                peopleList.get(position).setChecked(true);
                holder.friendStatus.setChecked(true);
            }
            }
        }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        SwitchMaterial friendStatus;
        TextView personName;
        ImageView personPhoto;
        public ViewHolder(View view) {
            super(view);
            personName = view.findViewById(R.id.people_name);
            friendStatus = view.findViewById(R.id.people_friend_switch);
            personPhoto = view.findViewById(R.id.people_photo_view);
            personName.setOnClickListener(this);
            friendStatus.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.people_name:
                    onPeopleClick.onNameClick(getAdapterPosition());
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            onPeopleClick.onSwitchClick(getAdapterPosition() , b , peopleList.get(getAdapterPosition()).getId());
        }
    }

    public interface OnPeopleClick {
        void onNameClick(int position);
        void onSwitchClick(int position , Boolean b , String uid);
    }


}
