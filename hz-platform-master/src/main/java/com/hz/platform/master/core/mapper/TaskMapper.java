package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.task.TaskAlipayNotifyModel;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/3/25 16:13
 * @Version 1.0
 */
@Mapper
public interface TaskMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询要填充的数据集合
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:28
     */
    public List<DataCoreModel> getWorkTypeList(Object obj);

    /**
     * @Description: 更新填充数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateWorkType(Object obj);

    /**
     * @Description: 更新渠道的总账以及余额
     * @param obj
     * @return
     * @author yoko
     * @date 2020/3/25 16:27
     */
    public int updateChannelMoney(Object obj);

    /**
     * @Description: 查询要同步给下游的数据集合
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:28
     */
    public List<DataCoreModel> getNotifyList(Object obj);

    /**
     * @Description: 更新同步数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateNotifyStatus(Object obj);


    /**
     * @Description: 查询要提现的数据集合
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:28
     */
    public List<WithdrawModel> getWithdrawList(Object obj);

    /**
     * @Description: 更新提现数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateWithdrawStatus(Object obj);

    /**
     * @Description: 更新渠道的余额-提现：减法
     * @param obj
     * @return
     * @author yoko
     * @date 2020/3/25 16:27
     */
    public int updateChannelReduceMoney(Object obj);


    /**
     * @Description: 更新代理的余额-提现：减法
     * @param obj
     * @return
     * @author yoko
     * @date 2020/3/25 16:27
     */
    public int updateAgentReduceMoney(Object obj);


    /**
     * @Description: 根据条件查询阿里订单同步数据需要跑task的数据：runStatus属于初始化值
     * @param obj - 查询条件
     * @return List
     * @author yoko
     * @date 2019/12/27 22:24
     */
    public List<TaskAlipayNotifyModel> getTaskAlipayNotify(Object obj);

    /**
     * @Description: 更新阿里订单同步数据task的状态
     * @param obj - 更新状态
     * @return int
     * @author yoko
     * @date 2019/12/27 22:26
     */
    public int updateTaskAlipayNotifyStatus(Object obj);
}
