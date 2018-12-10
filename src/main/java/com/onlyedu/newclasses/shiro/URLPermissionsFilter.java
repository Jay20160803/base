package com.onlyedu.newclasses.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 * @date 2018/12/4 17:22
 */

public class URLPermissionsFilter extends PermissionsAuthorizationFilter {


    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {
        String curUrl = ((HttpServletRequest)request).getRequestURI() ;
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() == null || StringUtils.endsWithAny(curUrl, ".js", ".css", ".html")
                || StringUtils.endsWithAny(curUrl, ".jpg", ".png", ".gif", ".jpeg")
                || StringUtils.equals(curUrl, "/unauthor")) {
            return true;
        }

        //List<String> urls = userService.findPermissionUrl(subject.getPrincipal().toString());

        List<String> urls = new ArrayList<>();
        urls.add("/users/user");
        return urls.contains(curUrl);
    }


}
