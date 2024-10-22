package net.musecom.community.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class ClientIpAddress {
   private String ip;
   
   public void setClientIpAddress(HttpServletRequest req) {
      String ip = req.getHeader("x-Forwarded-For");
      if(ip == null || ip.isEmpty() || "unkwon".equalsIgnoreCase(ip)) {
        ip = req.getHeader("Proxy-Client-IP");   
      }
      if(ip == null || ip.isEmpty() || "unkwon".equalsIgnoreCase(ip)) {
        ip = req.getHeader("WL-Proxy-Client-IP");   
      }
      if(ip == null || ip.isEmpty() || "unkwon".equalsIgnoreCase(ip)) {
        ip = req.getHeader("HTTP-CLIENT-IP");   
      }
      if(ip == null || ip.isEmpty() || "unkwon".equalsIgnoreCase(ip)) {
        ip = req.getHeader("HTTP_X_FORWARDED_FOR");   
      }
      if(ip == null || ip.isEmpty() || "unkwon".equalsIgnoreCase(ip)) {
         ip = req.getRemoteAddr();
      }
      this.ip = ip;
   }
   
   public String getClientIpAddress() {
      return this.ip;
   }
}
