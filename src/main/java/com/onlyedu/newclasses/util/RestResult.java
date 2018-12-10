package com.onlyedu.newclasses.util;

/**
 * @author Andy
 * @date 2018/11/21 9:29
 */
public class RestResult {


    private static final int FAIL = 500;
    private static final int SUCCESS = 200;
    private static final int UNAUTHOR = 403;
    private static final int UNLOGIN = 401;
    private int status;
    private String message;
    private Object data;

    public static RestResult fail(String message){
        RestResult result = new RestResult();
        result.setStatus(FAIL);
        result.setMessage(message);
        return result;
    }

    public static RestResult unauthor(){
        RestResult result = new RestResult();
        result.setStatus(UNAUTHOR);
        result.setMessage("未授权");
        return result;
    }

    public static RestResult unLogin(){
        RestResult result = new RestResult();
        result.setStatus(UNLOGIN);
        result.setMessage("未登录");
        return result;
    }

    public static RestResult success(Object data){
        RestResult result = new RestResult();
        result.setStatus(SUCCESS);
        result.setData(data);
        return result;
    }

    public static RestResult success(){
        return success(null);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
