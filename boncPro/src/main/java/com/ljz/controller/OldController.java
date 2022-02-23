package com.ljz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ljz.entity.PageRequest;
import com.ljz.entity.PageResult;
import com.ljz.model.TableList;
import com.ljz.model.TableListKey;
import com.ljz.service.factory.CreateSqlFactory;
import com.ljz.service.impl.LogServiceImpl;
import com.ljz.service.impl.TableListServiceImpl;
import com.ljz.service.impl.TbWarnServiceImpl;

@Controller
public class OldController {
	
	private static final Logger logger = LoggerFactory.getLogger(OldController.class);
	
	@Autowired
	TbWarnServiceImpl warnService;
	
	@Autowired
	CreateSqlFactory factory;
	
	@Autowired
	TableListServiceImpl service;
	
	@Autowired
	LogServiceImpl logService;
	
	@RequestMapping(value="/interface")
	public String interfacePage(HttpServletRequest request,@RequestParam(value="ds") String ds,@RequestParam(value="inter") String inter,Model model) {
		System.out.println(request.getHeader("X-PJAX")+"interface");
		model.addAttribute("ds", ds);
		model.addAttribute("inter", inter);
		return "interface";
	}
	
	
	@RequestMapping("/detail")
	public String detail(Model model,@Param(value="tableName") String tableName) {
		model.addAttribute("tableName",tableName);
		return "detail";
	}
	
	/**
	 * 查字段
	 * @param model
	 * @param tableName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findDetailPage")
    public Object findDetailPage(@RequestBody PageRequest pageRequest) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("table_ename", pageRequest.getTable_ename());
		condition.put("ename", pageRequest.getEname());
		pageRequest.setCondition(condition);
        return service.findDetailPage(pageRequest);
    }
	
	@RequestMapping("/edit")
	public String eidt(Model model,@Param(value="tableName") String tableName,@Param(value="ename") String ename) {
		model.addAttribute("tableName",tableName);
		model.addAttribute("ename",ename);
		return "edit";
	}
	
	@ResponseBody
	@RequestMapping("/queryEdit")
	public TableList queryEdit(Model model,@Param(value="tableName") String tableName,@Param(value="ename") String ename) {
		TableListKey key = new TableListKey();
		key.setEname(ename);
		key.setTableEname(tableName);
		return service.selectByPrimaryKey(key);
	}
	
	@ResponseBody
	@RequestMapping("/saveEdit")
	public String saveEdit(Model model,HttpServletRequest request,HttpServletResponse response) {
		String table_ename =request.getParameter("table_ename");
		String table_cname =request.getParameter("table_cname");
		String ename =request.getParameter("ename");
		String cname =request.getParameter("cname");
		String column_type =request.getParameter("column_type");
		String null_flag =request.getParameter("null_flag");
		String pk_flag =request.getParameter("pk_flag");
		TableList tableList =new TableList(table_ename, table_cname, ename, cname, pk_flag, column_type, null_flag, "", "", "");
		service.save(tableList);
		return "成功";
	}
	
	@RequestMapping("/createSqlPage_bak")
	public String createSqlPage_bak(Model model,HttpServletRequest request) {
		String ids = request.getParameter("ids");
		System.out.println(ids);
		List<String> list = JSON.parseArray(ids,String.class);
		String[] tableNames = list.toArray(new String[list.size()]);
		model.addAttribute("list", service.getTableList(tableNames));
		return "createSql";
	}
	
	
	@ResponseBody
	@RequestMapping(value="/warnResult")
	public Object warnResult(@RequestBody PageRequest pageRequest) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("boncDate", pageRequest.getEname());
		pageRequest.setCondition(condition);
	    return warnService.findPage(pageRequest);
	}
	
	@RequestMapping(value="/warn")
	public String warn(HttpServletResponse response,HttpServletRequest request) {
	   
	    return "warn";
	}
	
	@ResponseBody
	@RequestMapping(value="/logResult")
	public Object logResult(@RequestBody PageRequest pageRequest) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("boncDate", pageRequest.getEname());
		pageRequest.setCondition(condition);
	    return logService.findPage(pageRequest);
	}
	
	/**
	 * 查表名
	 * @param pageRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findPage")
	public Object findPage(@RequestBody PageRequest pageRequest) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("table_ename", pageRequest.getTable_ename());
		pageRequest.setCondition(condition);
        return service.findPage(pageRequest);
    }
	@ResponseBody
	@RequestMapping(value="/findPage_bak",method = RequestMethod.GET)
    public Map<String, Object> findPage_bak(String idx) {
		Map<String, String> condition = new HashMap<String, String>();
		PageRequest pageRequest = new PageRequest();
		pageRequest.setPageNum(1);
		pageRequest.setPageSize(6);
		pageRequest.setCondition(condition);
		PageResult findPage = service.findPage(pageRequest);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", findPage.getTotalSize());
        resultMap.put("recordsFiltered", findPage.getTotalSize());
        resultMap.put("total", findPage.getTotalSize());
        resultMap.put("data", findPage.getContent());
        return resultMap;
    }
	
	@RequestMapping(value="/newIndex")
	public String newIndex(HttpServletRequest request,Model model) {
		logger.info("hello,world");
		return "newIndex";
	}
	
	/*@ResponseBody
	@RequestMapping(value="/testTree",method = RequestMethod.GET)
    public List<Map<String, Object>> testTree() {
		List list = new ArrayList();
		Map map = new Map();
		map.put("", value)
        return resultMap;
    }*/

}
