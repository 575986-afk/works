package kr.co.sist.login;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        
//        HttpSession session = request.getSession();
//        Object user = session.getAttribute("user");
//        Object companyNo = session.getAttribute("companyNo");
//        Object userNo = session.getAttribute("userNo");
//        
//        if (user == null || companyNo == null || userNo == null) {
//            
//            String ajaxHeader = request.getHeader("X-Requested-With");
//            if ("XMLHttpRequest".equals(ajaxHeader) || request.getRequestURI().contains("Data")) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//                return false;
//            }
//            
//            String uri = request.getRequestURI();
//            String query = request.getQueryString();
//            if (query != null && !query.isEmpty()) {
//                uri += "?" + query;
//            }
//            
//            session.setAttribute("redirectURL", uri);
//            
//            response.sendRedirect("/login");
//            return false;
//        }
//
//        return true;
//    }
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    
	    String uri = request.getRequestURI();
	    
	    if (uri.equals("/login") || uri.equals("/loginProcess") || uri.startsWith("/works/login/")) {
	        return true;
	    }

	    HttpSession session = request.getSession();
	    Object user = session.getAttribute("user");
	    Object companyNo = session.getAttribute("companyNo");
	    Object userNo = session.getAttribute("userNo");
	    
	    if (user == null || companyNo == null || userNo == null) {
	        
	        String ajaxHeader = request.getHeader("X-Requested-With");
	        if ("XMLHttpRequest".equals(ajaxHeader) || uri.contains("Data")) {
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	            return false;
	        }
	        
	        String query = request.getQueryString();
	        if (query != null && !query.isEmpty()) {
	            uri += "?" + query;
	        }
	        
	        if (!uri.contains("/login")) {
	            session.setAttribute("redirectURL", uri);
	        }
	        
	        response.sendRedirect("/login");
	        return false;
	    }

	    return true;
	}
}