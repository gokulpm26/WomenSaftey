package android.cybersiblings.safegaurd;

public class Login_Status {
    public String getLogin_message() {
        return Login_message;
    }

    public void setLogin_message(String login_message) {
        Login_message = login_message;
    }

    public Boolean getLogin_status() {
        return Login_status;
    }

    public void setLogin_status(Boolean login_status) {
        Login_status = login_status;
    }

    String Login_message;
Boolean Login_status;
}
