package in.ecstasy.app.Objects;

import com.google.gson.JsonObject;

import java.util.Map;

public class User {

    String name, phonenumber, admirerscount, followerscount,
            photourl, sharescount, type, username, id, bio, web;
    Map<String, Friend> friends;
    Map<String, SharedVideo> sharedvideos;
    Map<String, JsonObject> admiring;
    boolean isChecked;

    public User(String name, String phonenumber, String admirerscount, String followerscount, String photourl, String sharescount, String type, String username, String id, String bio,
                String web, Map<String, Friend> friends, Map<String, SharedVideo> sharedvideos, Map<String, JsonObject> admiring) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.admirerscount = admirerscount;
        this.followerscount = followerscount;
        this.photourl = photourl;
        this.sharescount = sharescount;
        this.type = type;
        this.username = username;
        this.id = id;
        this.bio = bio;
        this.web = web;
        this.friends = friends;
        this.sharedvideos = sharedvideos;
        this.admiring = admiring;
        this.isChecked = false;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", admirerscount='" + admirerscount + '\'' +
                ", followerscount='" + followerscount + '\'' +
                ", photourl='" + photourl + '\'' +
                ", sharescount='" + sharescount + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", bio='" + bio + '\'' +
                ", web='" + web + '\'' +
                ", friends=" + friends +
                ", sharedvideos=" + sharedvideos +
                ", admiring=" + admiring +
                ", isChecked=" + isChecked +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAdmirerscount() {
        return admirerscount;
    }

    public void setAdmirerscount(String admirerscount) {
        this.admirerscount = admirerscount;
    }

    public String getFollowerscount() {
        return followerscount;
    }

    public void setFollowerscount(String followerscount) {
        this.followerscount = followerscount;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getSharescount() {
        return sharescount;
    }

    public void setSharescount(String sharescount) {
        this.sharescount = sharescount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Map<String, Friend> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, Friend> friends) {
        this.friends = friends;
    }

    public Map<String, SharedVideo> getSharedvideos() {
        return sharedvideos;
    }

    public void setSharedvideos(Map<String, SharedVideo> sharedvideos) {
        this.sharedvideos = sharedvideos;
    }

    public Map<String, JsonObject> getAdmiring() {
        return admiring;
    }

    public void setAdmiring(Map<String, JsonObject> admiring) {
        this.admiring = admiring;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

//{
//        "admirerscount": "0",
//        "admiring": {
//        "0": {
//        "id": "S4pasyT5DqOb21L8MFJZ2K7t30W2"
//        }
//        },
//        "bio": "Biography",
//        "followerscount": "0",
//        "friends": {
//        "0": {
//        "id": "lt94Kp9rDbhDdXun5UyPxlJDi8X2",
//        "name": "Marco Pilloni",
//        "photo": "https://firebasestorage.googleapis.com/v0/b/theatronfinal.appspot.com/o/profileImages%2Flt94Kp9rDbhDdXun5UyPxlJDi8X2.jpeg?alt=media&token=3kal3"
//        }
//        },
//        "name": "Marco Pilloni",
//        "phonenumber": "324 8607489",
//        "photourl": "https://firebasestorage.googleapis.com/v0/b/theatronfinal.appspot.com/o/profileImages%2FgYbZzcqqdHXisaPnxlypWWlz3Tc2.jpeg?alt=media&token=v7mu3",
//        "sharedvideos": {
//        "0": {
//        "videoOwner": "S4pasyT5DqOb21L8MFJZ2K7t30W2",
//        "vnum": "0"
//        },
//        "1": {
//        "videoOwner": "S4pasyT5DqOb21L8MFJZ2K7t30W2",
//        "vnum": "0"
//        }
//        },
//        "sharescount": 0,
//        "type": "artist",
//        "username": "@marcopilloni12",
//        "web": "",
//        "id": "gYbZzcqqdHXisaPnxlypWWlz3Tc2"
//        }
