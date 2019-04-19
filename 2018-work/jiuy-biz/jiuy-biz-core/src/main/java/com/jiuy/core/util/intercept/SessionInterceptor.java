package com.jiuy.core.util.intercept;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jiuy.core.dao.AdminUserDao;
import com.jiuy.core.dao.mapper.AuthorityDao;
import com.jiuy.core.dao.mapper.RoleAuthorityDao;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.admin.Authority;
import com.jiuy.core.meta.admin.RoleAuthority;

@Service
public class SessionInterceptor implements HandlerInterceptor {
	
	@Autowired
	private AuthorityDao authorityDao;
	
	@Autowired
	private RoleAuthorityDao roleAuthorityDao;
	
	@Autowired
	private AdminUserDao adminUserDao;
	
	private List<String> excludedUrls;

//	@Autowired
//	private AuthorityDao authorityDao;
	
	public void setExcludedUrls(List<String> excludedUrls) {
		this.excludedUrls = excludedUrls;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 接口中包含 login的就不进行拦截
        StringBuffer requestUri = request.getRequestURL();
//        Authority authority = authorityDao.searchOne(requestUri);
        for (String url : excludedUrls) {
            if (requestUri.toString().contains(url)) {
                return true;
            }
        }
        
        
        HttpSession session = request.getSession();
		if (session.getAttribute("userid") == null) {
			String type = request.getHeader("X-Requested-With");
			if ("XMLHttpRequest".equalsIgnoreCase(type)) {// AJAX REQUEST PROCESS
				response.setHeader("sessionstatus", "timeout");
				response.setStatus(400);
				return false;
			} else {// NORMAL REQUEST PROCESS
				response.sendRedirect(request.getContextPath() + "/loginpage");
				return false;
			}
		} else {
	        if (!requestUri.toString().contains(".html")) return true;
			return checkAuthority(request, (Long)session.getAttribute("userid"), requestUri);
		}
	}

	private boolean checkAuthority(HttpServletRequest request, Long userId, StringBuffer uri) {
		Map<String, String[]> params = request.getParameterMap();
		if (params.size() > 0) {
			uri.append("?");
			for (Map.Entry<String, String[]> string : params.entrySet()) {
				String key = string.getKey();
				String[] value = string.getValue();
				uri.append(key + "=" + value[0] + "&");
			}
			uri.deleteCharAt(uri.length() - 1);
		}
		
		if (authorityDao.containsUrl(uri.toString()) == null) {
			return true;
		}
		
		AdminUser adminUser = adminUserDao.getUser(userId);
		if (roleAuthorityDao.findAuthority(adminUser.getRoleId(), uri.toString()) != null) {
			return true;
		}
		
		return false;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
