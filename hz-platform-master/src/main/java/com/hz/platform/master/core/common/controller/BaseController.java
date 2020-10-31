package com.hz.platform.master.core.common.controller;

import com.hz.platform.master.core.common.utils.HtmlUtil;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * 基础Controller
 * 
 * @author df
 *@since 2016-05-05
 * 
 */
public class BaseController {

	public final static String CODE = "code";
	public final static String SUCCESS = "success";
	public final static String MSG = "msg";
	public final static String DF = "df";

	public final static String DATA = "data";

	public final static String LOGOUT_FLAG = "logoutFlag";

	public static String actionStr = "";

	public static Logger log = Logger.getLogger("BaseController");


	/**
	 * 获取IP地址
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 所有ActionMap 统一从这里获取
	 * 
	 * @return
	 */
	public Map<String, String> getRootMap(HttpServletRequest request) {
		Map<String, String> rootMap = new HashMap<String, String>();
		String str = request.getQueryString();
		if (str != null) {
			String[] param = str.split("&");
			for (int i = 0; i < param.length; i++) {
				if (param[i] != null && param[i].indexOf("=") >= 0) {
					String[] params = param[i].split("=");
					if (params.length == 2) {

						try {
							rootMap.put(params[0], java.net.URLDecoder.decode(
									params[1], "utf-8"));
						} catch (UnsupportedEncodingException e) {
							log.info("str 参数异常:" + str);
							return new HashMap<String, String>();
						}
					}
				}
			}
		}
		return rootMap;
	}
	
	
	/**
	 * 所有ActionMap 统一从这里获取
	 * 
	 * @return
	 */
	public Map<String, Object> getObjectMap(HttpServletRequest request) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		String str = request.getQueryString();
		if (str != null) {
			String[] param = str.split("&");
			for (int i = 0; i < param.length; i++) {
				if (param[i] != null && param[i].indexOf("=") >= 0) {
					String[] params = param[i].split("=");
					if (params.length == 2) {

						try {
							objectMap.put(params[0], java.net.URLDecoder.decode(
									params[1], "utf-8"));
						} catch (UnsupportedEncodingException e) {
							log.info("str 参数异常:" + str);
							return new HashMap<String, Object>();
						}
					}
				}
			}
		}
		return objectMap;
	}
	

	@SuppressWarnings("unchecked")
	public static Object getRootBean(Class type, HttpServletRequest request) {
		Object obj = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
			obj = type.newInstance(); // 创建 JavaBean 对象
			// 给 JavaBean 对象的属性赋值
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			String str = request.getQueryString();
			log.info("请求参数:"+str+"&转化对象"+type.getName());
			for (int j = 0; j < propertyDescriptors.length; j++) {
				PropertyDescriptor descriptor = propertyDescriptors[j];
				String propertyName = descriptor.getName(); 
				if (str != null) {
					String[] param = str.split("&");
					for (int i = 0; i < param.length; i++) {
						if (param[i] != null && param[i].indexOf("=") >= 0) {
							String[] params = param[i].split("=");
							if (params.length == 2) {
								if (propertyName.toLowerCase().equals(
										params[0].toLowerCase())) {
									Object[] args = new Object[1];
									args[0] = java.net.URLDecoder.decode(
											params[1], "gbk");
									descriptor.getWriteMethod().invoke(obj,
											args);
								}
							}
						}
					}
				}
			}
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			log.info("参数转对象异常：IntrospectionException");
		//	e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			log.info("参数转对象异常：IllegalAccessException");
			//e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			log.info("参数转对象异常：InstantiationException");
			//e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block			e1.printStackTrace();
			log.info("参数转对象异常：InvocationTargetException");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.info("参数转对象异常：UnsupportedEncodingException");
			//e.printStackTrace();
		}
		return obj;
	}

	public ModelAndView forword(String viewName, Map<String, Object> context) {
		return new ModelAndView(viewName, context);
	}

	public ModelAndView error(String errMsg) {
		return new ModelAndView("error");
	}

	/**
	 * 
	 * 提示成功信息
	 * 
	 * @param message
	 * 
	 */
	public void sendSuccessMessage(HttpServletResponse response, String message) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, true);
		result.put(MSG, message);
		HtmlUtil.writerJson(response, result);
	}

	/**
	 * 提示成功信息
	 * 
	 * @param response
	 * @param message
	 * @param data
	 *            附带的数据
	 */
	public void sendSuccessMessage(HttpServletResponse response, String code,
                                   String message, Object data) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(CODE, code);
		result.put(SUCCESS, true);
		result.put(MSG, message);
		result.put(DATA, data);
		HtmlUtil.writerJson(response, result);
	}

	/**
	 * 
	 * 提示失败信息
	 * 
	 * @param message
	 * 
	 */
	public void sendFailureMessage(HttpServletResponse response, String message) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, false);
		result.put(MSG, message);
		HtmlUtil.writerJson(response, result);
	}

	/**
	 *
	 * 提示失败信息
	 *
	 * @param message
	 *
	 */
	public void sendFailureMessage(HttpServletResponse response, String code, String message, String df) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(CODE, code);
		result.put(SUCCESS, false);
		result.put(MSG, message);
		result.put(DF, df);
		HtmlUtil.writerJson(response, result);
	}
}
