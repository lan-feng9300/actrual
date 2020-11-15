package com.example.iputil;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *  获取用户真实的 ip,不使用 request.getRemoteAddr() 的原因是有可能用户使用了代理软件方式避开真实ip
 *  可是: 如果通过了多级反向代理的话, X-Forwarded-For的值并不止一个, 而是一串的 ip值, 究竟取哪个呢?
 *  答案: 取 X-Forwarded-For中,第一个非unknown的有效 ip字符串
 *  如: X-Forwarded-For: 
 *      192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
 *      用户的真实ip: 192.168.1.110
 *      
 */

public class IpAddrUtil {
    
    public static String getIpAddr(HttpServletRequest request){

        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
