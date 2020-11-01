package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task：渠道扣减余额流水的Dao层
 * @Author yoko
 * @Date 2020/11/1 17:12
 * @Version 1.0
 */
@Mapper
public interface TaskChannelBalanceDeductMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的渠道扣款流水信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ChannelBalanceDeductModel> getDataList(Object obj);

    /**
     * @Description: 更新渠道扣款流水信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
