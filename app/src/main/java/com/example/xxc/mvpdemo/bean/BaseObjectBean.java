package com.example.xxc.mvpdemo.bean;

/**
 * @Description: 请求得到的数据封装对象
 * @Author: xxc
 * @Date: 2020/4/25 0:55
 * @Version: 1.0
 */
public class BaseObjectBean<T> {

    /**
     * 数据格式
     * status : 1
     * msg : "获取成功"
     * result : {}
     */
    private int errorCode;
    private String errorMsg;
    private T result;

    public BaseObjectBean() {
    }

    public BaseObjectBean(int errorCode, String errorMsg, T result) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
