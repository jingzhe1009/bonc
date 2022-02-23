package com.ljz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ljz.entity.ParamEntity;
import com.ljz.model.DataFuncConfig;
import com.ljz.model.DataFuncRegister;
import com.ljz.service.impl.DataFuncRegisterService;

@Controller
@RequestMapping("/func")
public class FuncController extends MainController{
	
	private static final Logger logger = LoggerFactory.getLogger(FuncController.class);
	
	@Autowired
	DataFuncRegisterService funcService;
	
	@ResponseBody
	@RequestMapping(value="/queryFunc",method = RequestMethod.GET)
    public Map<String, Object> queryFunc(DataFuncRegister record) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if("0".equals(record.getUseType())){
			List<DataFuncRegister> list1 = new ArrayList<DataFuncRegister>();
			List<DataFuncRegister> list2 = new ArrayList<DataFuncRegister>();
			record.setUseType(null);
			List<DataFuncRegister> list = funcService.queryAll(record);
			for(DataFuncRegister reg:list){
				if("1".equals(reg.getUseType())){
					list1.add(reg);
				}else if("2".equals(reg.getUseType())){
					list2.add(reg);
				}
			}
			resultMap.put("list1", list1);
			resultMap.put("list2", list2);
			return resultMap;
		}else{
			List<DataFuncRegister> list = funcService.queryAll(record);
	        resultMap.put("recordsTotal", list.size());
	        resultMap.put("recordsFiltered", list.size());
	        resultMap.put("total", list.size());
	        resultMap.put("data", list);
	        logger.info("funcRegister query success");
	        return resultMap;
		}
    }
	
	@ResponseBody
	@RequestMapping(value="/createFunc",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> createFunc(DataFuncRegister record) {
		Map<String, Object> resultMap= new HashMap<String,Object>();
		int existFunc = funcService.existFunc(record.getFuncName());
		if (existFunc!=0){
			resultMap.put("message","保存失败，函数已存在");
			return resultMap;
		}

		funcService.insert(record);
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("useType", record.getUseType());
		resultMap.put("message","保存成功");
		logger.info("funcRegister insert success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/editFunc",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> editFunc(DataFuncRegister record) {
		funcService.update(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("useType", record.getUseType());
		resultMap.put("message","保存成功");
		logger.info("funcRegister edit success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/deleteFunc",method = RequestMethod.POST)
    public Map<String, Object> deleteFunc(DataFuncRegister record) {
		funcService.delete(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("useType", record.getUseType());
		logger.info("funcRegister delete success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/queryConfig",method = RequestMethod.POST)
    public Map<String, Object> queryConfig(String dataSrcAbbr,String useType,String funcName) {
		DataFuncConfig config = new DataFuncConfig();
		config.setDataSrcAbbr(dataSrcAbbr);
		config.setUseType(useType);
		config.setFuncName(funcName);
		List<Map<String, String>> list = funcService.queryAllConfig(config);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		/*if(list.size()<1) {
			DataFuncRegister record = new DataFuncRegister();
			record.setUseType(useType);
			record.setFuncName(funcName);
			resultMap.put("tables", funcService.queryAll(record));
		}else {
			resultMap.put("tables", list);
			resultMap.put("dataSrcAbbr", dataSrcAbbr);
			resultMap.put("useType", useType);
			//resultMap.put("word", funcService.getWord(dataSrcAbbr,useType));
		}*/
		resultMap.put("tables", list);
		resultMap.put("dataSrcAbbr", dataSrcAbbr);
		resultMap.put("useType", useType);
		logger.info("funcConfig query success");
        return resultMap;
    }

	@ResponseBody
	@RequestMapping(value="/queryContent",method = RequestMethod.POST)
    public Map<String, Object> queryContent(String dataSrcAbbr,String useType) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		System.out.println(dataSrcAbbr+useType);
		resultMap.put("word", funcService.getWord(dataSrcAbbr,useType));
		logger.info("funcContent query success");
        return resultMap;
    }

	@ResponseBody
	@RequestMapping(value="/saveConfig",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> saveConfig(@RequestBody(required=false) ParamEntity param) {
		String[] tables = param.getTables();
		String[] params = param.getParam();
		String[] descs = param.getDesc();
		String [] type = param.getFuncType();
		if(tables.length<1) {
			DataFuncConfig  record = new DataFuncConfig();
			record.setDataSrcAbbr(param.getDataSrcAbbr());
			record.setUseType(param.getDbType());
			funcService.deleteConfig(record);
		}else {
			DataFuncConfig  config = new DataFuncConfig();
			config.setDataSrcAbbr(param.getDataSrcAbbr());
			config.setUseType(param.getDbType());
			funcService.deleteConfig(config);
			List<DataFuncConfig> list = new ArrayList<DataFuncConfig>();
			for(int i=0;i<tables.length;i++) {
				DataFuncConfig  record = new DataFuncConfig();
				String funcName = tables[i];
				String funcParam = params[i];
				String funcParamDesc = descs[i];
				String funcType = type[i];
				record.setDataSrcAbbr(param.getDataSrcAbbr());
				record.setFuncName(funcName);
				record.setUseType(param.getDbType());
				record.setFuncParam(funcParam);
				record.setFuncParamDesc(funcParamDesc);
				record.setFunc_order(i);
				record.setFuncType(funcType);
				list.add(record);
			}
			funcService.batchInsertConfig(list);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("tables", tables);
		/*resultMap.put("word", funcService.getWord(record.getDataSrcAbbr(),param.getDbType()));*/
		logger.info("funcConfig insert success");
        return resultMap;
    }

	/*void downMonitor(HttpServletResponse response){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(new Date());
		String iniPath = config.getIniPath()+"data_src.ini";
		String word = funcService.getWord(dataSrcAbbr,useType);
		FileUtil.write(iniPath, word,config.getFileEncode());
		ServletOutputStream outputStream = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
						"attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
			outputStream = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }*/

}
