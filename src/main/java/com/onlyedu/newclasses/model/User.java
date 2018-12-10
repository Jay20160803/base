package com.onlyedu.newclasses.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Andy
 * @date 2018/11/20 17:46
 */
public class User implements Serializable {

    @NotNull(message = "userName 不能为空")
    private String userName;
    @NotNull(message = "passWord 不能为空")
    private String passWord;
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
