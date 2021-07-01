package in.ecstasy.app.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.ecstasy.app.ExploreActivity;
import in.ecstasy.app.Objects.User;
import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestActivity extends AppCompatActivity implements FriendRequestRecyclerAdapter.OnClickFriendRequest{

    private static final String TAG = "FriendRequestActivity";
    RecyclerView recyclerView;
    FriendRequestRecyclerAdapter adapter;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView = findViewById(R.id.friend_request_recycler_view);
        adapter = new FriendRequestRecyclerAdapter(this, (FriendRequestRecyclerAdapter.OnClickFriendRequest) this);
        recyclerView.setAdapter(adapter);
        getReceivedFriendRequests();

    }

    private void getReceivedFriendRequests() {
        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN",null);
        String TYPE = "R";
        Call<List<User>> call = apiInterface.getFriendRequests(idToken, TYPE);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "FriendRequests: " + response.body());
                    adapter.updateRequestsList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "FriendRequestsRequest: " + t.getLocalizedMessage());
            }
        });

    }


    @Override
    public void onNameClick(int position) {
        Intent intent = new Intent(FriendRequestActivity.this, ExploreActivity.class);
        intent.putExtra("userId", adapter.friendRequestsList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onAcceptClick(int position) {
        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN",null);
        Call<Object>call = apiInterface.acceptFriendRequest(idToken, adapter.friendRequestsList.get(position).getId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "AcceptFriendRequest: "+response.body());
                if(response.body().toString().equals("success")) {
                    adapter.friendRequestsList.remove(position);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d(TAG, "AcceptFriendRequest: "+t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onDenyClick(int position) {
        String idToken = getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN",null);
        Call<Object>call = apiInterface.denyFriendRequest(idToken, adapter.friendRequestsList.get(position).getId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "DenyFriendRequest: "+response.body());
                if(response.body().toString().equals("success")) {
                    adapter.friendRequestsList.remove(position);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d(TAG, "DenyFriendRequest: "+t.getLocalizedMessage());
            }
        });
    }
}