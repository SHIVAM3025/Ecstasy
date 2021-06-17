package in.ecstasy.app.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created By Shivam Gupta on 15-06-2021 of package in.ecstasy.app.Retrofit
 */
public class ApiClient {


    private static final String BASE_URL = "https://us-central1-theatronfinal.cloudfunctions.net/app/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if(retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


}
