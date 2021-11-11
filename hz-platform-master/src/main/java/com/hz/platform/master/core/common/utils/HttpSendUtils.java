package com.hz.platform.master.core.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @ClassName: HttpSendUtils
 * @Description:Http协议
 * @author df
 * @date 2014-9-23 上午10:26:41
 */
public class HttpSendUtils {

	private static final int TIME_OUT = 10;
	
	private static final int READ_TIME_OUT = 20;
	
	//返回状态码
	public static boolean sendGetCode(String httpUrl, String odno, Map<String, String> parameter , Map<String, String> headerMap) {
		return sendGetCode(httpUrl, "UTF-8", odno, parameter, headerMap);
	}
	
	public static boolean sendGetCode(String httpUrl, String encoding, String odno, Map<String, String> parameter , Map<String, String> headerMap){
		if (httpUrl == null) {
			return false;
		}
		String urlStr = null;
		StringBuilder sb = null;
		if(parameter != null){
			sb = new StringBuilder();
			Iterator<Entry<String, String>> iterator = parameter.entrySet().iterator();
			while (iterator.hasNext()) {
				if (sb.length() > 0) {
					sb.append('&');
				}
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value;
				try {
					value = URLEncoder.encode(entry.getValue(), encoding);
				} catch (UnsupportedEncodingException e) {
					value = "";
				}
				sb.append(key).append('=').append(value);
			}
		}
		
		if(sb != null){
			if (httpUrl.lastIndexOf('?') != -1) {
				urlStr = httpUrl + '&' + sb.toString();
			} else {
				urlStr = httpUrl + '?' + sb.toString();
			}
		}else{
			urlStr = httpUrl;
		}
		HttpURLConnection httpCon = null;
		int responseCode = 0;
		try {
			URL url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(READ_TIME_OUT * 1000);
			if(headerMap != null ){//
				Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator();
				while (headerIterator.hasNext()) {
					Entry<String, String> entry = headerIterator.next();
					//System.out.println(entry.getKey()+"--" + entry.getValue());
					//httpCon.setRequestProperty("Authorization", "Basic ZG9vcmNhY0Bkb29ybW9iaS5jb206ZG9vcjEyMw==");
					httpCon.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, 1024);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, 1024);
			}
			String responseBody =null;
			responseBody = new String(baos.toByteArray(), encoding);
			baos.close();
			responseCode = httpCon.getResponseCode();
		} catch (Exception e) {
			System.out.println(String.format("-->Exception:%s", httpUrl));
			e.printStackTrace();
		} finally {
			if (httpCon != null)
				httpCon.disconnect();
		}
		
