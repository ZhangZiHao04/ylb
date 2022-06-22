package com.bjpowernode.money.web.config.intercepors;

import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if(user==null){
            response.sendRedirect(request.getContextPath()+"/loan/page/login");
            return false;
        }else {
            return true;
        }
    }
}
