package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.*;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.datacoreout.DataCoreOutModel;
import com.hz.platform.master.core.model.geway.GewayProfitModel;
import com.hz.platform.master.core.service.TaskDataCoreOutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task：代付订单数据回传的Service层的实现层
 * @Author yoko
 * @Date 2020/11/1 14:19
 * @Version 1.0
 */
@Service
public class TaskDataCoreOutServiceImpl<T> extends BaseServiceImpl<T> implements TaskDataCoreOutService<T> {

    private final static Logger log = LoggerFactory.getLogger(TaskDataCoreOutServiceImpl.class);
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskDataCoreOutMapper taskDataCoreOutMapper;

    @Autowired
    private ChannelBalanceDeductMapper channelBalanceDeductMapper;

    @Autowired
    private ChannelOutMapper channelOutMapper;

    @Autowired
    private GewayProfitMapper gewayProfitMapper;

    @Autowired
    private AgentProfitMapper agentProfitMapper;

    public BaseDao<T> getDao() {
        return taskDataCoreOutMapper;
    }

    @Override
    public List<DataCoreOutModel> getDataList(Object obj) {
        return taskDataCoreOutMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskDataCoreOutMapper.updateStatus(obj);
    }

    @Override
    public boolean handleDataCoreOut(ChannelBalanceDeductModel channelBalanceDeductModel, ChannelOutModel channelOutModel, GewayProfitModel gewayProfitModel, List<AgentProfitModel> agentProfitList) throws Exception {
        int num1 = channelBalanceDeductMapper.updateOrderStatusByOrderNo(channelBalanceDeductModel);
        int num2 = channelOutMapper.updateOrderStatusByOrderNo(channelOutModel);
        int num3 = gewayProfitMapper.add(gewayProfitModel);
        int num4 = 0;
        if (agentProfitList == null || agentProfitList.size() <= 0){
            num4 = 1;
            log.info("");
        }else {
            num4 = agentProfitMapper.addBatchAgentProfit(agentProfitList);
            if (num4 != agentProfitList.size()){
                // 说明批量查询的数据影响条数与集合的数据条数不一
                num4 = 0;
            }
        }

        if (num1 > 0 && num2 > 0 && num3 > 0 && num4 > 0){
            return true;
        }else{
            throw new ServiceException("handleDataCoreOut", "四个执行更新SQL其中有一个或者多个响应行为0");
        }
    }
}