		return HttpURLConnection.HTTP_OK == responseCode;
	}
	
	
	
	//返回状态码
	public static boolean sentPostCode(String httpUrl, String postBody, String encoding, Map<String, String> headerMap) {
		HttpURLConnection httpCon = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
			httpCon = (HttpURLConnection) url.openConnection();
		} catch (Exception e1) {
			return false;
		}
		if (httpCon == null) {
			return false;
		}
		httpCon.setDoOutput(true);
		httpCon.setConnectTimeout(TIME_OUT * 1000);
		httpCon.setReadTimeout(READ_TIME_OUT * 1000);
		httpCon.setDoOutput(true);
		httpCon.setUseCaches(false);
		try {
			httpCon.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			return false;
		}
		if (headerMap != null) {
			Iterator<Entry<String, String>> iterator = headerMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				httpCon.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		OutputStream output;
		try {
			output = httpCon.getOutputStream();
			output.write(postBody.getBytes(encoding));
		} catch (IOException e1) {
			return false;
		}
		
		try {
			output.flush();
			output.close();
		} catch (IOException e1) {
			return false;
		}
		try {
			int responseCode = httpCon.getResponseCode();
			return (HttpURLConnection.HTTP_OK == responseCode);
		} catch (IOException e) {
			e.printStackTrace();
		}   
		return false;
	}
	
	/**
	 * 通过HTTP GET 发送参数
	 * 
	 * @param httpUrl
	 * @param parameter
	 * @param headerMap
	 */
	public static String sendGet(String httpUrl, Map<String, String> parameter , Map<String, String> headerMap) {
		return sendGet(httpUrl, "UTF-8", parameter, headerMap);
	}
	
	public static String sendGet(String httpUrl,String encoding, Map<String, String> parameter , Map<String, String> headerMap){
		if (httpUrl == null) {
			System.out.println("");
			return null;
		}
		String urlStr = null;
		StringBuilder sb = null;
		if(parameter != null){
			sb = new StringBuilder();
			Iterator<Entry<String, String>> iterator = parameter.entrySet().iterator();
			while (iterator.hasNext()) {
				if (sb.length() > 0) {
					sb.append('&');
				}
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value;
				try {
					value = URLEncoder.encode(entry.getValue(), encoding);
				} catch (UnsupportedEncodingException e) {
					value = "";
				}
				sb.append(key).append('=').append(value);
			}
		}
		
		if(sb != null){
			if (httpUrl.lastIndexOf('?') != -1) {
				urlStr = httpUrl + '&' + sb.toString();
			} else {
				urlStr = httpUrl + '?' + sb.toString();
			}
		}else{
			urlStr = httpUrl;
		}
		HttpURLConnection httpCon = null;
		String responseBody = null;
		try {
			URL url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(READ_TIME_OUT * 1000);
			if(headerMap != null ){//
				Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator();
				while (headerIterator.hasNext()) {
					Entry<String, String> entry = headerIterator.next();
					//System.out.println(entry.getKey()+"--" + entry.getValue());
					//httpCon.setRequestProperty("Authorization", "Basic ZG9vcmNhY0Bkb29ybW9iaS5jb206ZG9vcjEyMw==");
					httpCon.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			
			// 开始读取返回的内容
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, 1024);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, 1024);
			}
			responseBody = new String(baos.toByteArray(), encoding);
			baos.close();
		} catch (Exception e) {
			System.out.println(String.format("-->Exception:%s", httpUrl));
			e.printStackTrace();
		} finally {
			if (httpCon != null)
				httpCon.disconnect();
		}
		return responseBody;
	}
	
	
	/**
	 * 通过HTTP GET 发送参数
	 * 
	 * @param httpUrl
	 * @param timeout
	 * @param parameter
	 * @param headerMap
	 */
	public static String sendGet(String httpUrl, int timeout, Map<String, String> parameter , Map<String, String> headerMap) {
		if (httpUrl == null) {
			System.out.println("");
			return null;
		}
		String urlStr = null;
		StringBuilder sb = null;
		if(parameter != null){
			sb = new StringBuilder();
			Iterator<Entry<String, String>> iterator = parameter.entrySet().iterator();
			while (iterator.hasNext()) {
				if (sb.length() > 0) {
					sb.append('&');
				}
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value;
				try {
					value = URLEncoder.encode(entry.getValue(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					value = "";
				}
				sb.append(key).append('=').append(value);
			}
		}
		
		if(sb != null){
			if (httpUrl.lastIndexOf('?') != -1) {
				urlStr = httpUrl + '&' + sb.toString();
			} else {
				urlStr = httpUrl + '?' + sb.toString();
			}
		}else{
			urlStr = httpUrl;
		}
		HttpURLConnection httpCon = null;
		String responseBody = null;
		try {
			URL url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setConnectTimeout(timeout * 1000);
			httpCon.setReadTimeout(READ_TIME_OUT * 1000);
			if(headerMap != null ){//
				Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator();
				while (headerIterator.hasNext()) {
					Entry<String, String> entry = headerIterator.next();
					//System.out.println(entry.getKey()+"--" + entry.getValue());
					//httpCon.setRequestProperty("Authorization", "Basic ZG9vcmNhY0Bkb29ybW9iaS5jb206ZG9vcjEyMw==");
					httpCon.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			
			// 开始读取返回的内容
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, 1024);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, 1024);
			}
			responseBody = new String(baos.toByteArray(), "UTF-8");
			baos.close();
		} catch (Exception e) {
			System.out.println(String.format("-->Exception:%s", httpUrl));
			e.printStackTrace();
		} finally {
			if (httpCon != null)
				httpCon.disconnect();
		}
		return responseBody;
	}
	
	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sentPost(String httpUrl, String postBody) {
		return sentPost(httpUrl, postBody, "UTF-8", null);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sentPost(String httpUrl, String postBody, String encoding) {
		return sentPost(httpUrl, postBody, encoding, null);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * @param httpUrl   目的地址
	 * @param postBody  post的包体
	 * @param headerMap 增加的Http头信息
	 * @return
	 */
	public static String sentPost(String httpUrl, String postBody, Map<String, String> headerMap) {
		return sentPost(httpUrl, postBody, "UTF-8", headerMap);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @param encoding
	 *            发送的内容的编码
	 * @param headerMap 增加的Http头信息          
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sentPost(String httpUrl, String postBody, String encoding, Map<String, String> headerMap) {
		HttpURLConnection httpCon = null;
		String responseBody = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {
			return null;
		}
		try {
			httpCon = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			return null;
		}
		if (httpCon == null) {
			return null;
		}
		httpCon.setDoOutput(true);
		httpCon.setConnectTimeout(TIME_OUT * 1000);
		httpCon.setReadTimeout(READ_TIME_OUT * 1000);
		httpCon.setDoOutput(true);
		httpCon.setUseCaches(false);
		try {
			httpCon.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			return null;
		}
		if (headerMap != null) {
			Iterator<Entry<String, String>> iterator = headerMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				httpCon.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		OutputStream output;
		try {
			output = httpCon.getOutputStream();
		} catch (IOException e1) {
			return null;
		}
		try {
			output.write(postBody.getBytes(encoding));
		} catch (UnsupportedEncodingException e1) {
			return null;
		} catch (IOException e1) {
			return null;
		}
		try {
			output.flush();
			output.close();
		} catch (IOException e1) {
			return null;
		}


		// 开始读取返回的内容
		InputStream in;
		try {
			in = httpCon.getInputStream();
		} catch (IOException e1) {
			return null;
		}
		/**
		 * 这个方法可以在读写操作前先得知数据流里有多少个字节可以读取。
		 * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，
		 * 但如果是用于网络操作，就经常会遇到一些麻烦。
		 * 比如，Socket通讯时，对方明明发来了1000个字节，但是自己的程序调用available()方法却只得到900，或者100，甚至是0，
		 * 感觉有点莫名其妙，怎么也找不到原因。
		 * 其实，这是因为网络通讯往往是间断性的，一串字节往往分几批进行发送。
		 * 本地程序调用available()方法有时得到0，这可能是对方还没有响应，也可能是对方已经响应了，但是数据还没有送达本地。
		 * 对方发送了1000个字节给你，也许分成3批到达，这你就要调用3次available()方法才能将数据总数全部得到。
		 * 
		 * 经常出现size为0的情况，导致下面readCount为0使之死循环(while (readCount != -1) {xxxx})，出现死机问题
		 */
		int size = 0;
		try {
			size = in.available();
		} catch (IOException e1) {
			return null;
		}
		if (size == 0) {
			size = 1024;
		}
		byte[] readByte = new byte[size];
		// 读取返回的内容
		int readCount = -1;
		try {
			readCount = in.read(readByte, 0, size);
		} catch (IOException e1) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (readCount != -1) {
			baos.write(readByte, 0, readCount);
			try {
				readCount = in.read(readByte, 0, size);
			} catch (IOException e) {
				return null;
			}
		}
		try {
			responseBody = new String(baos.toByteArray(), encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		} finally {
			if (httpCon != null) {
				httpCon.disconnect();
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
		
		return responseBody;
	}
	
	public static String httpsentPost(String httpUrl, Map<String,String> postBody, String encoding, Map<String, String> headerMap) {
		HttpClient httpClient = null;
		String responseString = "";
		try {			
			String memberid = postBody.get("memberid");
			String backurl = postBody.get("backurl");
			String webbackurl = postBody.get("webbackurl");
			String clienturl = postBody.get("clienturl");
			String type = postBody.get("type");
			String body = postBody.get("body");

			String amount = postBody.get("amount");
			String ordernum = postBody.get("ordernum");
			String openid = postBody.get("openid");
			String appid = postBody.get("appid");
			String code = postBody.get("code");
			String sign = postBody.get("sign");
			
			httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(httpUrl);
			
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("memberid",memberid));
			nvps.add(new BasicNameValuePair("backurl",backurl));
			nvps.add(new BasicNameValuePair("webbackurl",webbackurl));
			nvps.add(new BasicNameValuePair("clienturl",clienturl));
			nvps.add(new BasicNameValuePair("type", type));
			nvps.add(new BasicNameValuePair("body", body));
			nvps.add(new BasicNameValuePair("amount", amount));
			nvps.add(new BasicNameValuePair("ordernum", ordernum));
			nvps.add(new BasicNameValuePair("openid", openid));
			nvps.add(new BasicNameValuePair("appid", appid));
			nvps.add(new BasicNameValuePair("code", code));


			nvps.add(new BasicNameValuePair("sign",sign));
			post.setEntity(new UrlEncodedFormEntity(nvps,encoding));
			
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				responseString = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return responseString;
	}


	/**
	 * 使用HTTP POST 发送文本-----sp要求HTTP头中Content-Type要设置为application/json
	 * 
	 * @param url
	 *            发送的地址
	 * @param param
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sendPostAppJson(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
			String data = StringUtil.mergeCodeBase64("9e8c23f670efe7dba280b6a6" + ":" + "b1bcae8402ceaeb1e7928bd0");
			URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection(); 
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("Authorization", "Basic " + data);
            conn.setDefaultUseCaches(false);
            conn.setDoOutput(true);
//            conn.setDoInput(true); 
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8")); 
            out.print(param); 
            out.flush(); 
            StringBuffer d = new StringBuffer();
			String responseLine = "";
			DataInputStream downdatais = new DataInputStream(
					new BufferedInputStream(conn.getInputStream()));
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					downdatais));
			while ((responseLine = bf.readLine()) != null) {
				d.append(new String(responseLine.getBytes(), "UTF-8") + "\n");//
			}
			bf.close();
			downdatais.close();
			result=d.toString();
        } catch (Exception e) { 
            e.printStackTrace();
        } 
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

	public static String doPost(String url, String jsonStr) {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		String result = "";

		try {
			String data = StringUtil.mergeCodeBase64("9e8c23f670efe7dba280b6a6" + ":" + "b1bcae8402ceaeb1e7928bd0");
			StringEntity s = new StringEntity(jsonStr);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			s.setContentType("Authorization : Basic " + data);
			post.setEntity(s);
			HttpResponse res = httpclient.execute(post);
			if (res.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(res.getEntity());
			}
		} catch (Exception var8) {
			var8.printStackTrace();
		}

		return result;
	}



	/**
	 *
	 * @param httpUrl  请求的url
	 * @param param  form表单的参数（key,value形式）
	 * @return
	 */
	public static String doPostForm(String httpUrl, Map param) {

		HttpURLConnection connection = null;
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		String result = null;
		try {
			URL url = new URL(httpUrl);
			// 通过远程url连接对象打开连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接请求方式
			connection.setRequestMethod("POST");
			// 设置连接主机服务器超时时间：15000毫秒
			connection.setConnectTimeout(15000);
			// 设置读取主机服务器返回数据超时时间：60000毫秒
			connection.setReadTimeout(60000);

			// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
			connection.setDoOutput(true);
			// 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
			connection.setDoInput(true);
			// 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
			//connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
			// 通过连接对象获取一个输出流
			os = connection.getOutputStream();
			// 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的(form表单形式的参数实质也是key,value值的拼接，类似于get请求参数的拼接)
			os.write(createLinkString(param).getBytes());
			// 通过连接对象获取一个输入流，向远程读取
			if (connection.getResponseCode() == 200) {

				is = connection.getInputStream();
				// 对输入流对象进行包装:charset根据工作项目组的要求来设置
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				StringBuffer sbf = new StringBuffer();
				String temp = null;
				// 循环遍历一行一行读取数据
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 断开与远程地址url的连接
			connection.disconnect();
		}
		return result;
	}




	/**
	 *
	 * @param httpUrl  请求的url
	 * @param param  form表单的参数（key,value形式）
	 * @return
	 */
	public static String doPostFormData(String httpUrl, Map param) {

		HttpURLConnection connection = null;
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		String result = null;
		try {
			URL url = new URL(httpUrl);
			// 通过远程url连接对象打开连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接请求方式
			connection.setRequestMethod("POST");
			// 设置连接主机服务器超时时间：15000毫秒
			connection.setConnectTimeout(15000);
			// 设置读取主机服务器返回数据超时时间：60000毫秒
			connection.setReadTimeout(60000);

			// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
			connection.setDoOutput(true);
			// 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
			connection.setDoInput(true);
			// 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
			connection.setRequestProperty("Content-Type", "form-data");
			// 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
			//connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
			// 通过连接对象获取一个输出流
			os = connection.getOutputStream();
			// 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的(form表单形式的参数实质也是key,value值的拼接，类似于get请求参数的拼接)
			os.write(createLinkString(param).getBytes());
			// 通过连接对象获取一个输入流，向远程读取
			if (connection.getResponseCode() == 200) {

				is = connection.getInputStream();
				// 对输入流对象进行包装:charset根据工作项目组的要求来设置
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				StringBuffer sbf = new StringBuffer();
				String temp = null;
				// 循环遍历一行一行读取数据
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 断开与远程地址url的连接
			connection.disconnect();
		}
		return result;
	}


	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		StringBuilder prestr = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr.append(key).append("=").append(value);
			} else {
				prestr.append(key).append("=").append(value).append("&");
			}
		}

		return prestr.toString();
	}





	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent",
//					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}




//	/**
//	 * 向指定 URL 发送POST方法的请求
//	 * @param url
//	 *            发送请求的 URL
//	 * @param param
//	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
//	 * @return 所代表远程资源的响应结果
//	 */
//	public static String sendPostHttps(String url, String param) {
//		PrintWriter out = null;
//		BufferedReader in = null;
//		String result = "";
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
//			URLConnection conn = realUrl.openConnection();
//			// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
////			conn.setRequestProperty("user-agent",
////					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			// 发送POST请求必须设置如下两行
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			// 获取URLConnection对象对应的输出流
//			out = new PrintWriter(conn.getOutputStream());
//			// 发送请求参数
//			out.print(param);
//			// flush输出流的缓冲
//			out.flush();
//			// 定义BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(
//					new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			System.out.println("发送 POST 请求出现异常！"+e);
//			e.printStackTrace();
//		}
//		//使用finally块来关闭输出流、输入流
//		finally{
//			try{
//				if(out!=null){
//					out.close();
//				}
//				if(in!=null){
//					in.close();
//				}
//			}
//			catch(IOException ex){
//				ex.printStackTrace();
//			}
//		}
//		return result;
//
//	}
//
//
//
//
//	public static String doPostHttps(String url,Map<String,Object> map,String charset){
//		HttpClient httpClient = null;
//		HttpPost httpPost = null;
//		String result = null;
//		try{
//			httpClient = new SSLClient();
//			httpPost = new HttpPost(url);
//			//设置参数
//			List<NameValuePair> list = new ArrayList<NameValuePair>();
//			Iterator iterator = map.entrySet().iterator();
//			while(iterator.hasNext()){
//				Entry<String,String> elem = (Entry<String, String>) iterator.next();
//				list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
//			}
//			if(list.size() > 0){
//				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
//				httpPost.setEntity(entity);
//			}
//			HttpResponse response = httpClient.execute(httpPost);
//			if(response != null){
//				HttpEntity resEntity = response.getEntity();
//				if(resEntity != null){
//					result = EntityUtils.toString(resEntity,charset);
//				}
//			}
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return result;
//	}




	/**
	 *
	 * @param httpUrl  请求的url
	 * @param param  form表单的参数（key,value形式）
	 * @return
	 */
	public static String doPostFormByHeader(String httpUrl, Map param) {

		HttpURLConnection connection = null;
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		String result = null;
		try {
			URL url = new URL(httpUrl);
			// 通过远程url连接对象打开连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接请求方式
			connection.setRequestMethod("POST");
			// 设置连接主机服务器超时时间：15000毫秒
			connection.setConnectTimeout(15000);
			// 设置读取主机服务器返回数据超时时间：60000毫秒
			connection.setReadTimeout(60000);

			// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
			connection.setDoOutput(true);
			// 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
			connection.setDoInput(true);
			// 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("referer", "www.rlk8.com");
			// 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
			//connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
			// 通过连接对象获取一个输出流
			os = connection.getOutputStream();
			// 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的(form表单形式的参数实质也是key,value值的拼接，类似于get请求参数的拼接)
			os.write(createLinkString(param).getBytes());
			// 通过连接对象获取一个输入流，向远程读取
			if (connection.getResponseCode() == 200) {

				is = connection.getInputStream();
				// 对输入流对象进行包装:charset根据工作项目组的要求来设置
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				StringBuffer sbf = new StringBuffer();
				String temp = null;
				// 循环遍历一行一行读取数据
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 断开与远程地址url的连接
			connection.disconnect();
		}
		return result;
	}






	/**
	 * 使用HTTP POST 发送文本-----sp要求HTTP头中Content-Type要设置为application/json
	 *
	 * @param url
	 *            发送的地址
	 * @param param
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sendPostJsonAndHeader(String url, String param, String bearer, String token) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("Authorization", bearer + " " + token);
			conn.setDefaultUseCaches(false);
			conn.setDoOutput(true);
//            conn.setDoInput(true);
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
			out.print(param);
			out.flush();
			StringBuffer d = new StringBuffer();
			String responseLine = "";
			DataInputStream downdatais = new DataInputStream(
					new BufferedInputStream(conn.getInputStream()));
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					downdatais));
			while ((responseLine = bf.readLine()) != null) {
				d.append(new String(responseLine.getBytes(), "UTF-8") + "\n");//
			}
			bf.close();
			downdatais.close();
			result=d.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}



	public static void main(String[] args) {
		String url = "http://www.baidu.com";
//		boolean b = sentPostCode(url, "", "utf-8", null);
		boolean b = sendGetCode(url, "123455", null, null);
		System.out.println(b);
		String url1 = "http://114.55.67.167:8082/play/csm/upPayCode";
		Map<String, String> map = new HashMap<>();
		map.put("jsonData", "eyJwYXlQdyI6IjE1OTY3MTcxNDE1IiwidXBQYXlDb2RlIjoxMjM0NTYsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9");
		String data = JSON.toJSONString(map);
		String resData = sendPostAppJson(url1, data);
		System.out.println("resData:" + resData);
		String sbUrl = "http://www.xunyinpay.cn/Recharge.aspx?parter=8142&type=98&value=100&orderid=orderid_1&callbackurl=http://www.baidu.com&sign=efb02f6e2acf98ffebf6e39df22443f1";
		String sb = sendGet(sbUrl, null, null);
		System.out.println("sb:" + sb);
	}
}
