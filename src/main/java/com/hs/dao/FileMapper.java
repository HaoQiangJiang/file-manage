package com.hs.dao;

import com.hs.pojo.Files;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface FileMapper extends Mapper<Files> {
    @Select(value = "select file_id as fileId,file_title as fileTitle,file_name as fileName,img_name as imgName,exe_name as exeName,t.type_id as typeId,file_describer as fileDescriber,file_path as filePath,file_label as fileLabel,file_suffix as fileSuffix,file_size as fileSize,file_img_path as fileImgPath,f.last_update_time as lastUpdateTime from t_file f,t_type t where f.type_id=t.type_id and t.type_name=#{filetype}")
    List<Files> selectByFileType(@Param("filetype") String filetype);

    @Select(value = "select * from t_file where file_id=#{id}")
    Files findById(@Param("id") String id);

    @Update({ "update t_file set file_title = #{fileTitle},file_name = #{fileName},img_name = #{imgName},exe_name = #{exeName},type_id = #{typeId},file_describer = #{fileDescriber},file_path = #{filePath},file_label = #{fileLabel},file_suffix = #{fileSuffix},file_size = #{fileSize},file_img_path = #{fileImgPath},last_update_time = #{lastUpdateTime, jdbcType=TIMESTAMP} where file_id = #{fileId}" })
    Integer update(Files files);

    @Update({ "update t_file set file_title = #{fileTitle},exe_name = #{exeName},file_describer = #{fileDescriber},last_update_time = #{lastUpdateTime, jdbcType=TIMESTAMP} where file_id = #{fileId}" })
    Integer updateTitleAndDescriberAndexeName(Files files);

    @Delete(value = "DELETE from t_file where file_id=#{id}")
    Integer deleteById(@Param("id") String id);
}
