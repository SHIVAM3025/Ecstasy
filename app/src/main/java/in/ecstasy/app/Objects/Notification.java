package in.ecstasy.app.Objects;

/**
 * Created By Shivam Gupta on 24-06-2021 of package in.ecstasy.app.Objects
 */
public class Notification {

    private String header;
    private String body;
    private String date;

    public Notification(String header, String body, String date) {
        this.header = header;
        this.body = body;
        this.date = date;
    }

    public Notification() {
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
