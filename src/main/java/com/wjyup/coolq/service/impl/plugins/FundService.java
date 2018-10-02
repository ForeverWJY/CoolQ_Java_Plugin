package com.wjyup.coolq.service.impl.plugins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.service.ResolveMessageService;
import com.wjyup.coolq.util.LocalCache;
import com.wjyup.coolq.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;

/**
 * 基金Service
 */
@Component
public class FundService extends ResolveMessageService {
    @Override
    public void doit(RequestData data) throws Exception {
        String key = data.getMsg();
        //基金
        if((key.startsWith("基金") || key.startsWith("jj")) && key.length() <= 10){
            String k = key.substring(0,2).trim();
            String k1 = key.substring(2).replace(" ", "").trim();
            if(k.equals("基金") && StringUtils.isNumeric(k1)){
                String fund = getFund(k1);
                if(fund != null) reply(data, fund);
            }else if(k.equals("jj") && StringUtils.isNumeric(k1)){
                String fund = getFundGP(k1);
                if(fund != null) reply(data, fund);
            }
        }
    }

    /**
     * 基金实时走势
     */
    private String getFund(String fundNum) throws Exception {
        if(StringUtils.isBlank(fundNum)) return null;
        String url = "http://fundgz.1234567.com.cn/js/"+fundNum+".js";
        StringBuilder sb = new StringBuilder();
        String json = WebUtil.get(url);
        if(StringUtils.isNotBlank(json)){
            json = json.substring(8, json.length() - 2);
            JSONObject obj = JSON.parseObject(json);
            if(obj.containsKey("name") && StringUtils.isNotBlank(obj.getString("name"))){
                sb.append("基金名称：").append(obj.getString("name"));
            }
            if(obj.containsKey("fundcode") && StringUtils.isNotBlank(obj.getString("fundcode"))){
                sb.append("[").append(obj.getString("fundcode")).append("]");
            }
            if(obj.containsKey("gsz") && StringUtils.isNotBlank(obj.getString("gsz"))){
                sb.append("\n实时净值：").append(obj.getString("gsz"));
            }
            if(obj.containsKey("gszzl") && StringUtils.isNotBlank(obj.getString("gszzl"))){
                sb.append("\n实时涨跌幅：").append(obj.getString("gszzl")).append("%");
            }
            if(obj.containsKey("gztime") && StringUtils.isNotBlank(obj.getString("gztime"))){
                sb.append("\n更新时间：").append(obj.getString("gztime"));
            }
            if(obj.containsKey("jzrq") && StringUtils.isNotBlank(obj.getString("jzrq"))
                    && obj.containsKey("dwjz") && StringUtils.isNotBlank(obj.getString("dwjz"))){
                sb.append("\n").append(obj.getString("jzrq")).append("净值：").append(obj.getString("dwjz"));
            }
            if(log.isDebugEnabled()){
                log.debug(sb.toString());
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 查询基金和对应的股票信息
     */
    private String getFundGP(String fundNum){
        try {
            if(fundNum == null) return null;
            StringBuffer sb = new StringBuffer();
            //查询基金的信息
            String fund = getFund(fundNum);
            if(fund != null){
                sb.append(fund).append("\n");
                //查询基金买的股票列表
                String fundWithGP = getGP(fundNum);
                if(fundWithGP != null){
                    //传入股票代码列表，获取股票详情
                    String result = getGPResult(fundWithGP);
                    if(result != null){
                        sb.append(result);
                        return sb.toString();
                    }
                }else{
                    log.info("基金的股票代码为空！");
                }
            }else{
                return "没查询到["+fundNum+"]的基金信息";
            }
        } catch (Exception e) {
            log.error("查询基金和对应的股票信息失败\n", e);
        }
        return null;
    }

    /**
     * 查询基金重仓的股票
     */
    private String getGP(String fundNum) throws Exception {
        //优先读取缓存
        Object o = LocalCache.getCache("jijin_"+fundNum);
        //读取缓存
        if(o != null && StringUtils.isNotBlank(o.toString())){
            return o.toString();
        }
        //获取结果并设置缓存
        String url = "http://fund.eastmoney.com/pingzhongdata/"+fundNum+".js";
        String text = WebUtil.get(url);
        if(StringUtils.isNotBlank(text)){
            //处理js
            int lastIndex = text.indexOf("/*股票仓位测算图*/");
            String js = text.substring(0, lastIndex);
            js = js.replace("//最小申购金额", "");
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            //执行js
            engine.eval(js);
            //js数组转成Java数组
            engine.eval("var JavaArray = Java.to(stockCodes,\"java.lang.String[]\");");
            String[] str = (String[]) engine.get("JavaArray");
            String gpstr = Arrays.deepToString(str).replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");

            LocalCache.addCache("jijin_"+fundNum, gpstr);
            return gpstr;
        }
        return null;
    }

    /**
     * 获取股票数据
     * @param gpstr
     * @return
     */
    private String getGPResult(String gpstr) throws Exception {
        //拼接URL
        String url = "http://nufm3.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd="+gpstr+"&sty=E1OQCPZT&st=z&sr=&p=&ps=&cb=&js=var%20js_favstock={favif:[%28x%29]}&token=8a36403b92724d5d1dd36dc40534aec5&rt=function%20random()%20{%20[native%20code]%20}";
        StringBuilder sb = new StringBuilder("股票名称[代码]|最新价|涨跌幅|市盈率\n");
        String json = WebUtil.get(url);
        if(StringUtils.isNotBlank(json)){
            json = "{\"data\":"+json.substring(23, json.length() - 1) + "}";
            JSONObject obj = JSON.parseObject(json);
            if(obj.containsKey("data") && obj.getJSONArray("data").size() > 0){
                JSONArray jarr = obj.getJSONArray("data");
                for(int i=0; i<jarr.size(); i++){
                    String o = jarr.getString(i);
                    //解析股票
                    String[] os = o.split(",");
                    sb.append(String.format("%s[%s]|%s|%s|%s", os[2], os[1], os[3], os[4], os[5]));
                    if(i != jarr.size() - 1){
                        sb.append("\n");
                    }
                }
            }
            return sb.toString();
        }
        return null;
    }
}
