package com.hz.platform.master.core.protocol.response.channel;

import java.io.Serializable;

/**
 * @author yoko
 * @desc 协议-查询渠道返回的数据
 * @create 2021-12-06 14:33
 **/
public class ResponseChannel implements Serializable {
    private static final long   serialVersionUID = 9031023131150L;

    public ResponseChannel(){

    }

    public String channel_balance;// 余额
    public String channel_lock_money;// 锁定金额：正在出款中的单子的金额总和；如果有出款失败的单子，则锁定的订单金额会返回到余额中


    public String getChannel_balance() {
        return channel_balance;
    }

    public void setChannel_balance(String channel_balance) {
        this.channel_balance = channel_balance;
    }

    public String getChannel_lock_money() {
        return channel_lock_money;
    }

    public void setChannel_lock_money(String channel_lock_money) {
        this.channel_lock_money = channel_lock_money;
    }
}
