package in.ecstasy.app.Objects;

import org.json.JSONObject;

import java.util.HashMap;

public class Comment {
    String likes, dislikes, commentID, id;
    String comments;
    HashMap<String, SubComment> subcomments;
    HashMap<String, JSONObject> likedBy;
    boolean isExpanded;
    int commentStatus;

    public Comment(String id, String likes, String dislikes, String commentID, String comments, HashMap<String, JSONObject> likedBy, HashMap<String, SubComment> subcomments) {
        this.id = id;
        this.likes = likes;
        this.dislikes = dislikes;
        this.commentID = commentID;
        this.comments = comments;
        this.likedBy = likedBy;
        this.subcomments = subcomments;
        this.isExpanded = true;
        commentStatus = 0;
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

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public HashMap<String, JSONObject> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(HashMap<String, JSONObject> likedBy) {
        this.likedBy = likedBy;
    }

    public HashMap<String, SubComment> getSubcomments() {
        return subcomments;
    }

    public void setSubcomments(HashMap<String, SubComment> subcomments) {
        this.subcomments = subcomments;
    }

    public boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }
}
