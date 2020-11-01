package com.hz.platform.master.util;

import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.task.base.StatusModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Description 定时任务的公共类
 * @Author yoko
 * @Date 2020/1/11 16:20
 * @Version 1.0
 */
public class TaskMethod {
    private static Logger log = LoggerFactory.getLogger(TaskMethod.class);




    /**
     * @Description: 组装查询定时任务的查询条件
     * @param limitNum - 多少条数据
     * @param runType - 运行类型
     * @param workType - 运算类型
     * @param greaterThan - 大于
     * @param lessThan - 小于
     * @param sendType - 发送类型
     * @param orderStatus - 订单状态
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskStatusQuery(int limitNum, int runType, int workType, int greaterThan, int lessThan, int sendType, int orderStatus){
        StatusModel resBean = new StatusModel();
        if (runType > 0){
            resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (greaterThan > 0){
            resBean.setGreaterThan(greaterThan);
        }
        if (lessThan > 0){
            resBean.setLessThan(lessThan);
        }
        if (sendType > 0){
            resBean.setSendStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        resBean.setLimitNum(limitNum);
        return resBean;
    }



    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @param workType - 补充数据的类型：1初始化，2补充数据失败，3补充数据成功
     * @param sendStatus - 发送状态：0初始化，1锁定，2计算失败，3计算成功
     * @param orderStatus - 订单状态
     * @param info - 解析说明
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleTaskUpdateStatus(long id, int runStatus, int workType, int sendStatus,int orderStatus, String info){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        if (runStatus > 0){
            resBean.setRunStatus(runStatus);
            if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (sendStatus > 0){
            resBean.setSendStatus(sendStatus);
            if (sendStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(info)){
            resBean.setInfo(info);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询提现的查询条件
     * @param limitNum - 多少条数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleStatusQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setLimitNum(limitNum);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        return resBean;
    }

    /**
     * @Description: 组装查询定数据填充的查询条件
     * @param limitNum - 多少条数据
     * @param workType - 值以work形式填充计算,以后数据多起来则这些字段值填充由worker来跑数据：0初始化，1填充完毕，2无需下发数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
    */
    public static StatusModel assembleDeductRatioStatusQuery(int limitNum, int workType){
        StatusModel resBean = new StatusModel();
        resBean.setWorkType(workType);
        resBean.setLimitNum(limitNum);
        return resBean;
    }


    /**
     * @Description: 组装查询定数据同步给渠道数据的查询条件
     * @param limitNum - 多少条数据
     * @param workType - 值以work形式填充计算,以后数据多起来则这些字段值填充由worker来跑数据：0初始化，1填充完毕，2无需下发数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleNotifyStatusQuery(int limitNum, int workType){
        StatusModel resBean = new StatusModel();
        resBean.setWorkType(workType);
        resBean.setLimitNum(limitNum);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        return resBean;
    }


    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleUpdateStatusModel(long id, int runStatus){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        resBean.setRunStatus(runStatus);
        if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 表示失败：失败则需要运行次数加一
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        }
        return resBean;
    }




    /**
     * @Description: 组装查询提现的查询条件
     * @param limitNum - 多少条数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleWithdrawStatusQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setLimitNum(limitNum);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        return resBean;
    }


    /**
     * @Description: 组装查询支付宝订单同步的数据的查询条件
     * @param limitNum
     * @return StatusModel
     * @author yoko
     * @date 2019/12/6 22:48
     */
    public static StatusModel assembleTaskAlipayNotifyStatusQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setLimitNum(limitNum);
        return resBean;
    }


}
