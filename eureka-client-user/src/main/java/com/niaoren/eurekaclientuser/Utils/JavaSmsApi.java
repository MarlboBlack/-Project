package com.niaoren.eurekaclientuser.Utils;

import com.niaoren.eurekaclientuser.common.Const;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;


public class JavaSmsApi {

    private  static final Logger logger = LoggerFactory.getLogger(JavaSmsApi.class);

    //查账户信息的http地址
    private static String URI_GET_USER_INFO = "https://sms.yunpian.com/v2/user/get.json";
    //模板发送接口的http地址
    private static String URI_TPL_SEND_SMS = "https://sms.yunpian.com/v2/sms/tpl_single_send.json";
    //编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";


    /**
     * 发送短信方法
     * @param mobile 手机号
     * @return
     */
    public static String SendSmsMessage(String mobile,String code){
        //apikey
        String apikey = Const.APIKEY;
        //要发送的手机号
       // String mobile = "130xxxxxxxx";
        //设置模板id
        long tpl_id = 2300822;
        //设置对应的模板变量值
        String tpl_value = null;
        try {
            tpl_value = URLEncoder.encode("#code#", ENCODING) + "=" +
                    URLEncoder.encode(code, ENCODING) + "&" + URLEncoder.encode(
                    "#hour#", ENCODING) + "=" + URLEncoder.encode("5分钟",
                    ENCODING);
        } catch (UnsupportedEncodingException e) {
            logger.error("短信编码异常",e);
        }
        try {
            logger.info(tpl_value);
            return JavaSmsApi.tplSendSms(apikey, tpl_id, tpl_value,mobile);
        } catch (IOException e) {
            logger.error("短信发送异常",e);
        }
        return "短信发送失败";
    }
    //验证码 6位
    public String getValidateCode(){
        Random random = new Random();
        String str = random.nextInt(1000000)+"";
        if(str.length()!= 6){
            return getValidateCode();
        }
        return str;
    }
    /**
     * 取账户信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */

    public static String getUserInfo(String apikey) throws IOException,
            URISyntaxException {
        Map < String, String > params = new HashMap < String, String > ();
        params.put("apikey", apikey);
        return post(URI_GET_USER_INFO, params);
    }
    /**
     * 通过模板发送短信(不推荐)
     *
     * @param apikey    apikey
     * @param tpl_id    　模板id
     * @param tpl_value 　模板变量值
     * @param mobile    　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String tplSendSms(String apikey, long tpl_id, String tpl_value,
                                    String mobile) throws IOException {
        Map < String, String > params = new HashMap < String, String > ();
        params.put("apikey", apikey);
        params.put("tpl_id", String.valueOf(tpl_id));
        params.put("tpl_value", tpl_value);
        params.put("mobile", mobile);
        logger.info(apikey+","+String.valueOf(tpl_id)+","+tpl_value+","+mobile);
        return post(URI_TPL_SEND_SMS, params);
    }
    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static String post(String url, Map < String, String > paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List < NameValuePair > paramList = new ArrayList <
                        NameValuePair > ();
                for (Map.Entry < String, String > param: paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(),
                            param.getValue());
                    paramList.add(pair);
                }

                method.setEntity(new UrlEncodedFormEntity(paramList,
                        ENCODING));
            }
            logger.info("00000000000000");
            response = client.execute(method);
            logger.info(response.toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                logger.info("1111111111111");
                responseText = EntityUtils.toString(entity, ENCODING);
                logger.info(responseText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }


}
