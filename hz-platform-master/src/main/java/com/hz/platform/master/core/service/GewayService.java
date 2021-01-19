package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.geway.GewayChangeModel;

/**
 * @Description 通道的Service层
 * @Author yoko
 * @Date 2020/3/24 15:40
 * @Version 1.0
 */
public interface GewayService<T> extends BaseService<T> {

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
    public int updateBalance(GewayChangeModel model);


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
    public int updateTotalMoney(GewayChangeModel model);
}
