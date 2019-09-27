package com.hs.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "codes")
public class Codes {
    @Id
    private Integer id;

    private String code;
    private String email;

    public Codes() {
    }

    public Codes(String code, String email) {
        this.code = code;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
