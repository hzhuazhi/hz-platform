package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.geway.GewayModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 通道的Dao层
 * @Author yoko
 * @Date 2020/3/24 15:40
 * @Version 1.0
 */
@Mapper
public interface GewayMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新通道的余额
     * <p>
     *     余额加减，
     *     字段addBalance不为空，则余额加；
     *     字段subtractBalance不为空，则余额减
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/10/30 17:04
     */
    public int updateBalance(GewayModel model);


    /**
     * @Description: 更新通道的总额额
     * <p>
     *     总额加减，
     *     字段addBalance不为空，则总额加；
     *     字段subtractBalance不为空，则总额减
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/10/30 17:04
     */
    public int updateTotalMoney(GewayModel model);
}
