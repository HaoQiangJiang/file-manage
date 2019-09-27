package com.hs.service.impl;


import com.hs.dao.TypeMapper;
import com.hs.pojo.Type;
import com.hs.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl  implements TypeService {
    @Autowired private TypeMapper typeMapper;
    @Override
    public List<Type> SelectByName(String filetype) {
        return typeMapper.selectByName(filetype);
    }

    @Override
    public Integer save(Type type) {
        return typeMapper.insert(type);
    }

    @Override
    public Integer SelectIdByName(String filetype) {
        return typeMapper.SelectIdByName(filetype);
    }
}
