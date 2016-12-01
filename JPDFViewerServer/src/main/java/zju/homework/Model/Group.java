package zju.homework.Model;

import java.io.Serializable;

/**
 * Created by stardust on 2016/11/29.
 */
public class Group implements Serializable{

    private String groupId;

    private String pdfData;

    private String Creator;

    public Group(){}

    public Group(String groupId, String pdfData, String creator) {
        this.groupId = groupId;
        this.pdfData = pdfData;
        Creator = creator;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPdfData() {
        return pdfData;
    }

    public void setPdfData(String pdfData) {
        this.pdfData = pdfData;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }
}
