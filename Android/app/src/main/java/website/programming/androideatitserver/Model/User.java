package website.programming.androideatitserver.Model;

/**
 * Created by cokel on 2/27/2018.
 */

public class User {
    private String userId;
    private String Name;
    private String Password;
    private String Phone;
    private boolean IsStaff;
    private String SecureCode;

    public User() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public boolean getIsStaff() {
        return IsStaff;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getSecureCode() {
        return SecureCode;
    }

    public void setSecureCode(String secureCode) {
        this.SecureCode = secureCode;
    }

    public void setIsStaff(boolean isStaff) {
        this.IsStaff = isStaff;
    }
}
