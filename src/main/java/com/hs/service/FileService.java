package com.hs.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hs.pojo.Files;
import com.hs.pojo.Users;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


public interface FileService  {
    Map<String, Object> uploadFile(MultipartFile file, Integer typeId) throws IOException;
    Map<String, Object> fileSliceUpload(MultipartFile file, String filename, String range) throws IOException;
    Map<String, Object> fileSliceDownLoad(String filename, HttpServletRequest request, HttpServletResponse response);
    Boolean deleteByFileName(String filename);
    Integer save(Files files);
     Page<Files> findPageByFiletype(int page, int size,String filetype) ;
    Page<Files> findPage(int page, int size) ;
     Files findById(String id);
     Integer update(Files files);
    Integer delete(String id);
}
