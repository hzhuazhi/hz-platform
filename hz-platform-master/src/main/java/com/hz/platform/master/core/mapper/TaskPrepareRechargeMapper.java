package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.preparerecharge.PrepareRechargeModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task：代付预备充值表:预备给渠道进行充值的Dao层
 * @Author yoko
 * @Date 2020/11/1 14:09
 * @Version 1.0
 */
@Mapper
public interface TaskPrepareRechargeMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的代付预备充值信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<PrepareRechargeModel> getDataList(Object obj);

    /**
     * @Description: 更新代付预备充值信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
