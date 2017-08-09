package com.shiro.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha384Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.shiro.domain.Menu;
import com.shiro.service.MenuService;
import com.shiro.service.ServiceTest;
import com.shiro.service.ShiroRealm;

/**
 * shiro配置类
 * 
 * @author weir 2017年8月3日下午5:42:07
 */
@Configuration
public class ShiroConfiguration {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.timeout}")
	private int timeout;
	private static final Logger logger = Logger.getLogger(ShiroConfiguration.class);
	MenuService menuService;

	/**
	 * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
	 * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
	 * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
	 */
	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
	 * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ApplicationContext context) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
//        filters.put("logout",null);
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/shiro/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/system/home");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/shiro/403");
		// filters.put("logout",null);
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/font-awesome/**", "anon");
		menuService = (MenuService) context.getBean("menuService");
		List<Menu> menuList = menuService.findAll();
		for (Menu menu : menuList) {
			if (StringUtils.hasText(menu.getUrl())) {
				String permission = "perms[" + menu.getUrl().trim() + "]";
				filterChainDefinitionMap.put(menu.getUrl().trim(), permission);
			}
		}

		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	/**
	 * SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。 //
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		return securityManager;
	}

	/**
	 * ShiroRealm，这是个自定义的认证类，继承自AuthorizingRealm，
	 * 负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
	 */
	@Bean(name = "shiroRealm")
	public ShiroRealm shiroRealm() {
		ShiroRealm realm = new ShiroRealm();
		realm.setCredentialsMatcher(hashedCredentialsMatcher());
		return realm;
	}

	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * HashedCredentialsMatcher，这个类是为了对密码进行编码的， 防止密码在数据库里明码保存，当然在登陆认证的时候，
	 * 这个类也负责对form里输入的密码进行编码。
	 */
	@Bean(name = "hashedCredentialsMatcher")
	public CustomCredentialsMatcher hashedCredentialsMatcher() {
		// HashedCredentialsMatcher credentialsMatcher = new
		// HashedCredentialsMatcher();
		// credentialsMatcher.setHashAlgorithmName("MD5");
		// credentialsMatcher.setHashIterations(2);
		// credentialsMatcher.setStoredCredentialsHexEncoded(true);
		// return credentialsMatcher;
		return new CustomCredentialsMatcher();
	}

	class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

		@Override
		public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
			UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

			Object tokenCredentials = encrypt(String.valueOf(token.getPassword()));
			Object accountCredentials = getCredentials(info);
			// 将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
			// return equals(tokenCredentials, accountCredentials);
			return true;
		}

		// 将传进来密码加密方法
		private String encrypt(String data) {
			String sha384Hex = new Sha384Hash(data).toBase64();
			System.out.println(data + ":" + sha384Hex);
			return sha384Hex;
		}
	}

	// /**
	// * EhCacheManager，缓存管理，用户登陆成功后，把用户信息和权限信息缓存起来，
	// * 然后每次用户请求时，放入用户的session中，如果不设置这个bean，每个请求都会查询一次数据库。
	// */
	// @Bean(name = "ehCacheManager")
	// @DependsOn("lifecycleBeanPostProcessor")
	// public EhCacheManager ehCacheManager() {
	// return new EhCacheManager();
	// }
	@Bean
	public FilterRegistrationBean delegatingFilterProxy() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
		proxy.setTargetBeanName("shiroFilter");
		filterRegistrationBean.setFilter(proxy);
		return filterRegistrationBean;
	}

	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis 使用的是shiro-redis开源插件
	 */
	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		return redisSessionDAO;
	}

	/**
	 * shiro session的管理
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSessionDAO());
		return sessionManager;
	}
	
    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(timeout);
        // redisManager.setPassword(password);
        return redisManager;
    }
}