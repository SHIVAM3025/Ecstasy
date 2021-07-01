package in.ecstasy.app.Search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.ecstasy.app.Objects.Video;
import in.ecstasy.app.Retrofit.ApiClient;
import in.ecstasy.app.Retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ecstasy.app.MainActivity.ID_TOKEN;

/**
 * Created By Shivam Gupta on 23-06-2021 of package in.ecstasy.app.Search
 */
public class SearchVideoViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";
    private MutableLiveData<List<Video>> mutableLiveData;
    private final ApiInterface apiInterface;

    public SearchVideoViewModel() {
        super();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public LiveData<List<Video>> getVideoList() {
        if(mutableLiveData == null){
            mutableLiveData = new MutableLiveData<>();
            initVideoList();
        }
        return mutableLiveData;
    }

    private void initVideoList() {
        Call<List<Video>> call = apiInterface.getAllVideos(ID_TOKEN);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "AllVideos: " + response.body());
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "AllVideosRequest" + t.getLocalizedMessage());
            }
        });
    }

    public void findVideoList(String filter) {
        Call<List<Video>> call = apiInterface.getFilteredVideos(ID_TOKEN, filter);
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
        });
    }


}
