package com.hs.entity;

import java.util.List;

/**
 * 分页结果类
 */
public class PageResult<T> {

    private int code;
    private int pages;
    private List<T> users;
    private String error;

    public PageResult(int code, int pages, List<T> users, String error) {
        this.code = code;
        this.pages = pages;
        this.users = users;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getUsers() {
        return users;
    }

    public void setUsers(List<T> users) {
        this.users = users;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
