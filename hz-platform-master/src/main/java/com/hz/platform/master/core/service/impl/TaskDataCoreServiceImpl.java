package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.AgentProfitMapper;
import com.hz.platform.master.core.mapper.ChannelProfitMapper;
import com.hz.platform.master.core.mapper.GewayProfitMapper;
import com.hz.platform.master.core.mapper.TaskDataCoreMapper;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.channel.ChannelProfitModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.geway.GewayProfitModel;
import com.hz.platform.master.core.service.AgentProfitService;
import com.hz.platform.master.core.service.TaskDataCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description task:通道回调的成功数据的Service层的实现层
 * @Author yoko
 * @Date 2021/1/19 18:42
 * @Version 1.0
 */
@Service
public class TaskDataCoreServiceImpl<T> extends BaseServiceImpl<T> implements TaskDataCoreService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskDataCoreMapper taskDataCoreMapper;

    @Autowired
    private AgentProfitMapper agentProfitMapper;

    @Autowired
    private GewayProfitMapper gewayProfitMapper;

    @Autowired
    private ChannelProfitMapper channelProfitMapper;



    public BaseDao<T> getDao() {
        return taskDataCoreMapper;
    }

    @Override
    public List<DataCoreModel> getDataList(Object obj) {
        return taskDataCoreMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskDataCoreMapper.updateStatus(obj);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleSuccessOrder(List<AgentProfitModel> agentProfitList, GewayProfitModel gewayProfitModel, ChannelProfitModel channelProfitModel) throws Exception {
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        try {

            if (agentProfitList == null || agentProfitList.size() <= 0){
                num1 = 1;
            }else {
                num1 = agentProfitMapper.addBatchAgentProfit(agentProfitList);
                if (num1 != agentProfitList.size()){
                    // 说明批量查询的数据影响条数与集合的数据条数不一
                    num1 = 0;
                }
            }

            if (gewayProfitModel == null){
                num2 = 1;
            }else {
                num2 = gewayProfitMapper.add(gewayProfitModel);
            }

            num3 = channelProfitMapper.add(channelProfitModel);

            if (num1> 0 && num2 > 0 && num3 > 0 ){
                return true;
            }else {
                throw new ServiceException("handleSuccessOrder", "三个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("handleSuccessOrder", "执行时,出现未抓取到的错误");
        }
    }
}
