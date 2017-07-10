package com.redis.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.redis.db.TwiterRepository;
import com.redis.exception.NoSuchDataException;
import com.redis.interceptor.CookieInterceptor;
import com.redis.utils.Post;
import com.redis.utils.Range;

@Controller
public class TwiterController {
	@Autowired
	TwiterRepository twiterRepository;
	@RequestMapping("/")
	public String root(@RequestParam(required = false) Integer page, Model model) {
		if (RetwisSecurity.isSignedIn()) {
			return "redirect:/!" + RetwisSecurity.getName();
		}
		return timeline(page, model);
	}

	@RequestMapping("/signIn")
	public String signIn(@RequestParam(required = false) String name, @RequestParam(required = false) String pass, Model model,
			HttpServletResponse response) {
		if (twiterRepository.auth(name, pass)) {
			addAuthCookie(twiterRepository.addAuth(name), name, response);
			return "redirect:/!" + name;
		} else if (StringUtils.hasText(pass) || StringUtils.hasText(name)) {
			model.addAttribute("errorpass", Boolean.TRUE);
		}
		return "signin";
	}

	@RequestMapping("/signUp")
	public String signUp(String name, String pass, String pass2, Model model, HttpServletResponse response) {
		if (twiterRepository.isUserValid(name)) {
			model.addAttribute("errorduplicateuser", Boolean.TRUE);
			return "signin";
		}
		if (!StringUtils.hasText(name) || !StringUtils.hasText(pass) || !StringUtils.hasText(pass2) || !pass.equals(pass2)) {
			model.addAttribute("errormatch", Boolean.TRUE);
			return "signin";
		}
		String auth = twiterRepository.addUser(name, pass);
		addAuthCookie(auth, name, response);
		return "signin";
	}

	@RequestMapping(value = "/!{name}", method = RequestMethod.GET)
	public String posts(@PathVariable String name, @RequestParam(required = false) String replyto, @RequestParam(required = false) String replypid,
			@RequestParam(required = false) Integer page, Model model) {
		checkUser(name);
		String targetUid = twiterRepository.findUid(name);
		model.addAttribute("post", new Post());
		model.addAttribute("name", name);
		model.addAttribute("followers", twiterRepository.getFollowers(targetUid));
		model.addAttribute("following", twiterRepository.getFollowing(targetUid));
		

		if (RetwisSecurity.isSignedIn()) {
			model.addAttribute("replyTo", replyto);
			model.addAttribute("replyPid", replypid);

			if (!targetUid.equals(RetwisSecurity.getUid())) {
				model.addAttribute("also_followed", twiterRepository.alsoFollowed(RetwisSecurity.getUid(), targetUid));
				model.addAttribute("common_followers",twiterRepository.commonFollowers(RetwisSecurity.getUid(), targetUid));
				model.addAttribute("follows", twiterRepository.isFollowing(RetwisSecurity.getUid(), targetUid));
			}
		}
		// sanitize page attribute
		page = (page != null ? Math.abs(page) : 1);
		model.addAttribute("page", page + 1);
		Range range = new Range(page);
		return "home";
	}

	@RequestMapping(value = "/!{name}", method = RequestMethod.POST)
	public String posts(@PathVariable String name, WebPost post, Model model, HttpServletRequest request) {
		checkUser(name);
		twiterRepository.post(name, post);
		return "redirect:/!" + name;
	}

	@RequestMapping("/logout")
	public String logout() {
		String user = RetwisSecurity.getName();
		// invalidate auth
		twiterRepository.deleteAuth(user);
		return "redirect:/";
	}

	@RequestMapping("/!{name}/follow")
	public String follow(@PathVariable String name) {
		checkUser(name);
		twiterRepository.follow(name);
		return "redirect:/!" + name;
	}

	@RequestMapping("/timeline")
	public String timeline(@RequestParam(required = false) Integer page, Model model) {
		// sanitize page attribute
		page = (page != null ? Math.abs(page) : 1);
		model.addAttribute("page", page + 1);
		Range range = new Range(page);
		// model.addAttribute("moreposts", retwis.hasMoreTimeline(range));
		model.addAttribute("posts", twiterRepository.timeline(range));
		model.addAttribute("users", twiterRepository.newUsers(new Range()));
		return "timeline";
	}
	@RequestMapping("/!{name}/stopfollowing")
	public String stopFollowing(@PathVariable String name) {
		checkUser(name);
		twiterRepository.stopFollowing(name);
		return "redirect:/!" + name;
	}
	
	@RequestMapping("/status")
	public String status(String pid, Model model) {
		checkPost(pid);
		model.addAttribute("posts", twiterRepository.getPost(pid));
		return "status";
	}

	@RequestMapping("/!{name}/mentions")
	public String mentions(@PathVariable String name, Model model) {
		checkUser(name);
		model.addAttribute("name", name);
		String targetUid = twiterRepository.findUid(name);

		model.addAttribute("posts", twiterRepository.getMentions(targetUid, new Range()));
		model.addAttribute("followers", twiterRepository.getFollowers(targetUid));
		model.addAttribute("following", twiterRepository.getFollowing(targetUid));

		if (RetwisSecurity.isSignedIn() && !targetUid.equals(RetwisSecurity.getUid())) {
			model.addAttribute("also_followed", twiterRepository.alsoFollowed( targetUid,RetwisSecurity.getUid()));
			model.addAttribute("common_followers", twiterRepository.commonFollowers(RetwisSecurity.getUid(), targetUid));
			model.addAttribute("follows", twiterRepository.isFollowing(RetwisSecurity.getUid(), targetUid));
		}

		return "mentions";
	}
	private void checkPost(String pid) {
		if (!twiterRepository.isPostValid(pid)) {
			throw new NoSuchDataException(pid, false);
		}
	}
	private void addAuthCookie(String auth, String name, HttpServletResponse response) {
		RetwisSecurity.setUser(name, twiterRepository.findUid(name));

		Cookie cookie = new Cookie(CookieInterceptor.twiter_COOKIE, auth);
		cookie.setComment("Retwis-J demo");
		// cookie valid for up to 1 week
		cookie.setMaxAge(60 * 60 * 24 * 7);
		response.addCookie(cookie);
	}

	private void checkUser(String username) {
		if (!twiterRepository.isUserValid(username)) {
			throw new NoSuchDataException(username, true);
		}
	}
}
