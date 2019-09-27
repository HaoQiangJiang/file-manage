package com.hs.controller;


import com.hs.annotation.UserLoginToken;
import com.hs.entity.Result;
import com.hs.entity.StatusCode;
import com.hs.pojo.Files;
import com.hs.pojo.Type;
import com.hs.service.FileService;
import com.hs.service.TypeService;
import io.swagger.annotations.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Api("文件接口")
@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private TypeService typeService;
    private ApiResponse response;

    /**
     * 添加文件信息
     * @param title
     * @param describe
     * @param filetype
     * @param exename
     * @param imgname
     * @param filename
     * @param filesize
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "添加文件信息" ,  notes="添加一条文件信息记录。成功，返回201；失败，返回错误原因。该操作需要管理员权限，必须在http头或者formData设置token值。",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="title", value="资源标题", required=true, paramType="query", dataType = "String"),
            @ApiImplicitParam(name="describe", value="资源详细描述", required=true, paramType="query", dataType = "String"),
            @ApiImplicitParam(name="filetype", value="资源类型(exe、mp4等)", required=true, paramType="query", dataType = "String"),
            @ApiImplicitParam(name="exename", value="可执行程序名称，仅当filetype为exe时填写该字段", paramType="query", dataType = "String"),
            @ApiImplicitParam(name="imgname", value="图片上传后服务器返回的名称", required=true, paramType="query", dataType = "String"),
            @ApiImplicitParam(name="filename", value="文件上传后服务器返回的名称", required=true, paramType="query", dataType = "String"),
            @ApiImplicitParam(name="filesize", value="文件大小，以byte为单位", required=true, paramType="query", dataType = "long")
    })
    @PostMapping(value = "/api/v1/resfiles")
    public Map resfiles(@RequestParam("title") String title
            , @RequestParam("describe") String describe
            , @RequestParam("filetype") String filetype
            , @RequestParam("exename") String exename
            , @RequestParam("imgname") String imgname
            , @RequestParam("filename") String filename
            , @RequestParam("filesize") Long filesize) {
        Map map=new HashMap();
        Files files=new  Files();
        files.setFileTitle(title);
        files.setFileDescriber(describe);
        files.setFileSize(filesize);
        files.setImgName(imgname);
        files.setFileName(filename);
        files.setCreateTime(new Date());
        try {
            if (filetype.equals("exe")) {
                files.setExeName(exename);
            }
            List<Type> list = typeService.SelectByName(filetype);
            //查询文件类型在类别表中是否存在，存在的话直接添加，不存在先添加这种类别
            if (list.size() < 1) {
                Type type = new Type();
                type.setTypeName(filetype);
                type.setTypeDescriber(filetype + "文件");
                type.setCreateTime(new Date());
                Integer j = typeService.save(type);
                Integer id = typeService.SelectIdByName(filetype);
                files.setTypeId(id);
            } else {
                Integer id = typeService.SelectIdByName(filetype);
                files.setTypeId(id);
            }
            Integer j = fileService.save(files);
            if(j>0){
                map.put("code","201");
                map.put("err","添加成功");
            }else{
                map.put("code","400");
                map.put("err","添加失败");
            }
        }catch (Exception e){
            map.put("code","400");
            map.put("err",e.getMessage());
        }
        return map;
    }

    /**
     * 获取文件信息列表
     * @param page
     * @param filetype
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "获取文件信息列表" ,  notes="获取文件信息列表。成功，返回文件列表；失败，返回错误原因。用户必须已登录，必须在http头或者url中设置token值。",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="page", value="页数，指定获取第几页", required=true, paramType="query", dataType = "String"),
            @ApiImplicitParam(name="filetype", value="资源类型(eg：exe,mp4…)",  paramType="query", dataType = "String")
    })
    @GetMapping(value = "/api/v1/resfiles")
    public Map resfiles(@RequestParam("page") String page, @RequestParam(required = false) String filetype){
        Map map=new HashMap();
        List<Files> list=new ArrayList<>();
        Long pages=0l;
        try {
            if(filetype == null || "".equals(filetype)){
                list = fileService.findPage(Integer.parseInt(page), 10);
            }else {
                list = fileService.findPageByFiletype(Integer.parseInt(page), 10, filetype);
            }
            if(list.size()>0) {
                map.put("code", "200");
                map.put("error", "查询成功");
                map.put("resfiles", list);
                map.put("pages", (list.size()/10)+1);
            }else{
                map.put("code", "400");
                map.put("error", "查询失败");
                map.put("resfiles", list);
                map.put("pages", "");
            }
            return map;
        }catch (Exception e){
            map.put("code", "400");
            map.put("error", "");
            map.put("resfiles", e.getMessage());
            map.put("pages", "");
        }
        return map;
    }

    /**
     * 获取指定文件信息
     * @param id
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "获取指定文件信息" ,  notes="获取文件信息列表。成功，返回文件列表；失败，返回错误原因。用户必须已登录，必须在http头或者url中设置token值。",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="id", value="文件id", required=true, paramType="path", dataType = "String")
    })
    @GetMapping(value = "/api/v1/resfiles/{id}")
    public Map resfiles(@PathVariable String id){
        Map map=new HashMap();
        Files files=new Files();
        try {
            files = fileService.findById(id);
            if(files == null || "".equals(files)){
                map.put("code", "400");
                map.put("error", "获取失败");
                map.put("resfiles","");
            }else{
                map.put("code", "200");
                map.put("error", "获取成功");
                map.put("resfiles", files);
            }
            return map;
        }catch (Exception e){
            map.put("code", "400");
            map.put("error", e.getMessage());
            map.put("resfiles", files);
        }
        return map;
    }
    /**
     * 更新文件信息
     * @param id
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "更新文件信息" ,  notes="更新文件信息记录。成功，返回200；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值。",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="id", value="资源id", required=true, paramType="path", dataType = "String"),
            @ApiImplicitParam(name="title", value="资源标题", required=true, paramType="query", dataType = "String"),
            @ApiImplicitParam(name="exename", value="可执行程序名称",  paramType="query", dataType = "String"),
            @ApiImplicitParam(name="describe", value="资源详细描述", required=true, paramType="query", dataType = "String")
    })
    @PutMapping(value = "/api/v1/resfiles/{id}")
    public Map resfiles(@PathVariable String id,@RequestParam("title") String title,@RequestParam("describe") String describe,@RequestParam(required = false) String exename ){
        Map map=new HashMap();
        Files files=new Files();
        files.setFileId(Long.parseLong(id));
        files.setFileTitle(title);
        files.setFileDescriber(describe);
        files.setExeName(exename);
        try {
            Integer i=fileService.update(files);
            if(i>0){
                map.put("code", "200");
                map.put("error", "更新成功");
            }else{
                map.put("code", "400");
                map.put("error", "更新失败");
            }
        }catch (Exception e){
            map.put("code", "400");
            map.put("error", e.getMessage());
        }
        return map;
    }
    /**
     * 删除文件信息
     * @param id
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "删除文件信息" ,  notes="删除一条文件信息记录。成功，返回200；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="id", value="文件id", required=true, paramType="path", dataType = "String")
    })
    @DeleteMapping(value = "/api/v1/resfiles/{id}")
    public Map resfiles1(@PathVariable String id,String token){
        Map map=new HashMap();
        try {
            Integer i=fileService.delete(id);
            if(i>0){
                map.put("code", "200");
                map.put("error", "删除成功");
            }else{
                map.put("code", "400");
                map.put("error", "删除失败");
            }
        }catch (Exception e){
            map.put("code", "400");
            map.put("error", "服务器异常");
        }
        return map;
    }
    /**
     * 上传文件
     *
     * @param file
     * @return
     */
   /* @UserLoginToken
   *//* @ApiOperation(value = "上传文件" ,  notes="上传文件")*//*
    @PostMapping(value = "uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file, Integer typeId) {
        Map<String, Object> map;
        try {
            System.out.println(file.getSize());
            map = fileService.uploadFile(file,typeId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(StatusCode.ERROR,"上传成功",e.getMessage());
        }
        return new Result(StatusCode.ERROR,"上传成功");
    }*/
    /**
     * 分片上传文件
     *
     * @param file 上传文件分片
     * @param filename 文件uuid+后缀名
     * @param range  上传文件分片范围(start-end)，eg：0-1024
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "上传文件分片" ,  notes="上传文件。成功，返回200；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值。",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="filename", value="文件uuid+后缀名", required=true, paramType="form", dataType = "String"),
            @ApiImplicitParam(name="range", value="上传文件分片范围(start-end)，eg：0-1024, 0-", required=true, paramType="form", dataType = "String"),
            @ApiImplicitParam(name="file", value="上传文件分片", required=true, paramType="form", dataType = "__file")
    })
    @PostMapping(value = "/api/v1/files")
    public Result files(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename, @RequestParam("range") String range) {
        /**
         * 判断前端Form表单格式是否支持文件上传
         */
            try {
                Map map=fileService.fileSliceUpload(file,filename,range);
                if(map.get("code")=="200"){
                    return new Result(StatusCode.OK,"上传成功",filename);
                }else {
                    return new Result(StatusCode.ERROR,"上传失败",filename);
                }

            } catch (Exception e) {
                return new Result(StatusCode.ERROR,"上传失败"+HttpStatus.INTERNAL_SERVER_ERROR,filename);
            }
    }

    /**
     * 下载文件
     *
     * @param filename 文件名
     * @return
     */
   /* @UserLoginToken
    @ApiOperation(value = "下载文件" ,  notes="下载文件。成功，返回200；失败，返回错误原因。用户必须已登录，必须在http头或者url中设置token值。用户可在http头设置Range参数实现分段下载",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="filename", value="文件名", required=true, paramType="path", dataType = "String")
    })
    @GetMapping (value = "/api/v1/download/{filename}")
    public void files(@PathVariable String filename,HttpServletRequest request, HttpServletResponse response) {
        try(InputStream inputStream = new FileInputStream(new File("D:\\data\\" ,filename));
            OutputStream outputStream = response.getOutputStream();)
        {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename="+filename);
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;
    }*/
    /**
     * 下载文件（断点续传）
     *
     * @param filename 文件名
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "下载文件" ,  notes="下载文件。成功，返回200；失败，返回错误原因。用户必须已登录，必须在http头或者url中设置token值。用户可在http头设置Range参数实现分段下载",produces = "application/octet-stream")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="filename", value="文件名", required=true, paramType="path", dataType = "String")
    })
    @GetMapping (value = "/api/v1/files/{filename}")
    public Map download(@PathVariable String filename,HttpServletRequest request, HttpServletResponse response) {
        return fileService.fileSliceDownLoad(filename,request,response);
    }

    /**
     * 删除文件
     *
     * @param filename 文件名
     * @return
     */
    @ApiOperation(value = "删除文件" ,  notes="删除文件。成功，返回200；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值。",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(name="filename", value="文件名", required=true, paramType="path", dataType = "String")
    })
    @DeleteMapping (value = "/api/v1/files/{filename}")
    public Map files(@PathVariable String filename) {
        Map<String, Object> map=new HashMap<>();
        try {
            Boolean b = fileService.deleteByFileName(filename);
            if (b) {
                map.put("code", "200");
                map.put("error", "删除成功");
            }else{
                map.put("code", "400");
                map.put("error", "删除失败");
            }
        }catch (Exception e){
            map.put("code", "400");
            map.put("error", "删除失败"+e.getMessage());
        }
        return map;
    }
    /**
     * 获取文件uuid
     *
     * @return
     */
    @ApiOperation(value = "获取文件uuid" ,  notes="获取文件uuid。成功，返回200；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值。",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
    })
    @GetMapping (value = "/api/v1/uuid")
    public Map uuid() {
        Map<String, Object> map=new HashMap<>();
        String uuidStr=null;
        try {
            UUID uuid = UUID.randomUUID();
            String str = uuid.toString();
            uuidStr = str.replace("-", "");
            map.put("code", "200");
            map.put("error", "获取成功");
        }catch (Exception e){
            map.put("code", "400");
            map.put("error", e.getMessage());
        }
        map.put("uuid", uuidStr);
        return map;
    }
}
