package com.quartz.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.quartz.common.exception.ServiceException;
import com.quartz.common.json.ResultInfo;
import com.quartz.domain.TaskInfo;
import com.quartz.service.TaskServiceImpl;

@Controller
public class QuartzController {
	@Autowired
	private TaskServiceImpl taskServiceImpl;
	@RequestMapping(value="/quartz")
	public String quartzIndex(){
		return "quartz";
	}
	@ResponseBody
	@RequestMapping(value="/save",method =RequestMethod.POST)
	public String save(TaskInfo info){
		if(info.getId()==0){
			taskServiceImpl.addJob(info);
		}else{
			taskServiceImpl.edit(info);
		}
		return ResultInfo.success();
	}
	
	@ResponseBody
	@RequestMapping(value="/list",method =RequestMethod.POST)
	public String listJobs(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<TaskInfo> taskInfo = taskServiceImpl.list();
		map.put("rows", taskInfo);
		map.put("total", taskInfo.size());
		return JSON.toJSONString(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value="pause/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
	public String pause(@PathVariable String jobName, @PathVariable String jobGroup){
		try {
			taskServiceImpl.pause(jobName, jobGroup);
		} catch (ServiceException e) {
			return ResultInfo.error(-1, e.getMessage());
		}
		return ResultInfo.success();
	}
	
	/**
	 * 重新开始定时任务
	 * @param jobName
	 * @param jobGroup
	 * 2016年10月10日上午9:41:40
	 */
	@ResponseBody
	@RequestMapping(value="resume/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
	public String resume(@PathVariable String jobName, @PathVariable String jobGroup){
		try {
			taskServiceImpl.resume(jobName, jobGroup);
		} catch (ServiceException e) {
			return ResultInfo.error(-1, e.getMessage());
		}
		return ResultInfo.success();
	}
	
	/**
	 * 删除定时任务
	 * @param jobName
	 * @param jobGroup
	 * 2016年10月9日下午1:52:20
	 */
	@ResponseBody
	@RequestMapping(value="delete/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
	public String delete(@PathVariable String jobName, @PathVariable String jobGroup){
		try {
			taskServiceImpl.delete(jobName, jobGroup);
		} catch (ServiceException e) {
			return ResultInfo.error(-1, e.getMessage());
		}
		return ResultInfo.success();
	}
}
