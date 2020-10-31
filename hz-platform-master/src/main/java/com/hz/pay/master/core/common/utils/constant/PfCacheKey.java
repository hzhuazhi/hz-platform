package com.hz.pay.master.core.common.utils.constant;

/**
 * @Description 支付业务的缓存key
 * @Author yoko
 * @Date 2019/11/21 16:03
 * @Version 1.0
 */
public interface PfCacheKey {

    /**
     * 用户在操作他的钻石，需要对其进行锁定
     * 所以在变量名称前加了lock
     */

    String LOCK_CONSUMER = "-1";

}
