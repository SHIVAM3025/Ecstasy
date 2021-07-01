package in.ecstasy.app.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.ecstasy.app.Objects.Notification;
import in.ecstasy.app.R;

/**
 * Created By Shivam Gupta on 23-06-2021 of package in.ecstasy.app.Notification
 */
public class NotificationItemRecyclerViewAdapter extends RecyclerView.Adapter<NotificationItemRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private onNotificationClickListener mListener;
    NotificationViewModel notificationViewModel;

    public NotificationItemRecyclerViewAdapter(Context context,  onNotificationClickListener mListener) {
        notificationList = new ArrayList<>();
        this.context = context;
        this.mListener = mListener;
        notificationViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NotificationViewModel.class);
        notificationViewModel.getNotificationList().observe((LifecycleOwner) context, new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                notificationList = notifications;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.heading.setText(notification.getHeader());
        holder.body.setText(notification.getBody());
        //holder.timestamp.setText(notification.getDate());
        holder.timestamp.setText("a moments ago");

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading;
        TextView body;
        TextView timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            body = itemView.findViewById(R.id.body);
            timestamp = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnNotificationClick(getAdapterPosition());
                }
            });

        }
    }


    public interface onNotificationClickListener {
        void OnNotificationClick(int position);
    }


}
