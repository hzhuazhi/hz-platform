package com.hz.pay.master.core.model.advert;


import com.hz.pay.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 广告的实体属性Bean
 * @Author yoko
 * @Date 2020/3/3 21:39
 * @Version 1.0
 */
public class AdvertModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233123301184L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 广告名称
     */
    private String adName;

    /**
     * 广告连接地址
     */
    private String adAds;

    /**
     * 广告有效开始时间
     */
    private String startTime;

    /**
     * 广告有效结束时间
     */
    private String endTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    private Integer yn;

    /**
     * 当前时间：yyyy-MM-dd HH-mm-ss
     */
    private String nowTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdAds() {
        return adAds;
    }

    public void setAdAds(String adAds) {
        this.adAds = adAds;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }
}
