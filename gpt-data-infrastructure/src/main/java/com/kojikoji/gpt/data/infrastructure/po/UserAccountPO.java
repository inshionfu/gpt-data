package com.kojikoji.gpt.data.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * @ClassName UserAccountPO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/18 20:37
 * @Version
 */
@Slf4j
public class UserAccountPO {
    private Long id;
    private String openid;
    private Integer totalQuota;
    private Integer surplusQuota;
    private String modelTypes;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getTotalQuota() {
        return totalQuota;
    }

    public void setTotalQuota(Integer totalQuota) {
        this.totalQuota = totalQuota;
    }

    public Integer getSurplusQuota() {
        return surplusQuota;
    }

    public void setSurplusQuota(Integer surplusQuota) {
        this.surplusQuota = surplusQuota;
    }

    public String getModelTypes() {
        return modelTypes;
    }

    public void setModelTypes(String modelTypes) {
        this.modelTypes = modelTypes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public UserAccountPO() {
    }

    public UserAccountPO(Long id, String openid, Integer totalQuota, Integer surplusQuota, String modelTypes, Integer status, Date createTime, Date updateTime) {
        log.info("Creating UserAccountPO {} {} {} {} {} {} {}", id, openid, totalQuota, surplusQuota, modelTypes, status, createTime);
        this.id = id;
        this.openid = openid;
        this.totalQuota = totalQuota;
        this.surplusQuota = surplusQuota;
        this.modelTypes = modelTypes;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
