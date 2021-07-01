package in.ecstasy.app.Notification;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;

public class NotificationFragment extends Fragment implements NotificationItemRecyclerViewAdapter.onNotificationClickListener{

    private NotificationItemRecyclerViewAdapter notificationItemRecyclerViewAdapter;
    private RecyclerView recyclerView;
    Context context;
    ApiInterface apiInterface;

    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notification, container, false);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        context = getContext();
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationItemRecyclerViewAdapter = new NotificationItemRecyclerViewAdapter(context ,this);
        recyclerView.setAdapter(notificationItemRecyclerViewAdapter);

        return v;
    }

    @Override
    public void OnNotificationClick(int position) {

        Toast.makeText(getContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();

    }
}