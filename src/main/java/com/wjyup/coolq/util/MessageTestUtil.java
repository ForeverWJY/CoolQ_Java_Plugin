package com.wjyup.coolq.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.wjyup.coolq.entity.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by WJY on 2017/7/8.
 */
public class MessageTestUtil {
    private static final String qq = "1066231345";
    private static final String msg = "你好啊！\n123";

    //加密部分
    private static final String key = "123";
    private static final String secret = "456";
    //是否加密
    private static final boolean isEncrypt = true;

    /**
     * 发送私聊信息测试
     * @return
     */
    public static String sendPrivateMsg(){
        Data data = new Data(Long.parseLong(qq), msg,"sendPrivateMsg");
        return data.toJson();
    }


    /**
     * 加密发送数据测试！
     * @param json JSON格式数据
     */
    public static void sendMsgTest(String json) throws UnsupportedEncodingException {
        if(isEncrypt){
            //MD5加密
            long time = System.currentTimeMillis() / 1000;
            System.out.println(time);
            String str = key + time + secret;
            String hash = Hashing.md5().newHasher().putString(str, Charsets.UTF_8).hash().toString();
            System.out.println(hash);
            String url = "http://127.0.0.1:1970/" + time + "/" + hash;
            Map<String,Object> map = (Map<String, Object>) JSON.parse(json);
//            Set<Map.Entry<String,Object>> set = JSON.parseObject(json).entrySet();
            Set<Map.Entry<String,Object>> set = map.entrySet();

//            Map<String,String> params = new HashMap<>(map.size());
            StringBuffer sb = new StringBuffer(url+"/"+map.get("Fun")+"?");

            for(Map.Entry<String,Object> o : set){
                System.out.println(o.getKey()+":"+o.getValue());
                //post方式使用map
//                params.put(o.getKey(),o.getValue().toString());
                //get方式使用拼接
                String temp = URLEncoder.encode(o.getValue().toString(),"utf-8");
                sb.append(o.getKey()+"="+ temp +"&");
            }
            System.out.println(url);

            try{
                System.out.println(sb.toString());
                Document doc = Jsoup.connect(sb.toString()).timeout(5000).get();
                System.out.println("返回："+doc.text());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 加密：部分
     * url/time/hash/data
     * data=base64_encode(rc4('api?param',$key))
     */

   /* public static void main(String[] args) {
        sendMsgTest("123");
    }*/
}
