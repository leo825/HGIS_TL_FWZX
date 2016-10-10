package cn.geobeans.fwzx.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.geobeans.common.enums.JsonResponseStatusEnum;
import cn.geobeans.common.model.JsonResponse;
import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.RouteModel;
import cn.geobeans.fwzx.service.ProjectService;
import cn.geobeans.fwzx.service.RouteService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-4-27上午10:57:58
 */

@Controller
@RequestMapping("/rest")
public class RouteController {
	private static Logger logger = Logger.getLogger(RouteController.class);
	
    private static final String POST = "/service";
    private static final String DELETE = "/service/{id}";
    private static final String PUT = "/service";
    private static final String GET_ALL = "/service";
    private static final String GET_BY_ID = "/service/{id}";
    private static final String GET_EASYUI_QUERY = "/service/easyui_query";
    
	@Resource
	private RouteService service;
	@Resource
	private ProjectService projectService;
	/**
	 * 添加服务
	 *
	 * @param route
	 * @return
	 * @throws Exception
	*/
	@RequestMapping(value = POST, method = RequestMethod.POST)
	@ResponseBody 
	public JsonResponse save(@RequestBody RouteModel route) {
		try {
			int result = service.insert(route);
			if(result > -1){
				return new JsonResponse(route);
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		return new JsonResponse(JsonResponseStatusEnum.ERROR, "服务已存在,保存失败");
	}
	
	/**
	 * 删除服务
	 *
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@RequestMapping(value = DELETE, method = RequestMethod.DELETE)
	@ResponseBody 
	public JsonResponse delete(@PathVariable String id) {
		try {
			int count = service.delete(id);
			if(count > -1){
				return new JsonResponse();				
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		return new JsonResponse(JsonResponseStatusEnum.ERROR, "删除失败");
	}

	/**
	 * 更新应用
	 *
	 * @param route
	 * @return
	 * @throws Exception
	*/
	@RequestMapping(value = PUT, method = RequestMethod.PUT)
	@ResponseBody 
	public JsonResponse update(@RequestBody RouteModel route) throws Exception {
		try {			
			int result = service.update(route);
			if(result > -1){
				return new JsonResponse(route);
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		return new JsonResponse(JsonResponseStatusEnum.ERROR, "更新失败");
	}

	/**
	 * 获取所有服务
	 *
	 * @return
	 */
	@RequestMapping(value = GET_ALL, method = RequestMethod.GET)
	@ResponseBody 
	public JsonResponse getAll() {
		try {
			List<RouteModel> projects = service.findList();
			return new JsonResponse(projects);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
	}

	/**
	 * 通过ID获取应用信息
	 *
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@RequestMapping(value = GET_BY_ID, method = RequestMethod.GET)
	@ResponseBody 
	public JsonResponse get(@PathVariable("id") String id){
		try {
			RouteModel project = service.get(id);
			return new JsonResponse(project);
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
	}

	/**
	 * 查询所有服务路由信息
	 * @throws IOException 
	 * 
	 * 
	 * */
	@RequestMapping(value = GET_EASYUI_QUERY, method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody 
	public JSONObject queryOfEasyui(@RequestParam(value = "name", required = false) String name,
									@RequestParam(value = "projectId", required = false) String projectId){
		JSONObject jsonResult = new JSONObject();		
		List<JSONObject> rows = new ArrayList<JSONObject>();
		int total = 0;
		
		try{
			List<Map<String,Object>> listAll = service.getListByNameOrProjectName(name, projectId);
			for(int i = 0;  !StringUtil.isListEmpty(listAll) && i < listAll.size(); i++){
				JSONObject json = new JSONObject();
				ProjectModel pModel = projectService.get((String) listAll.get(i).get("PROJECT_ID"));
				json.put("id",  listAll.get(i).get("ID"));
				json.put("name", listAll.get(i).get("SERVER_NAME"));
				json.put("serverAddress", listAll.get(i).get("SERVER_ADDR"));
				json.put("project", pModel.getName());
				json.put("dataReturnType", listAll.get(i).get("DATA_RETURN_TYPE"));
				json.put("description", listAll.get(i).get("DESCRIPTION"));
				rows.add(json);
			}
			total = rows.size();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		jsonResult.put("total", total);
		jsonResult.put("rows", rows);
		return jsonResult;
	}
}
