package com.hs.entity;

/**
 * 返回结果实体类
 */
public class Result<T> {


    private String error; //错误信息
    private Integer code;//返回码


    private T user;//返回数据

    public Result(Integer code, String error, T user) {
        this.code = code;
        this.error = error;
        this.user = user;
    }



    public Result(Integer code, String error) {
        this.code = code;
        this.error = error;
    }

    public Result() {
    }



    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }



    public T getUser() {
        return user;
    }


    public void setUser(T user) {
        this.user = user;
    }
}
