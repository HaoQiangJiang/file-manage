package com.hs.pojo;



import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="t_file_img")
public class FileImg implements Serializable {
    private static final long serialVersionUID = -1840831686851699943L;

    /**
     * 类别唯一标识
     */
    private Long id;

    /**
     *类别名称
     */
    private String typeId;


    /**
     * 类别描述信息
     */
    private String imgId;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
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
