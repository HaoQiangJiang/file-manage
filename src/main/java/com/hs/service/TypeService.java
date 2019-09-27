package com.hs.service;

import com.hs.pojo.Type;

import java.util.List;

public interface TypeService  {
    List<Type> SelectByName(String filetype);
    Integer save(Type type);
    Integer SelectIdByName(String filetype);
}
