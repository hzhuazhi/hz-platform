package com.hz.platform.master.core.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 页面工具类
 */
public class HtmlUtil {

	/**
	 * 
	 * <br>
	 * <b>功能：</b>输出json格式<br>
	 * @param response
	 * @param jsonStr
	 * @throws Exception
	 */
	public static void writerJson(HttpServletResponse response, String jsonStr) {
		writer(response, jsonStr);
	}

	public static void writerJson(HttpServletResponse response, Object object) {
		try {
			response.setContentType("application/json");
			writer(response, JSON.toJSONString(object));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void writerJson(HttpServletResponse response, int count, List<?> data) {
		try {
			response.setContentType("application/json");
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", count);
			jsonMap.put("rows", data);
			writer(response, JSON.toJSONString(jsonMap));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>输出HTML代码<br>
	 * @param response
	 * @param htmlStr
	 * @throws Exception
	 */
	public static void writerHtml(HttpServletResponse response, String htmlStr) {
		writer(response, htmlStr);
	}

	private static void writer(HttpServletResponse response, String str) {
		try {
			//设置页面不缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = null;
			out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
