package com.hz.platform.master.core.controller.whitelist;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.exception.ExceptionMethod;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.utils.HttpSendUtils;
import com.hz.platform.master.core.common.utils.JsonResult;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.zhongbang.ZbWhitelistModel;
import com.hz.platform.master.core.protocol.request.whitelist.RequestWhitelist;
import com.hz.platform.master.core.protocol.response.whitelist.ResponseWhitelist;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author yoko
 * @desc 众邦白名单添加
 * @create 2022-07-05 14:59
 **/
@RestController
@RequestMapping("/platform/whitelist")
public class WhitelistController {

    private static Logger log = LoggerFactory.getLogger(WhitelistController.class);

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
     * @Description: 众邦添加白名单
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/whitelist/add
     * 请求的属性类:RequestWhitelist
     * 必填字段:{"channel":"10101","userSerialNo":"userSerialNo1","userName":"userName1","cardNum":"cardNum1","cardImgFront":"cardImgFront1","cardImgBack":"cardImgBack1","phoneNum":"phoneNum1","bankCard":"bankCard1","notifyUrl":"channel_notify_Url1","sign":"bb2e8d1656d3a3ab4455bfb5f7546c48"}
     * 加密字段:{"jsonData":"eyJ1c2VyU2VyaWFsTm8iOiJ1c2VyU2VyaWFsTm8xIiwidXNlck5hbWUiOiJ1c2VyTmFtZTEiLCJjYXJkTnVtIjoiY2FyZE51bTEiLCJjYXJkSW1nRnJvbnQiOiJjYXJkSW1nRnJvbnQxIiwiY2FyZEltZ0JhY2siOiJjYXJkSW1nQmFjazEiLCJwaG9uZU51bSI6InBob25lTnVtMSIsImJhbmtDYXJkIjoiYmFua0NhcmQxIiwic2lnbiI6InNpZ24xIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siY29udGVudCI6IiIsImNyZWF0ZVRpbWUiOiIyMDIwLTAxLTIwIDE4OjMwOjMwIiwiaWNvbkFkcyI6Imh0dHA6Ly9xNmxta3U4dTcuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wM18wMyUyRjY3NmNmNTE0NTFjMDY0ZTkzMTc2ZTM5NmZjZThlOTcyNjM2MTlhZjYuanBnIiwiaWQiOjExLCJwYWdlQWRzIjoiaHR0cDovL3E2bG1rdTh1Ny5ia3QuY2xvdWRkbi5jb20vdXBsb2FkJTJGaW1hZ2UlMkYyMDIwXzAzXzAzJTJGZWU3NTIwZWQyMzcwNTFiMDg2MmExZjkzNmIwMzFkNjU3YTBiMmU4Yi5wbmciLCJza2V0Y2giOiLnrKzkuIDmnaHlhazlkYoiLCJ0aXRsZSI6IjUwMOeQhui0oiDor5Xov5DooYzpgJrnn6UifSx7ImNvbnRlbnQiOiLllabllabllaYiLCJjcmVhdGVUaW1lIjoiMjAyMC0wMy0wMyAxNTo0ODo0NCIsImljb25BZHMiOiJodHRwOi8vcTZsbWt1OHU3LmJrdC5jbG91ZGRuLmNvbS91cGxvYWQlMkZpbWFnZSUyRjIwMjBfMDNfMDMlMkY2NzZjZjUxNDUxYzA2NGU5MzE3NmUzOTZmY2U4ZTk3MjYzNjE5YWY2LmpwZyIsImlkIjoxMiwicGFnZUFkcyI6Imh0dHA6Ly9xNmxta3U4dTcuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wM18wMyUyRmVlNzUyMGVkMjM3MDUxYjA4NjJhMWY5MzZiMDMxZDY1N2EwYjJlOGIucG5nIiwic2tldGNoIjoi5rWL6K+V5ZOmMzMiLCJ0aXRsZSI6Iua1i+ivlTIyIn0seyJjb250ZW50IjoiMzEzMTMxMiIsImNyZWF0ZVRpbWUiOiIyMDIwLTAzLTAzIDE2OjE5OjM2IiwiaWNvbkFkcyI6Imh0dHA6Ly9xNmxta3U4dTcuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wM18wMyUyRjcwMzVjODQxZTAzNWQ0MjVlNjU0NzdjMjk1ZjMzZjQ5MDdhYmFmY2EuanBnIiwiaWQiOjEzLCJwYWdlQWRzIjoiIiwic2tldGNoIjoiMzEzMSIsInRpdGxlIjoiMzEzMSJ9XSwicm93Q291bnQiOjUsInNpZ24iOiIwZGRlZTZkOTNhZjliZWMxMzY1OGFhM2ZiY2U3YWM4ZSIsInN0aW1lIjoxNTkzODYxNjczODMyfQ=="
     *     },
     *     "sgid": "202007041921130000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public JsonResult<Object> add(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestWhitelist requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        try{

