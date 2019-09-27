package com.hs.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hs.dao.FileMapper;
import com.hs.pojo.Files;
import com.hs.service.FileService;
import com.hs.util.FsUtil;
import com.hs.utils.FIleMerge;
import com.hs.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.StandardSystemProperty.FILE_SEPARATOR;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;
    @Override
    @Transactional
        public Map<String, Object> uploadFile (MultipartFile aFile, Integer typeId) throws IOException {
            Map<String, Object> map = new HashMap<>();
            String path = null;
            String originalFilename;
        //检查文件类型,大小
        //checkFileType(aFile);
        //带后缀名，文件名
        originalFilename = aFile.getOriginalFilename();
        //不带后缀名，文件名
        //String fileName = FileUtil.getFileName(aFile);
        //后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //时间戳
        Long time = System.currentTimeMillis();
        path = "D:\\FileUpload\\"+time;
        FsUtil.checkDirExists(new File(path));
        Files uploadFiles = new Files();
        String fileId = UUID.randomUUID().toString().replace("-", "");
        uploadFiles.setFileName(originalFilename);
        uploadFiles.setFileSuffix(suffix);
        uploadFiles.setFilePath(path);
        uploadFiles.setFileSize(aFile.getSize());
        uploadFiles.setTypeId(typeId);
        map.put("filePath", path);
        map.put("Filename", originalFilename);
        int insert = fileMapper.insert(uploadFiles);
               /* if (insert > 0) {
                    //将文件写入硬盘
                    aFile.transferTo((Path) uploadFile);
                }*/
        map.put("code","200");
        return map;
        }

    @Override
    public Map<String, Object> fileSliceUpload(MultipartFile file, String filename, String range) throws IOException {
        String rootPath="D:" + File.separator + "data";
        Map<String, Object> map = new HashMap<>();
        //获取文件分片情况
        String[] chunkSizes = range.split("-");
        long chunkSize = 0l;//切片大小
        chunkSize = Long.parseLong(chunkSizes[1]) - Long.parseLong(chunkSizes[0]);
        //不带后缀的文件名称
        String orignalFilename = filename.substring(0, filename.lastIndexOf("."));
        //文件保存的父文件夹位置
        File parentFolder = new File(rootPath);
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }
        //查询文件夹中是否有同名文件，有的话直接合并
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        File rootFile = new File(rootPath + File.separator + filename);
        if (!rootFile.exists()) {
            rootFile.createNewFile();
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(rootFile));
                bufferedInputStream = new BufferedInputStream(file.getInputStream());
                int len = 0;
                byte[] bytes = new byte[2048];
                while ((len = bufferedInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
            } catch (Exception e) {

            } finally {
                try {
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            FileReader fr1 = new FileReader(rootFile);//读取已存在文件的内容
            //将新上传的后续分片文件先存到temp
            File tempFile = new File(rootPath + File.separator + "temp");
            if (!tempFile.exists()) {
                tempFile.mkdir();
            }
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile + File.separator + filename));
                bufferedInputStream = new BufferedInputStream(file.getInputStream());
                int len = 0;
                byte[] bytes = new byte[2048];
                while ((len = bufferedInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
            } catch (Exception e) {
                e.getMessage();
            } finally {
                try {
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String[] fpaths = {rootPath + File.separator + filename, rootPath + File.separator + "temp" + File.separator + filename};
            //合并文件
            //判断合并后文件夹temp2是否存在，不存在创建一个
            String mergePath=rootPath + File.separator + "temp2";
            File temp2 = new File(mergePath);
            if (!temp2.exists()) {
                temp2.mkdir();
            }
            FIleMerge.mergeFiles(fpaths, mergePath+ File.separator + filename);
            rootFile.delete();
            File mergeFile=new File(mergePath+ File.separator + filename);
            //复制合并后的文件到原来的文件夹下
            FileInputStream fis = new FileInputStream(mergePath+ File.separator + filename);
            BufferedInputStream bufis = new BufferedInputStream(fis);

            FileOutputStream fos = new FileOutputStream(rootFile);
            BufferedOutputStream bufos = new BufferedOutputStream(fos);

            int len = 0;
            while ((len = bufis.read()) != -1) {
                bufos.write(len);
            }
            bufis.close();
            bufos.close();
            //删除temp2中的文件
            mergeFile.delete();
        }
        map.put("code", "200");
        map.put("error", "上传成功");
        return map;
    }

    @Override
    public Map<String, Object> fileSliceDownLoad(String filename, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map=new HashMap<>();
        // Get your file stream from wherever.
        String fullPath = "D:\\data\\" + filename;
        File downloadFile = new File(fullPath);
        //判断目标文件是否存在
        Boolean r=FileUtils.findFile(new File("D:\\data\\"),filename);
        if(r){
            ServletContext context = request.getServletContext();
            // get MIME type of the file
            String mimeType = context.getMimeType(fullPath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            // response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);
            // 解析断点续传相关信息
            response.setHeader("Accept-Ranges", "bytes");
            long downloadSize = downloadFile.length();
            long fromPos = 0, toPos = 0;
            if (request.getHeader("Range") == null) {
                response.setHeader("Content-Length", downloadSize + "");
            } else {
                // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                String range = request.getHeader("Range");
                String bytes = range.replaceAll("bytes=", "");
                String[] ary = bytes.split("-");
                fromPos = Long.parseLong(ary[0]);
                if (ary.length == 2) {
                    toPos = Long.parseLong(ary[1]);
                }
                int size;
                if (toPos > fromPos) {
                    size = (int) (toPos - fromPos);
                } else {
                    size = (int) (downloadSize - fromPos);
                }
                response.setHeader("Content-Length", size + "");
                downloadSize = size;
            }
            // Copy the stream to the response's output stream.
            RandomAccessFile in = null;
            OutputStream out = null;
            try {
                in = new RandomAccessFile(downloadFile, "rw");
                // 设置下载起始位置
                if (fromPos > 0) {
                    in.seek(fromPos);
                }
                // 缓冲区大小
                int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
                byte[] buffer = new byte[bufLen];
                int num;
                int count = 0; // 当前写到客户端的大小
                out = response.getOutputStream();
                while ((num = in.read(buffer)) != -1) {
                    out.write(buffer, 0, num);
                    count += num;
                    //处理最后一段，计算不满缓冲区的大小
                    if (downloadSize - count < bufLen) {
                        bufLen = (int) (downloadSize-count);
                        if(bufLen==0){
                            break;
                        }
                        buffer = new byte[bufLen];
                    }
                }
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
                map.put("code","400");
                map.put("error",e.getMessage());
            } finally {
                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                map.put("code","200");
                map.put("error","下载成功");
        }else{
            map.put("code","400");
            map.put("error","下载失败，目标文件不存在");
        }
        return map;
    }

    @Override
    @Transactional
    public Boolean deleteByFileName(String filename) {
        Boolean b=false;
        File testFile = new File("D:" + File.separator + "data" + File.separator + filename);
        if (!testFile.exists()) {
            System.out.println("测试文件不存在");
        }else {
            testFile.delete();
            System.out.println("删除成功");
            b=true;
        }
        return b;
    }

    @Override
    public Integer save(Files files) {
        return fileMapper.insert(files);
    }

    @Override
    public Page<Files> findPageByFiletype(int page, int size, String filetype) {
        PageHelper.startPage(page, size);
        return (Page<Files>) fileMapper.selectByFileType(filetype);
    }

    @Override
    public Files findById(String id) {
        Files ff=new Files();
        ff.setFileId(Long.parseLong(id));
        Files list=fileMapper.selectOne(ff);
        return list;
    }

    @Override
    @Transactional
    public Integer update(Files files) {
        return fileMapper.updateTitleAndDescriberAndexeName(files);
    }

    @Override
    @Transactional
    public Integer delete(String id) {
        return fileMapper.deleteById(id);
    }

    /**
     * 将字节大小转换为KB/MB/GB/B
     *
     * @param size
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
    public Page<Files> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Files>) fileMapper.selectAll();
    }
}
