package com.hs.pojo;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name="t_type")
public class Type implements Serializable {
    private static final long serialVersionUID = -1840831686851699943L;

    /**
     * 类别唯一标识
     */
    private Long typeId;

    /**
     *类别名称
     */
    private String typeName;


    /**
     * 类别描述信息
     */
    private String typeDescriber;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescriber() {
        return typeDescriber;
    }

    public void setTypeDescriber(String typeDescriber) {
        this.typeDescriber = typeDescriber;
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
