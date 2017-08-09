package com.shiro.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.shiro.domain.Menu;
import com.shiro.domain.MenuTree;
import com.shiro.domain.Permission;
import com.shiro.json.ResultInfo;
import com.shiro.service.MenuService;
import com.shiro.service.RoleResourceService;

/**
 * 权限管理
 * @author weir
 * 2017年7月31日下午6:01:56
 */
@Controller
@RequestMapping(value="/permission")
public class PermissionController {
	@Resource
	RoleResourceService roleResourceService;
	@Resource
	MenuService menuService;
	@RequestMapping(value="/home")
	public String home(String roleId,ModelMap model){
		model.addAttribute("roleId", roleId);
		return "permissionManage";
	}
	@RequestMapping(value="/listMenu",method = RequestMethod.POST)
	@ResponseBody
	public String listMenuTree(String roleId){
		System.out.println("roleId:"+roleId);
		List<String> menuIdList = roleResourceService.findResourceByRoleId(roleId);
		List<Menu> root = menuService.findByPid(-1);
		List<MenuTree> menuTreeList = new ArrayList<MenuTree>();
		for(Menu menu:root){
			MenuTree menuTree = new MenuTree();
			menuTree.setId(menu.getId());
			menuTree.setPid(menu.getParent());
			menuTree.setText(menu.getName());
			for(String menuId:menuIdList){
				if(String.valueOf(menu.getId()).equals(menuId)){
					menuTree.setChecked(true);
				}
			}
			menuTreeList.add(menuTree);
		}
		menuTreeList = bulidTree(menuTreeList,menuIdList);
		return JSON.toJSONString(menuTreeList);
	}
	
	@RequestMapping(value="/savePermission",method=RequestMethod.POST)
	@ResponseBody
	public String savePemission(String menus,String roleId){
		String resourcesId[] = menus.split(",");
		List<Permission> permissionList = new ArrayList<Permission>();
		for(String resourceId:resourcesId){
			if(StringUtils.hasText(resourceId)){
				Permission permission = new Permission();
				permission.setRoleId(roleId);
				permission.setResourceId(resourceId);
				permissionList.add(permission);
			}
		}
		roleResourceService.save(permissionList,roleId);
		return ResultInfo.success();
	}
	public List<MenuTree> bulidTree(List<MenuTree> root,List<String> menuIdList){
		for(int i = 0;i<root.size();i++){
			List<MenuTree> menuTreeList = new ArrayList<MenuTree>();
			List<Menu> children = menuService.findByPid(root.get(i).getId());
			for(Menu menu:children){
				MenuTree menuTree = new MenuTree();
				menuTree.setId(menu.getId());
				menuTree.setPid(menu.getParent());
				menuTree.setText(menu.getName());
				for(String menuId:menuIdList){
					if(String.valueOf(menu.getId()).equals(menuId)){
						menuTree.setChecked(true);
					}
				}
				menuTreeList.add(menuTree);
			}
			bulidTree(menuTreeList,menuIdList);
			root.get(i).setChildren(menuTreeList);
		}
		return root;
	}
}
