package com.hz.platform.master.core.controller.channel;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.controller.BaseController;
import com.hz.platform.master.core.common.exception.ExceptionMethod;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.utils.JsonResult;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.protocol.request.pay.RequestPayOut;
import com.hz.platform.master.core.protocol.response.channel.ResponseChannel;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author yoko
 * @desc 渠道的Controller层
 * @create 2021-12-06 11:29
 **/
@RestController
@RequestMapping("/platform/channel")
public class ChannelController  extends BaseController {

    private static Logger log = LoggerFactory.getLogger(ChannelController.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    /**
     * 30分钟.
     */
    public long THIRTY_MIN = 30;



    /**
     * @Description: 查询余额
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/channel/queryBalance
     * 请求的属性类:RequestPayOut
     * 必填字段:{"channel":"10102","ctime":20211206112206,"sign":"30ac3a8fd96640508c4ce1022d3a9089"}
     *
     * {
     *     "code": "0",
     *     "message": "success",
     *     "data": {
     *         "channel_balance": "4598",
     *         "channel_lock_money": "1803.0000"
     *     }
     * }
     *
     *
     */
    @RequestMapping(value = "/queryBalance", method = {RequestMethod.POST})
    public JsonResult<Object> queryBalance(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestPayOut requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        try{

            if (requestData == null){
                throw new ServiceException("50001", "协议数据不能为空!");
            }
            log.info("-----ChannelController--queryBalance--all-data-json:" + JSON.toJSONString(requestData));

            // check商铺号
            if (StringUtils.isBlank(requestData.channel)){
                throw new ServiceException("50002", "商家号不能为空!");
            }

            // check访问时间
            if (requestData.ctime == null || requestData.ctime <= 0){
                throw new ServiceException("50003", "访问时间不能为空!");
            }

            // check签名
            if (StringUtils.isBlank(requestData.sign)){
                throw new ServiceException("50004", "秘钥不能为空!");
            }

            // check校验是否频繁查询
            HodgepodgeMethod.checkFrequentlyByQueryBalance(requestData.channel);

            // 校验请求时间
            HodgepodgeMethod.checkCtime(requestData.ctime);


            // 获取渠道的信息
            ChannelModel channelModel = new ChannelModel();
            channelModel.setChannel(requestData.channel);
            channelModel = (ChannelModel) ComponentUtil.channelService.findByObject(channelModel);
            if (channelModel == null || channelModel.getId() <= 0){
                throw new ServiceException("0010", "请填写正确的商家号!");
            }

            // 校验是否是白名单IP
            if (!StringUtils.isBlank(channelModel.getWhiteListIp())){
                HodgepodgeMethod.checkWhiteListIp(channelModel.getWhiteListIp(), ip);
            }

            // 校验sign签名
            HodgepodgeMethod.checkSignByChannel(requestData, channelModel.getSecretKey());


            // 组装要返回渠道的数据
            ResponseChannel responseChannel = HodgepodgeMethod.assembleResponseChannelResult(channelModel);


            // 返回数据给客户端
            return JsonResult.successResult(responseChannel);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this ChannelController.queryBalance() is error , the data=%s!", JSON.toJSONString(requestData)));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }finally {
            // redis 保存渠道号，check频繁操作查询渠道余额
            HodgepodgeMethod.saveFrequentlyByQueryBalance(requestData.channel);
        }
    }
}
