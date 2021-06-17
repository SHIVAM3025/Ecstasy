package in.ecstasy.app.Objects;

public class Video {
    String date, desc, dislikes, id, likes, name, photo, shares, status, thumbnailphoto, time, title, url, view, vnum;
    // 0 - Nothing
    // 1 - like
    // 2 - dislike
    int videoStatus;

    public Video(String date, String desc, String dislikes, String id, String likes, String name, String photo, String shares, String status,
                 String thumbnailphoto, String time, String title, String url, String view, String vnum) {
        this.date = date;
        this.desc = desc;
        this.dislikes = dislikes;
        this.id = id;
        this.likes = likes;
        this.name = name;
        this.photo = photo;
        this.shares = shares;
        this.status = status;
        this.thumbnailphoto = thumbnailphoto;
        this.time = time;
        this.title = title;
        this.url = url;
        this.view = view;
        this.vnum = vnum;
        videoStatus = 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnailphoto() {
        return thumbnailphoto;
    }

    public void setThumbnailphoto(String thumbnailphoto) {
        this.thumbnailphoto = thumbnailphoto;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getVnum() {
        return vnum;
    }

    public void setVnum(String vnum) {
        this.vnum = vnum;
    }

    public int getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(int videoStatus) {
        this.videoStatus = videoStatus;
    }
}
