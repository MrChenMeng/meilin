package com.ruoyi.common.core.page;


/**
 * @author chenm
 * @create 2019-08-01 16:07
 */
public class Message {
    private static final long serialVersionUID = 1L;

    private static final int success = 0;//成功

    private static final int logout = 302;//掉线

    private static final int error = 1;//失败

    private static final int level = 2;//权限不足

    private static final int update = 3;//数据发生变化


    private int code ;

    private String msg;

    private Object data;

    private String token;

    private int count;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static int getSuccess() {
        return success;
    }

    public static int getError() {
        return error;
    }

    public static int getLogout() {
        return logout;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static int getUpdate() {
        return update;
    }

    public static int getLevel() {
        return level;
    }
}
