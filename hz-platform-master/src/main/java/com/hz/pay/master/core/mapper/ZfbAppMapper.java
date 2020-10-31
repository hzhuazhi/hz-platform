package com.hz.pay.master.core.mapper;

import com.hz.pay.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 支付宝账号的Dao层
 * @Author yoko
 * @Date 2020/4/9 17:01
 * @Version 1.0
 */
@Mapper
public interface ZfbAppMapper <T> extends BaseDao<T> {
}
