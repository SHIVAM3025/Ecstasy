package in.ecstasy.app.Retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import in.ecstasy.app.Objects.Comment;
import in.ecstasy.app.Objects.Notification;
import in.ecstasy.app.Objects.User;
import in.ecstasy.app.Objects.Video;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created By Shivam Gupta on 15-06-2021 of package in.ecstasy.app.Retrofit
 */
public interface ApiInterface {

    @POST("login")
    Call<Object> registerUser(@Header("Authorization") String idToken, @Header("name") String name,
                              @Header("type") String type, @Header("photourl") String photourl);

    @POST("profile")
    Call<User> getCurrentUserProfile(@Header("Authorization") String idToken);

    @POST("profile/friends")
    Call<List<User>> getFriends(@Header("Authorization") String idToken);

    @POST("profile/friend-requests")
    Call<List<User>> getFriendRequests(@Header("Authorization") String idToken, @Header("type") String type);

    @POST("profile/be-friend")
    Call<User> sendFriendRequest(@Header("Authorization") String idToken, @Header("friend") String friendId);

    @POST("profile/delete-friend")
    Call<String> deleteFriendRequest(@Header("Authorization") String idToken, @Header("friend") String friendId);

    @POST("profile/accept-friend")
    Call<Object> acceptFriendRequest(@Header("Authorization") String idToken, @Header("friend") String friendId);

    @POST("profile/deny-friend")
    Call<Object> denyFriendRequest(@Header("Authorization") String idToken, @Header("friend") String friendId);

    @POST("profile/can-be-friends")
  Call<List<User>> possibleFriends(@Header("Authorization") String idToken, @Body JsonObject phoneNumbers);
   // Call<String> possibleFriends(/*@Header("Authorization") String idToken,*/ @Header("phonenumbers") String phoneNumbers);

    @POST("profile/edit")
    Call<Object> editProfile(@Header("Authorization") String idToken, @Header("") String name,
                             @Header("username") String userName, @Header("bio") String bio,
                             @Header("number") String number, @Header("web") String web);

    @POST("profile/admire")
    Call<Object> admireArtist(@Header("Authorization") String idToken, @Header("user") String userId);

    @POST("profile/remove-admire")
    Call<Object> unAdmireArtist(@Header("Authorization") String idToken, @Header("user") String userId);

    @POST("profile/all-video")
    Call<List<Video>> getAllVideos(@Header("Authorization") String idToken);

    @POST("/profile/notifications")
    Call<List<Notification>> getAllNotifications(@Header("Authorization") String idToken);

    @POST("profile/thumbnail")
    Call<List<Video>> getTimelineVideos(@Header("Authorization") String idToken);

    @POST("profile/videos-from-name")
    Call<List<Video>> getFilteredVideos(@Header("Authorization") String idToken, @Header("text") String filter);


    @POST("profile/videos-from-user")
    Call<List<Video>> getUserSpecificVideos(@Header("Authorization") String idToken, @Header("user_identifier") String userId);

    @POST("profile/users-from-name")
    Call<List<User>> getFilteredUsers(@Header("Authorization") String idToken, @Header("text") String filter);

    @POST("profile/user-from-id")
    Call<User> getUserFromId(@Header("Authorization") String idToken, @Header("id") String userId);

    @POST("profile/video-comments")
    Call<List<Comment>> getVideoComments(@Header("Authorization") String idToken, @Header("video_owner") String id ,  @Header("video_number") String vnum);

    @POST("profile/watch-video")
    Call<Object> addVideoView(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum);

    @POST("profile/share-video")
    Call<Object> shareVideo(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum, @Header("caption") String caption, @Header("comment_identifier") String commentId);

    @POST("profile/likes-video")
    Call<Object> likesVideo(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum);

    @POST("profile/dislikes-video")
    Call<Object> dislikesVideo(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum);

    @POST("profile/like-video")
    Call<Object> likeVideo(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum);

    @POST("profile/dislike-video")
    Call<Object> dislikeVideo(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum);

    @POST("profile/remove-video-like")
    Call<Object> removeVideoLike(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum);

    @POST("profile/remove-video-dislike")
    Call<Object> removeVideoDislike(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum);

    @POST("profile/likes-comment")
    Call<Object> likesComment(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum, @Header("comment_identifier") String commentId);

    @POST("profile/dislikes-comment")
    Call<Object> dislikesComment(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum, @Header("comment_identifier") String commentId);

    @POST("profile/like-comment")
    Call<Object> likeComment(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum, @Header("comment_identifier") String commentId);

    @POST("profile/dislike-comment")
    Call<Object> dislikeComment(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum, @Header("comment_identifier") String commentId);

    @POST("profile/remove-comment-like")
    Call<Object> removeCommentLike(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum, @Header("comment_identifier") String commentId);

    @POST("profile/remove-comment-dislike")
    Call<Object> removeCommentDislike(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum, @Header("comment_identifier") String commentId);

    @POST("profile/reply-to-comment")
    Call<Object> replyToComment(@Header("Authorization") String idToken, @Header("video_owner") String id, @Header("video_number") String vnum, @Header("comment_identifier") String commentId, @Header("caption") String caption);


  /*  @POST("profile/upload")
    @FormUrlEncoded
    //Call<ResponseBody> uploadVideo(@Header("Authorization") String idToken, @Part MultipartBody.Part file, @Part("title") RequestBody title, @Part("desc") RequestBody desc);
    Call<ResponseBody> uploadVideo(@Header("Authorization") String idToken, @Part("file") RequestBody file, @Part("title") RequestBody title, @Part("desc") RequestBody desc);
*/

    @POST("profile/upload")
    Call<ResponseBody> uploadVideo(@Header("Authorization") String idToken,
                                   @Header("videourl") String videourl,
                                   @Header("desc") String desc ,
                                   @Header("title") String title);

    @POST("get-otp")
    Call<JsonObject> getOtp( @Body JsonObject jsonObject);

    @POST("otp-verify")
    Call<JsonObject> verifyOTP( @Body JsonObject jsonObject);

}
