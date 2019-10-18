package com.hy.core.secure.intercept;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.common.tool.SpringUtil;
import com.hy.core.secure.entity.TokenStub;
import com.hy.core.secure.utils.RoleMatcher;
import com.hy.core.secure.utils.TokenUtil;
import com.hy.common.tool.R;
import com.hy.common.tool.ResultCode;
import com.hy.common.tool.WebUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SecureInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
	    Claims claims = TokenUtil.getClaims(request);
	    if (claims == null || TokenUtil.extractTokenStub(claims) == null) {
            responseError(R.fail(ResultCode.UN_AUTHORIZED), request, response);
            return false;
        }

		if (TokenUtil.isExpired(claims)) {
			responseError(R.fail(ResultCode.ACCESS_TOKEN_EXPIRED), request, response);
			return false;
		}

		TokenStub stub = TokenUtil.extractTokenStub(claims);
		RoleMatcher matcher = SpringUtil.getBean(RoleMatcher.class);
		matcher.setRoles(stub.getRoleIds());
		if (matcher.matches(request)) {
			return true;
		} else {
			responseError(R.fail(ResultCode.ACCESS_TOKEN_EXPIRED), request, response);
			return false;
		}
	}

	private void responseError(R result, HttpServletRequest request, HttpServletResponse response) {
		log.warn("授权检查失败，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), WebUtil.getIP(request), JSON.toJSONString(request.getParameterMap()));
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			response.getWriter().write(JSONObject.toJSONString(result));
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
	}

}
