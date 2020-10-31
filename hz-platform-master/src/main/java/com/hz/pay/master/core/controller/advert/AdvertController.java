package com.hz.pay.master.core.controller.advert;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.hz.pay.master.core.common.controller.BaseController;
import com.hz.pay.master.core.common.exception.ExceptionMethod;
import com.hz.pay.master.core.common.utils.JsonResult;
import com.hz.pay.master.core.common.utils.SignUtil;
import com.hz.pay.master.core.common.utils.StringUtil;
import com.hz.pay.master.core.common.utils.constant.ServerConstant;
import com.hz.pay.master.core.model.RequestEncryptionJson;
import com.hz.pay.master.core.model.ResponseEncryptionJson;
import com.hz.pay.master.core.protocol.request.advert.RequestAdvert;
import com.hz.pay.master.util.ComponentUtil;
import com.hz.pay.master.util.HodgepodgeMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description 广告的Controller层
 * @Author yoko
 * @Date 2020/3/4 10:42
 * @Version 1.0
 */
@RestController
@RequestMapping("/ad/ad")
public class AdvertController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(AdvertController.class);

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

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;




    /**
     * @Description: 首页-开始浏览广告
     * <p>
     *     开始浏览：A.判断用户是否登录。
     *     B.判断是否有足够的浏览次数。
     *     C.添加广告观看纪录。
     *     D.随机生成每次浏览广告的收益。
     *     E.添加用户收益。
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/ad/ad/watch
     * 请求的属性类:RequestAppeal
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 客户端加密字段:token+ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     *
     * {
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiN2UyZDhlODA4NWRhNzE5YzAzNWU2NTNiNGZmZmUzYjYiLCJzdGltZSI6MTU4MzMwNTc0NTQzOSwid2giOnsiYWRBZHMiOiJodHRwOi8vd3d3LnFpZGlhbi5jb20iLCJwcm9maXQiOiIwLjMyIn19"
     *     },
     *     "sgid": "202003041509030000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/watch", method = {RequestMethod.POST})
    public JsonResult<Object> watch(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RequestAdvert requestModel = new RequestAdvert();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestAdvert.class);
            // #零时数据
//            ComponentUtil.redisService.set(requestModel.token, "3");
            // check校验数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(token, stime, secretKeySign); // stime+秘钥=sign
            String strData = "";
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // #添加流水
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this AdvertController.watch() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    @PostMapping(value = "/data")
    public JsonResult<Object> data(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> obj) throws Exception{
        //TempData
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
//        request.getParameterMap()
//        Map<String, Object> mapData = getObjectMap(request);
        try{
            log.error("AdvertController.data():" + JSON.toJSON(obj));
            // #添加流水
            // 返回数据给客户端
            return JsonResult.successResult(obj, cgid, sgid);
//            return JsonResult.successResult(null, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this AdvertController.data() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 接收阿里支付宝的数据-APP
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/ad/ad/postNotify
     */
    @RequestMapping(value = "/postNotify", method = {RequestMethod.POST})
//    public JsonResult<Object> notify(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
    public void postNotify(HttpServletRequest request, HttpServletResponse response, @RequestBody Object obj) throws Exception{
        try{
            log.error("AdvertController.postNotify()----------进来了!");
            log.error("AdvertController.postNotify():" + JSON.toJSON(obj));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * @Description: 接收阿里支付宝的数据-APP
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/ad/ad/getNotify
     */
    @RequestMapping(value = "/getNotify", method = {RequestMethod.GET})
//    public JsonResult<Object> notify(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
    public void getNotify(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> obj) throws Exception{
        try{
            log.error("AdvertController.getNotify()----------进来了!");
            log.error("AdvertController.getNotify():" + JSON.toJSON(obj));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
