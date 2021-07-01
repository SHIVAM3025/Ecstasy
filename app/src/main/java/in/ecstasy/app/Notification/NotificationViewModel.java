package in.ecstasy.app.Notification;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.ecstasy.app.Objects.Notification;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ecstasy.app.MainActivity.ID_TOKEN;

/**
 * Created By Shivam Gupta on 24-06-2021 of package in.ecstasy.app.Notification
 */
public class NotificationViewModel extends ViewModel {

    private static final String TAG = "NotificationViewModel";
    private MutableLiveData<List<Notification>> mutableLiveData;
    private final ApiInterface apiInterface;


    public NotificationViewModel() {
        super();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }


    public LiveData<List<Notification>> getNotificationList() {
        if(mutableLiveData == null){
            mutableLiveData = new MutableLiveData<>();
            initNotificationList();
        }
        return mutableLiveData;
    }

    private void initNotificationList() {

        //TODO change the video interface to notifications
        Call<List<Notification>> call = apiInterface.getAllNotifications(ID_TOKEN);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "AllVideos: " + response.body());
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e(TAG, "AllVideosRequest" + t.getLocalizedMessage());
            }
        });

    }

    public void findVideoList(String filter) {
/*        Call<List<Video>> call = apiInterface.getFilteredVideos(ID_TOKEN, filter);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "FilteredVideos: " + response.body());
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "FilteredVideosRequest" + t.getLocalizedMessage());
            }
        });*/
    }


}
