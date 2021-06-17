package in.ecstasy.app.Objects;

public class SharedVideo {
    String videoOwner, vnum;

    public SharedVideo(String videoOwner, String vnum) {
        this.videoOwner = videoOwner;
        this.vnum = vnum;
    }

    public String getVideoOwner() {
        return videoOwner;
    }

    public void setVideoOwner(String videoOwner) {
        this.videoOwner = videoOwner;
    }

    public String getVnum() {
        return vnum;
    }

    public void setVnum(String vnum) {
        this.vnum = vnum;
    }
}
