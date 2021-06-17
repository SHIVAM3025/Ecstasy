package in.ecstasy.app.Objects;

public class SubComment {

    String comments, id;

    public SubComment(String comments, String id) {
        this.comments = comments;
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //    [
//    {
//        "comments": "Awesome",
//            "dislikes": "0",
//            "id": "gYbZzcqqdHXisaPnxlypWWlz3Tc2",
//            "likedby": {
//        "-MMU8-9b8qr3K877dydO": {
//            "id": "N3PctqYLxjMqzfG6QsqNAF9P6RL2"
//        },
//        "-MNIOnp2oziiDoXg3cTH": {
//            "id": "N3PctqYLxjMqzfG6QsqNAF9P6RL2"
//        }
//    },
//        "likes": "2",
//            "subcomments": {
//        "-MN-Uo7Dk0vEGXdtxvK6": {
//            "comments": "really awesome",
//                    "id": "ruqdWUHkFzWmBzISxztHRv4frA23"
//        }
//    },
//        "commentID": "-MMMq9P0d0QHdOs3VcPr"
//    }
//]
}
