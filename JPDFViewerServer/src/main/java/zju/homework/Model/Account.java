package zju.homework.Model;

import java.io.Serializable;

/**
 * Created by stardust on 2016/11/29.
 */
public class Account implements Serializable{

    private String email;

    private String passwd;

    private String groupId;

    public Account(){

    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Account(String email, String passwd, String groupId) {

        this.email = email;
        this.passwd = passwd;
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
