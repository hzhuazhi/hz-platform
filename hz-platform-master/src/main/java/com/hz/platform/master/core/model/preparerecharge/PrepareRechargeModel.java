package com.hz.platform.master.core.model.preparerecharge;


import com.hz.platform.master.core.protocol.page.BasePage;

/**
 * @author yoko
 * @desc 代付预备充值表:预备给渠道进行充值的实体属性Bean
 * @create 2021-11-15 15:25
 **/
public class PrepareRechargeModel extends BasePage {
    /**
     * 自增主键ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * 名称/别名
     *
     * @mbggenerated
     */
    private String alias;

    /**
     * 渠道的主键ID
     *
     * @mbggenerated
     */
    private Long channelId;

    /**
     * 我方订单号：变更涉及到的订单号
     *
     * @mbggenerated
     */
    private String myTradeNo;

    /**
     * 要变更的金额
     *
     * @mbggenerated
     */
    private String money;
    /**
     * 渠道名称
     *
     * @mbggenerated
     */
    private String channelName;

    /**
     * 变更金额类型：0初始化，1核减金额，2加金额
     *
     * @mbggenerated
     */
    private Integer changeType;

    /**
     * 证据截图
     *
     * @mbggenerated
     */
    private String pictureAds;

    /**
     * 数据是否展现给用户看：0初始化，1展现，2不展现
     *
     * @mbggenerated
     */
    private Integer isShow;

    /**
     * 数据说明：做解说用的
     *
     * @mbggenerated
     */
    private String dataExplain;

    /**
     * 备注
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * 补充数据的类型：1初始化，2补充数据失败（其它原因等..），3补充数据成功
     */
    private Integer workType;

    /**
     * 创建日期：存的日期格式20160530
     *
     * @mbggenerated
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     *
     * @mbggenerated
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     *
     * @mbggenerated
     */
    private Integer curminute;

    /**
     * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     *
     * @mbggenerated
     */
    private Integer runStatus;

    /**
     * 运行计算次数
     *
     * @mbggenerated
     */
    private Integer runNum;

    /**
     * 创建人ID
     *
     * @mbggenerated
     */
    private Long createUserId;

    /**
     * 创建人归属角色ID
     *
     * @mbggenerated
     */
    private Long createRoleId;

    /**
     * 更新人ID
     *
     * @mbggenerated
     */
    private Long updateUserId;

    /**
     * 更新人归属角色ID
     *
     * @mbggenerated
     */
    private Long updateRoleId;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private String createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    private String updateTime;

    /**
     * TS时间
     *
     * @mbggenerated
     */
    private String tsTime;

    /**
     * 是否有效：0有效，1无效/删除
     *
     * @mbggenerated
     */
    private Integer yn;
    /**
     * 开始时间
     *
     * @mbggenerated
     */
    private Integer curdayStart;
    /**
     * 结束时间
     *
     * @mbggenerated
     */
    private Integer curdayEnd;

    /**
     * 补充数据的类型：1初始化，2补充数据失败（其它原因等..），3补充数据成功
     */
    private Integer checkWorkType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getMyTradeNo() {
        return myTradeNo;
    }

    public void setMyTradeNo(String myTradeNo) {
        this.myTradeNo = myTradeNo;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public String getPictureAds() {
        return pictureAds;
    }

    public void setPictureAds(String pictureAds) {
        this.pictureAds = pictureAds;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getDataExplain() {
        return dataExplain;
    }

    public void setDataExplain(String dataExplain) {
        this.dataExplain = dataExplain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getCreateRoleId() {
        return createRoleId;
    }

    public void setCreateRoleId(Long createRoleId) {
        this.createRoleId = createRoleId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Long getUpdateRoleId() {
        return updateRoleId;
    }

    public void setUpdateRoleId(Long updateRoleId) {
        this.updateRoleId = updateRoleId;
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

    public String getTsTime() {
        return tsTime;
    }

    public void setTsTime(String tsTime) {
        this.tsTime = tsTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }

    public Integer getCheckWorkType() {
        return checkWorkType;
    }

    public void setCheckWorkType(Integer checkWorkType) {
        this.checkWorkType = checkWorkType;
    }
}
