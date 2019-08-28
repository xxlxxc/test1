package com.tedu.sp11.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.tedu.web.util.JsonResult;

@Component
public class AccessFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String serverId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
		if (serverId.equals("item-service")) {
			return true;
		}
		return false;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();
		String at = req.getParameter("token");
		if (null == at) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(200);
			ctx.setResponseBody(JsonResult.err().code(JsonResult.NOT_LOGIN).toString());
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
	}

}
