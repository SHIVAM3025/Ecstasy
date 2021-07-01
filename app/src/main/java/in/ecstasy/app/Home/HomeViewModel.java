package in.ecstasy.app.Home;

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
 * Created By Shivam Gupta on 17-06-2021 of package in.ecstasy.app.Home
 */
public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    MutableLiveData<List<Video>> mutablePosts;
    private final ApiInterface apiInterface;


    public HomeViewModel () {
        super();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public LiveData<List<Video>> getPosts(String exploreUserId) {
        if(mutablePosts == null){
            mutablePosts = new MutableLiveData<>();
            if(exploreUserId == null) {
                timelinePostList();
            } else {
                explorePostList(exploreUserId);
            }
        }
        return mutablePosts;
    }

    public void timelinePostList() {
        Call<List<Video>> call = apiInterface.getTimelineVideos(ID_TOKEN) ;
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                Log.d(TAG, "TimelinePosts: " + response.body());
                if(response.isSuccessful() && response.body() !=null){
                    mutablePosts.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "TimelinePostsRequest: " + t.getLocalizedMessage() );
            }
        });
    }

    public void explorePostList(String userId) {
        Call<List<Video>> call = apiInterface.getUserSpecificVideos(ID_TOKEN, userId) ;
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                Log.d(TAG, "ExplorePosts: " + response.body());
                if(response.isSuccessful() && response.body() !=null){
                    mutablePosts.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "ExplorePostsRequest: " + t.getLocalizedMessage() );
            }
        });
    }



}
