package zju.homework;

import java.io.Serializable;

/**
 * Created by stardust on 2016/12/1.
 */
public class AnnotationData implements Serializable {

    private String author;

    private String data;

    private String groupId;

    public AnnotationData(String author, String data, String groupId) {
        this.author = author;
        this.data = data;
        this.groupId = groupId;
    }

    public AnnotationData(){

    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

