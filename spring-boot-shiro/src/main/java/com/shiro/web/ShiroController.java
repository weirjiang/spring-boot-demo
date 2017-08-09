package com.shiro.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shiro.domain.SystemUser;
import com.shiro.json.ResultInfo;
import com.shiro.service.UserService;

@Controller
@RequestMapping("/shiro")
public class ShiroController {
	private static final Logger logger = Logger.getLogger(ShiroController.class);
	@Autowired
	UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm(Model model) {
		model.addAttribute("user", new SystemUser());
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String login( HttpServletRequest request,
	        HttpServletResponse response,@RequestParam(value = "name", required = true) String userName,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "rememberMe", required = true, defaultValue = "false") boolean rememberMe, RedirectAttributes redirectAttributes) throws IOException {
		logger.info("==========" + userName + password + rememberMe);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		token.setRememberMe(rememberMe);
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			// rediect.addFlashAttribute("errorText", "您的账号或密码输入错误!");
			redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
		}
		// 验证是否登录成功
		if (subject.isAuthenticated()) {
			logger.info("用户[" + userName + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
			return ResultInfo.success();
		} else {
			token.clear();
			return ResultInfo.error(500, "err");
		}
	}

	@RequestMapping("/")
	@ResponseBody
	public String index() {
		return "no permission";
	}

	@RequestMapping("/403")
	public String unauthorizedRole() {
		logger.info("------没有权限-------");
		return "403";
	}
}
