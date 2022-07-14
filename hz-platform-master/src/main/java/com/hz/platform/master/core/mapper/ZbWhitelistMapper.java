package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.zhongbang.ZbWhitelistModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yoko
 * @desc 众邦白名单的Dao层
 * @create 2022-07-07 13:37
 **/
@Mapper
public interface ZbWhitelistMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新众邦白名单添加后返回的结果信息
     * @param model
     * @author: yoko
     * @date: 2022/7/14 14:10
     * @version 1.0.0
     */
    public int updateResultInfo(ZbWhitelistModel model);
}
