package cn.edu.dlut.mail.wuchen2020.signinserver.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import cn.edu.dlut.mail.wuchen2020.signinserver.config.SigninProperties;

/**
 * 通过ContentCachingResponseWrapper缓存内容来自动添加Content-Length的过滤器
 * <br>
 * 详见: https://zhuanlan.zhihu.com/p/375170625
 * 
 * @author Tango小黄
 */
@WebFilter(filterName = "ContentLengthFilter", urlPatterns = { "/api/*", "/error" }, dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.ERROR }, asyncSupported = true)
public class ContentLengthFilter extends OncePerRequestFilter {
    @Autowired
    private SigninProperties signinProperties;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (signinProperties.isChunkedTransferEnabled()) {
            // response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
            filterChain.doFilter(request, response);
            return;
        }
        ContentCachingResponseWrapper cacheResponseWrapper;
        if (response instanceof ContentCachingResponseWrapper) {
            cacheResponseWrapper = (ContentCachingResponseWrapper) response;
        } else {
            cacheResponseWrapper = new ContentCachingResponseWrapper(response);
        }
        filterChain.doFilter(request, cacheResponseWrapper);
        cacheResponseWrapper.copyBodyToResponse();
    }
}
