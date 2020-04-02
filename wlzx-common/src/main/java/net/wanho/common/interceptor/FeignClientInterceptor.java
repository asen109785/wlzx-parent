package net.wanho.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import net.wanho.common.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
                //取出request
                HttpServletRequest request = ServletUtils.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        if (name.equals("authorization")) {
                            //System.out.println("name="+name+"values="+values);
                            requestTemplate.header(name, values);
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}