            // check校验数据
            HodgepodgeMethod.checkRequestWhitelistData(requestData);

            // 获取渠道的信息
            ChannelModel channelModel = new ChannelModel();
            channelModel.setChannel(requestData.channel);
            channelModel = (ChannelModel) ComponentUtil.channelService.findByObject(channelModel);
            if (channelModel == null || channelModel.getId() <= 0){
                throw new ServiceException("ZBZ0001", "请填写正确的商家号!");
            }

            // 组装要添加的众邦白名单数据
            ZbWhitelistModel zbWhitelistModel = HodgepodgeMethod.assembleZbWhitelistAdd(requestData, channelModel.getId());
            // 添加众邦白名单数据
            int num = ComponentUtil.zbWhitelistService.add(zbWhitelistModel);
            if (num > 0){

                // 蛋糕-白名单添加
                Map<String ,Object> sendDataMap = new HashMap<>();
                sendDataMap.put("userSerialNo", zbWhitelistModel.getSerialNo());
                sendDataMap.put("userName", zbWhitelistModel.getUserName());
                sendDataMap.put("cardNum", zbWhitelistModel.getCardNum());
                sendDataMap.put("cardImgFront", zbWhitelistModel.getCardImgFront());
                sendDataMap.put("cardImgBack", zbWhitelistModel.getCardImgBack());
                sendDataMap.put("phoneNum", zbWhitelistModel.getPhoneNum());
                sendDataMap.put("bankCard", zbWhitelistModel.getBankCard());
                sendDataMap.put("notifyUrl", "http://localhost:8092/platform/whitelist/notify");

                String parameter = JSON.toJSONString(sendDataMap);
                parameter = StringUtil.mergeCodeBase64(parameter);
                Map<String, String> sendMap = new HashMap<>();
                sendMap.put("jsonData", parameter);
                String sendData = JSON.toJSONString(sendMap);
                String resData = HttpSendUtils.sendPostAppJson("http://localhost:8093/cake/whitelist/add", sendData);
                Map<String, Object> resMap = new HashMap<>();
                if (!StringUtils.isBlank(resData)) {
                    resMap = JSON.parseObject(resData, Map.class);
                    if (resMap.get("resultCode").equals("0")) {
                        return JsonResult.successResult(null);
                    }else{
                        return JsonResult.failedResult("添加白名单数据失败,请重试!", "-256");
                    }
                }else {
                    return JsonResult.failedResult("添加白名单数据失败,请重试!", "-256");
                }

            }else {
                return JsonResult.failedResult("添加白名单数据失败!", "-256");
            }

        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this WhitelistController.add() is error , the all data=%s!", data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this WhitelistController.add() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }





    /**
     * @Description: 蛋糕-众邦白名单回调
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/whitelist/notify
     *
     * {"user_serial_no":"user_serial_no1","order_status":2,"res_code":"res_code_1","res_explain":"res_explain_1"}
     */
    @RequestMapping(value = "/notify", method = {RequestMethod.POST})
    public String notify(HttpServletRequest request, HttpServletResponse response,@RequestBody ResponseWhitelist requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        try{

            if(requestData == null){
                return "no";
            }

            // 查询是否有这个白名单记录
            ZbWhitelistModel zbWhitelistQuery = new ZbWhitelistModel();
            zbWhitelistQuery.setSerialNo(requestData.user_serial_no);

            ZbWhitelistModel zbWhitelistModel = (ZbWhitelistModel)ComponentUtil.zbWhitelistService.findByObject(zbWhitelistQuery);
            if (zbWhitelistModel == null){
                return "no";
            }

            // 组装更新白名单的状态
            ZbWhitelistModel zbWhitelistUpdate = HodgepodgeMethod.assembleZbWhitelistUpdate(requestData);
            int num = ComponentUtil.zbWhitelistService.updateResultInfo(zbWhitelistUpdate);
            if (num > 0){
                return "ok";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this WhitelistController.notify() is error , the all data=%s!", data));
            e.printStackTrace();
            return "no";
        }
    }


}
