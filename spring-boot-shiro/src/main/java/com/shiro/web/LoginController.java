package com.shiro.web;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shiro.json.ResultInfo;
import com.shiro.service.UserService;

@RestController
public class LoginController {
	private static final Logger logger = Logger.getLogger(LoginController.class);
	@Autowired
	UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam(value = "username", required = true) String userName,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "rememberMe", required = true, defaultValue = "false") boolean rememberMe) {
		logger.info("==========" + userName + password + rememberMe);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		token.setRememberMe(rememberMe);
		try {
			subject.login(token);
			  return "admin";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			// rediect.addFlashAttribute("errorText", "您的账号或密码输入错误!");
			return "login";
		}
	}

	@RequestMapping("/")
	@ResponseBody
	public String index() {
		return "no permission";
	}
}
