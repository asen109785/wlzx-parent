package net.wanho.ceucenter_auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplyTokenTest {
    @Autowired
    private RestTemplate restTemplate;


    @Test
    public void ApplyToken(){
        // 授权方式
        String grant_type = "password";
        // 用户名
        String username = "admin";
        // 密码
        String password = "admin";
        // client_id是应用的唯一标识，平台通过client_id来鉴别应用的身份
        String client_id = "wlzxWebApp";
        // 平台给应用分配的密钥
        String client_secret = "wlzxWebApp";

        //获取令牌信息的请求地址
        String url = "http://localhost:40400/oauth/token";
        //存放Authrization =>  Basic Auth,
        LinkedMultiValueMap<String,String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(client_id,client_secret);
        header.add("Authorization",httpBasic);
        // RestTemplate 发起http请求
        // exchange(url + 请求类型 + 请求参数 + 接收类型)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type",grant_type);
        body.add("username",username);
        body.add("password",password);
        HttpEntity<MultiValueMap<String,String>> httpEitity = new HttpEntity<>(body,header);
        //设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEitity, Map.class);
        Map map = responseEntity.getBody();
        System.out.println(map);


    }

    /**
     * client_id:client_secret  需要加密
     * @param client_id
     * @param client_secret
     * @return
     */
    private String getHttpBasic(String client_id, String client_secret) {
        String str = client_id+":"+client_secret;
        return "Basic "+ new String(Base64Utils.encode(str.getBytes()));

    }

    /**
     * 解析访问令牌
     */
    @Test
    public void jiexi(){
        String public_key = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnhKLOTboPMFAzyMzemf5um8u3Q4nmREEkrDnAGtjo5Obht9wSr3yY9bep1uLkcdVHKT6p7eqClTELXgqJNubKGOiIFg+o1VSdk6hSm1JyvgqIelxLuo2HSmn5s5dUvJoZ2uLfi6Bm8Krvi/lB6toiq9e9nZJvEmkOkqMnlh9qfO8oTF/2MEfNSYg2xP95vwEFziX6CvlmBSdy3ZA1spXjcdTuJ5CWrqe401pkdyAem0YHuyZxVd2UuhXxS5NGoeB+PTFjajuLDvcTsqqZQHWXVJBgLd7166s37KnJvf6vh7P9ahVuJJxA9Gee4j0jSFEa8ayT3FsgtVI+em3UGyTCwIDAQAB-----END PUBLIC KEY-----";
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJhcHAiXSwibmFtZSI6Iuezu-e7n-euoeeQhuWRmCIsInV0eXBlIjoiMTAxMDAzIiwiaWQiOiI0OCIsImV4cCI6MTU4NTc4MjUyOSwiYXV0aG9yaXRpZXMiOlsiY3JlYXRlVGltZT1udWxsIiwiV2x6eE1lbnUoaWQ9ODk0NDc3ODUxMDgyODgzMDcyIiwicElkPTkwMzQ1OTM3ODY1NTM5NTg0MSIsIldsenhNZW51KGlkPTg5NDQ3Nzk5NTkwMzgxMTU4NCIsImNyZWF0ZVRpbWU9TW9uIEF1ZyAwNyAxNjoyMTo1MiBDU1QgMjAxNyIsImlzTWVudT0wIiwiaXNNZW51PTEiLCJzb3J0PW51bGwiLCJjb2RlPXdsenhfc3lzbWFuYWdlcl91c2VyX3ZpZXciLCJpY29uPSIsIldsenhNZW51KGlkPTg5MzMwNDk2MDI4Mjc4Nzg0MCIsImNyZWF0ZVRpbWU9TW9uIEF1ZyAwNyAxNjoyMToxMiBDU1QgMjAxNyIsIm1lbnVOYW1lPeaWh-aho-afpeivoiIsImNyZWF0ZVRpbWU9TW9uIEF1ZyAwNyAxNjozOTowNyBDU1QgMjAxNyIsIm1lbnVOYW1lPeeUqOaIt-S_ruaUuSIsIm1lbnVOYW1lPeeUqOaIt-euoeeQhiIsInVwZGF0ZVRpbWU9TW9uIEF1ZyAwNyAxODoxODozOSBDU1QgMjAxNykiLCJXbHp4TWVudShpZD04OTQ0NzM2NTE4Mzc5OTI5NjAiLCJ1cGRhdGVUaW1lPVR1ZSBBdWcgMDggMTE6MDI6NTUgQ1NUIDIwMTcpIiwiY29kZT13bHp4X3N5c21hbmFnZXIiLCJtZW51TmFtZT3or77nqIvnrqHnkIYiLCJzdGF0dXM9MSIsIldsenhNZW51KGlkPTkwMzQ1OTM3ODY1NTM5NTg0NiIsIldsenhNZW51KGlkPTkwMzQ1OTM3ODY1NTM5NTg0MiIsImNvZGU9d2x6eF9zeXNtYW5hZ2VyX3VzZXJfZGVsZXRlIiwiY3JlYXRlVGltZT1GcmkgQXVnIDA0IDA5OjQ3OjA2IENTVCAyMDE3IiwiV2x6eE1lbnUoaWQ9OTAzNDU5Mzc4NjU1Mzk1ODQxIiwibWVudU5hbWU95re75Yqg55So5oi3IiwibWVudU5hbWU955So5oi35YiX6KGoIiwiY29kZT13bHp4X3N5c21hbmFnZXJfdXNlciIsImNvZGU9d2x6eF90ZWFjaG1hbmFnZXJfY291cnNlX2Jhc2UiLCJ1cGRhdGVUaW1lPU1vbiBBdWcgMDcgMTY6NTc6NTIgQ1NUIDIwMTcpIiwidXBkYXRlVGltZT1UdWUgQXVnIDA4IDA5OjU2OjI5IENTVCAyMDE3KSIsImxldmVsPTEiLCJsZXZlbD0yIiwibGV2ZWw9MyIsInNvcnQ9OSIsImxldmVsPW51bGwiLCJjb2RlPXdsenhfc3lzbWFuYWdlcl9sb2ciLCJzb3J0PTQiLCJjcmVhdGVUaW1lPU1vbiBBdWcgMDcgMTE6MTU6MjMgQ1NUIDIwMTciLCJzb3J0PTIiLCJzb3J0PTEiLCJ1cGRhdGVUaW1lPVdlZCBTZXAgMTMgMTE6MjA6MjYgQ1NUIDIwMTcpIiwicElkPTIyMjIyMjIyMjIyMjIyMjIyMiIsInBJZD04OTMyODg3MTU4ODE4MDc4NzIiLCJwSWQ9MTExMTExMTExMTExMTExMTExIiwibWVudU5hbWU957yW6L6R6K--56iL5Z-656GA5L-h5oGvIiwiV2x6eE1lbnUoaWQ9ODk0Mzk2NTIzNTMyNTE3Mzc2IiwiY29kZT13bHp4X3N5c21hbmFnZXJfZG9jIiwiY29kZT13bHp4X3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIiwiV2x6eE1lbnUoaWQ9ODkzMjg4NzE1ODgxODA3ODcyIiwiY29kZT13bHp4X3N5c21hbmFnZXJfdXNlcl9hZGQiLCJpY29uPW51bGwiLCJjcmVhdGVUaW1lPU1vbiBBdWcgMDcgMTY6Mzg6MzMgQ1NUIDIwMTciLCJtZW51TmFtZT3nlKjmiLfliKDpmaQiLCJXbHp4TWVudShpZD04OTQ0NzM0ODY3MTI0Mzg3ODQiLCJpc01lbnU9bnVsbCIsIm1lbnVOYW1lPeezu-e7n-euoeeQhiIsIm1lbnVOYW1lPea3u-WKoOivvueoiyIsImNyZWF0ZVRpbWU9RnJpIEF1ZyAwNCAxMDo1Nzo1NCBDU1QgMjAxNyIsInBJZD0wMDAwMDAwMDAwMDAwMDAwMDAiLCJjb2RlPXdsenhfc3lzbWFuYWdlcl91c2VyX2VkaXQiLCJzb3J0PTEwIiwic3RhdHVzPW51bGwiLCJjb2RlPXdsenhfdGVhY2htYW5hZ2VyX2NvdXJzZSIsIldsenhNZW51KGlkPTExMTExMTExMTExMTExMTExMSIsIm1lbnVOYW1lPWFkZCIsImNyZWF0ZVRpbWU9RnJpIEF1ZyAwNCAwOTo1MzoyMSBDU1QgMjAxNyIsInVwZGF0ZVRpbWU9bnVsbCkiLCJ1cmw9bnVsbCJdLCJqdGkiOiJkNDdlNThhMS03M2Q5LTQyMTctOTczOC03MjNhNDc5ZDZlMTgiLCJjbGllbnRfaWQiOiJ3bHp4V2ViQXBwIn0.G_wIPtyh9eY-nOO2tv6hco2WldWvgWjjeSCVYI9H336SEzoCJ8sbAgYrb1hNBevEZn1LUAFGwfb8xEkUmYMEaSNnRqeJZdOUYwU3dfleFrly3Y8H4fDjCcLte6x4ab8PzTrM2AEUJYZZxpD0w_qtigtaH8AaaUPEFkvq2GbVvPbTJ45p8k9BY8_01J9wu1xLiMHyQWgfTmlA0HTX4YNgZXn70lDkuvkv50ElgXycJv8-K2fZZwgjc-JORwJZZWyZliitgAurGITWBCHyaQ6Oo1Un26xtWf6SRT7dC3C8Hx-5WINmie0F2zlBiEXXyxQZMRJGS7D8nWNhwpzCbMGDHg";
        Jwt jwt = JwtHelper.decodeAndVerify(token,new RsaVerifier(public_key));
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
