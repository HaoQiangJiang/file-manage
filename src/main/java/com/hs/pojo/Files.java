package com.hs.pojo;



import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name="t_file")
public class Files implements Serializable {
    private static final long serialVersionUID = -1840831686851699943L;

    /**
     * 文件唯一标识
     */
    private Long fileId;
    /**
     *资源标题
     */
    private String fileTitle;
    /**
     *文件名称
     */
    private String fileName;
    /**
     *图片名称
     */
    private String imgName;

    /**
     *exe文件名称
     */
    private String exeName;
    /**
     * 文件所属类别ID
     */
    private Integer typeId;

    /**
     * 文件描述信息
     */
    private String fileDescriber;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件标签
     */
    private String fileLabel;

    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件配图路径
     */
    private String fileImgPath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getExeName() {
        return exeName;
    }

    public void setExeName(String exeName) {
        this.exeName = exeName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getFileDescriber() {
        return fileDescriber;
    }

    public void setFileDescriber(String fileDescriber) {
        this.fileDescriber = fileDescriber;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileLabel() {
        return fileLabel;
    }

    public void setFileLabel(String fileLabel) {
        this.fileLabel = fileLabel;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }


    public String getFileImgPath() {
        return fileImgPath;
    }

    public void setFileImgPath(String fileImgPath) {
        this.fileImgPath = fileImgPath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }


}
