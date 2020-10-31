package com.hz.platform.master.core.protocol.response.advert;

import com.hz.platform.master.core.protocol.base.BaseResponse;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/3/4 10:59
 * @Version 1.0
 */
public class ResponseAdvert extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233003331141L;
    public AdvertWatch wh;
    public Integer rowCount;
    public ResponseAdvert(){

    }

    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public AdvertWatch getWh() {
        return wh;
    }

    public void setWh(AdvertWatch wh) {
        this.wh = wh;
    }
}
