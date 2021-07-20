package in.ecstasy.app.People;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import in.ecstasy.app.Objects.User;
import in.ecstasy.app.R;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class PeopleFragment extends Fragment implements PeopleItemRecyclerViewAdapter.OnPeopleClick{

    private static final String TAG = "PeopleFragment";
    RecyclerView recyclerView;
    PeopleItemRecyclerViewAdapter adapter;
    Context context;
    ApiInterface apiInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView = getView().findViewById(R.id.people_recycler_view);
        adapter = new PeopleItemRecyclerViewAdapter(context, (PeopleItemRecyclerViewAdapter.OnPeopleClick) this);
        recyclerView.setAdapter(adapter);
        getContactsList();
    }

    private void getContactsList() {
        StringBuilder phoneNums = new StringBuilder();
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null, null);
        while (phones.moveToNext())
        {
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(phoneNums.toString().equals("")) {
                phoneNums.append(phoneNumber);
            }else {
                phoneNums.append(",").append(phoneNumber);
            }
        }
        phones.close();
        String contacts = phoneNums.toString();
        contacts = contacts.replace("+91", "");
        contacts = contacts.replace("+", "");
        contacts = contacts.replace(" ", "");
        contacts = contacts.replace("-", "");
        Log.d(TAG, "ContactsList: " + contacts);
        getPossibleFriends(contacts);
    }

    private void getSentFriendRequests(List<User> possibleFriends) {
        String idToken = context.getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN",null);
        String TYPE = "S";
        Call<List<User>> call = apiInterface.getFriendRequests(idToken, TYPE);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "FriendRequests: " + response.body());
                    adapter.updateRequestSentPeopleList(response.body());
                    for(User user: response.body()) {
                        possibleFriends.add(user);
                    }
                    adapter.updatePeopleList(possibleFriends);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "FriendRequestsRequest: " + t.getLocalizedMessage());
            }
        });

    }

    private void getPossibleFriends(String phoneNumbers) {
        String idToken = getActivity().getSharedPreferences("Ecstasy", MODE_PRIVATE).getString("ID_TOKEN", null);
        String testing = phoneNumbers;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phonenumbers" , testing);

        apiInterface.possibleFriends(idToken ,jsonObject).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG , response.toString());
                if(response.isSuccessful()){
                    getSentFriendRequests(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG , "Error: " + t.toString());
            }
        });

    /*
        Call<List<User>> call = apiInterface.possibleFriends(phoneNumbers);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG, "PossibleFriends: "+ response.body());
                if(response.isSuccessful()) {
                    //getSentFriendRequests(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "PossibleFriendsRequest: " + t.getLocalizedMessage());
            }
        });*/
    }


    @Override
    public void onNameClick(int position) {



    }

    @Override
    public void onSwitchClick(int position) {

        

    }
}