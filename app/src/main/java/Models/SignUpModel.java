package Models;

public class SignUpModel {

    String user_name, user_mail, user_password, user_ID, last_message;

    public SignUpModel(String user_name,String user_mail, String user_password, String user_ID, String last_message) {
        this.user_name = user_name;
        this.user_mail = user_mail;
        this.user_password = user_password;
        this.user_ID = user_ID;
        this.last_message = last_message;
    }

    public SignUpModel(String user_name,String user_mail, String user_password) {
        this.user_name = user_name;
        this.user_mail = user_mail;
        this.user_password = user_password;
    }

    public SignUpModel() {}

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

}
