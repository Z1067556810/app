package zhang.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 张会丽
 * @create 2019/8/5
 */
public class JWTUtils {
    //生成JWT 加密信息
    public static String generateToken(String userInfo){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userInfo",userInfo);
        map.put("created",new Date());
        return Jwts.builder().setClaims(map).setExpiration(new Date(System.currentTimeMillis()+30*60*1000L)).signWith(SignatureAlgorithm.HS512,"secretkey").compact();
    }
    public static JSONObject decodeJwtToken(String token){
        Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
        JSONObject userInfo = JSON.parseObject(claims.get("userInfo").toString());
        return userInfo;
    }
}
