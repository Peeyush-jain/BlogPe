package Models;

import com.google.firebase.database.ServerValue;

public class Post {

    private String title;
    private String content;
    private Object blogTimeCreated;
    private int blogId;
    private String imageUrl;
    private String author;


    public Post(String title, String content, int blogId, String imageUrl, String author) {
        this.title = title;
        this.content = content;
        this.blogTimeCreated = ServerValue.TIMESTAMP;
        this.blogId = blogId;
        this.imageUrl = imageUrl;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Object getBlogTimeCreated() {
        return blogTimeCreated;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBlogTimeCreated(Object blogTimeCreated) {
        this.blogTimeCreated = blogTimeCreated;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
