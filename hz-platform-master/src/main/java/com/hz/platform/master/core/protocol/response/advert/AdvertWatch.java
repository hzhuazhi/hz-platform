package com.hz.platform.master.core.protocol.response.advert;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/3/4 14:36
 * @Version 1.0
 */
public class AdvertWatch implements Serializable {
    private static final long   serialVersionUID = 1233013331141L;
    public String adAds;
    public String profit;
    public AdvertWatch(){

    }

    public String getAdAds() {
        return adAds;
    }

    public void setAdAds(String adAds) {
        this.adAds = adAds;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
