package com.ruoyi.web.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.framework.util.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;

import java.util.Date;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@Controller
public class SysLoginController extends BaseController
{
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Object ajaxLogin(String username, String password, Boolean rememberMe)
    {
        Message msg = new Message();
        if(StringUtils.isBlank(username)){
            msg.setCode(500);
            msg.setMsg("用户名不能为空,请重新登录");
            return msg;
        }
        if(StringUtils.isBlank(password)){
            msg.setCode(500);
            msg.setMsg("用户名不能为空,请重新登录");
            return msg;
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            msg.setCode(0);
            msg.setMsg("操作成功");
            msg.setToken(ShiroUtils.getSessionId());
            return msg;
        }
        catch (AuthenticationException e)
        {
            String message = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                message = e.getMessage();
            }
            return error(message);
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }
}
