package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.channel.ChannelModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 渠道的Dao层
 * @Author yoko
 * @Date 2020/3/2 17:28
 * @Version 1.0
 */
@Mapper
public interface ChannelMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新渠道的余额
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
    public int updateBalance(ChannelModel model);

    /**
     * @Description: 更新渠道的锁定金额
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/3 11:17
    */
    public int updateLockMoney(ChannelModel model);



}
