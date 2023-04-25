package org.jeecg.modules.system.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.exception.JeecgBootExceptionHandler;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;


/**
 * 功能描述
 *
 * @author: 周泽龙
 * @date: 2022年08月05日 1:07
 */
public class HttpClientUtil {

    // 通用HTTP请求方法
    public static String clientUI(HttpRequestBase httpRequestBase){
        String str = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpRequestBase);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                str = EntityUtils.toString(responseEntity,"UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    //创建Get请求
    public static String createGetHttp(String Scheme, String Host, String Path, List<NameValuePair> params){
        //方法介绍：Scheme是协议类型，Host是主机地址，Path是地址之后的资源路径,返回字符串格式

        JSONObject jsonObject = null;
        String s =null;

        //拼接URL地址
        String url = new String(Scheme + "://" + Host +Path + "?");
        ListIterator iterger = params.listIterator();
        while(iterger.hasNext()){
            url = url + "&" + iterger.next();
        }
        HttpGet httpget = new HttpGet(url);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            CloseableHttpResponse execute = httpClient.execute(httpget);
            HttpEntity entity = execute.getEntity();
            Header[] allHeaders = execute.getAllHeaders(); // 获取响应头
            if (entity.getContent() != null){
                s = EntityUtils.toString(entity,"UTF-8");  // 将响应体的内容转换为字符串
                StatusLine statusLine = execute.getStatusLine();		// 获取响应的状态码

//                JSONArray array =  JSON.parseArray(s);
//                jsonObject =  JSONObject.parseObject(array.get(0).toString());
            }
            execute.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    //创建Get请求
    public static String createGetHttpByPath(String Path){
        //方法介绍：Scheme是协议类型，Host是主机地址，Path是地址之后的资源路径,返回字符串格式

        JSONObject jsonObject = null;
        String s =null;

        HttpGet httpget = new HttpGet(Path);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            CloseableHttpResponse execute = httpClient.execute(httpget);
            HttpEntity entity = execute.getEntity();
            Header[] allHeaders = execute.getAllHeaders(); // 获取响应头
            if (entity.getContent() != null){
                s = EntityUtils.toString(entity,"UTF-8");  // 将响应体的内容转换为字符串
                StatusLine statusLine = execute.getStatusLine();		// 获取响应的状态码
            }
            execute.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * 创建Get请求
     * @param Path
     * @return 返回相应状态码
     */
    public static String getGetState(String Path){
        String s =null;
        HttpGet httpget = new HttpGet(Path);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            CloseableHttpResponse execute = httpClient.execute(httpget);
            HttpEntity entity = execute.getEntity();
            Header[] allHeaders = execute.getAllHeaders(); // 获取响应头
            if (entity.getContent() != null){
                s = EntityUtils.toString(entity,"UTF-8");  // 将响应体的内容转换为字符串
                StatusLine statusLine = execute.getStatusLine();		// 获取响应的状态码
                s = String.valueOf(statusLine);
            }
            execute.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    //创建POST请求
    public static JSONObject createPostHttp(String Scheme, String Host, String Path,List<NameValuePair> params){
        //方法介绍：Scheme是协议类型，Host是主机地址，Path是地址之后的资源路径,返回json格式的返回结果
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme(Scheme).setHost(Host).setPath(Path).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
        httpPost.setHeader("charset", StandardCharsets.UTF_8.name());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String s =  HttpClientUtil.clientUI(httpPost);
        return JSONObject.parseObject(s);
    }

    public static void createPostHttp1(String Scheme, String Host, String Path,List<NameValuePair> params){
        //方法介绍：Scheme是协议类型，Host是主机地址，Path是地址之后的资源路径,返回json格式的返回结果
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme(Scheme).setHost(Host).setPath(Path).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type","application/json");
//        httpPost.setHeader("charset", StandardCharsets.UTF_8.name());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            httpPost.setHeader("Accept-Encoding","gzip, deflate, br");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String s =  HttpClientUtil.clientUI(httpPost);
        System.out.println(s);
//        return JSONObject.parseObject(s);
    }

    /**
     *
     * @param url
     * @param jsonArray
     * @return
     */
    public static String createPostHttpByPath(String url,String jsonArray) {

            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();//创建一个获取连接客户端的工具
                HttpPost httpPost = new HttpPost(url);//创建Post请求
                httpPost.addHeader("Content-Type", "application/json;charset=utf-8");//添加请求头
                StringEntity entity = new StringEntity(jsonArray,"utf-8");//使用StringEntity转换成实体类型
//                entity.setContentEncoding(new BasicHeader("utf-8", "application/json"));
                entity.setContentType("application/json");//发送json数据需要设置contentType
                httpPost.setEntity(entity);//将封装的参数添加到Post请求中
                CloseableHttpResponse response = httpClient.execute(httpPost);//执行请求
                HttpEntity responseEntity = response.getEntity();//获取响应的实体
                String entityString = EntityUtils.toString(responseEntity);//转化成字符串
                response.close();
                httpClient.close();
                return entityString;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("出错位置：HttpClientUtil");
                System.out.println(e);
            }
            return "error";
    }
}









