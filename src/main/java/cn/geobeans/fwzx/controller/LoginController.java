package cn.geobeans.fwzx.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdk.nashorn.api.scripting.JSObject;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import cn.geobeans.common.enums.JsonResponseStatusEnum;
import cn.geobeans.common.model.JsonResponse;
import cn.geobeans.fwzx.model.ResourceModel;
import cn.geobeans.fwzx.model.UserModel;
import cn.geobeans.fwzx.service.UserService;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-5-20下午3:37:59
 */

@Controller
@RequestMapping("/rest")
public class LoginController {

	private static Logger logger = Logger.getLogger(LoginController.class);
    private static final String LOGIN = "/login";
	private static final String LOG_OUT = "logOut";
//    private static final String DELETE = "/login/{id}";
//    private static final String PUT = "/login";
//    private static final String GET_ALL = "/login";
    private static final String GET_RESOURCE_BY_ID = "/login/resource/{id}";
    
    @Resource
	private UserService service;
    
	@RequestMapping(value = LOGIN, method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public JsonResponse login(@RequestParam(value = "account", required = false) String account,
							  @RequestParam(value = "password", required = false) String password,
							  HttpServletResponse response,
							  HttpSession session) {
		UserModel user = null;
		List<ResourceModel> resources = null;

		try {
			user = service.getUserByAccount(account);

			if(user!= null && user.getPassword().equals(password)){
				user.setRoleList(service.getRoleListByUserId(user.getId()));
				resources = service.getResourceById(user.getId());
				session.setAttribute("user",user);
				session.setAttribute("resources",resources);
				return new JsonResponse(user);
			}

		} catch (Exception e){
			logger.error(e.getMessage());
		}
		return new JsonResponse(JsonResponseStatusEnum.ERROR, "登陆失败");
	}
	
	@RequestMapping(value = GET_RESOURCE_BY_ID,method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public JsonResponse getResourceById(@PathVariable String id){
		List <ResourceModel> list = null;
		try{
			list = service.getResourceById(id);
			if(list != null){
				return new JsonResponse(list);
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return new JsonResponse(JsonResponseStatusEnum.ERROR, "获取权限失败");
	}

	@RequestMapping(value = LOG_OUT,method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public JsonResponse logOut(HttpSession session){
		JSONObject resultJson = new JSONObject();
		try{
			session.removeAttribute("user");
			session.removeAttribute("resources");
			resultJson.put("result", true);
			resultJson.put("data","用户注销成功");
			return new JsonResponse(resultJson);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return new JsonResponse(JsonResponseStatusEnum.ERROR, "获取权限失败");
	}


}
