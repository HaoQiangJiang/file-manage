package com.hs.dao;


import com.hs.pojo.Type;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TypeMapper extends Mapper<Type> {
    @Select(value = "select * from t_type where type_name=#{filetype}")
    List<Type> selectByName(@Param("filetype") String filetype);
    @Select(value = "select type_id from t_type where type_name=#{filetype}")
    Integer SelectIdByName(@Param("filetype") String filetype);
}
