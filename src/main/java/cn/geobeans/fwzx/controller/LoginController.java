package cn.geobeans.fwzx.controller;

import java.util.List;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    private static final String POST = "/login";
//    private static final String DELETE = "/login/{id}";
//    private static final String PUT = "/login";
//    private static final String GET_ALL = "/login";
    private static final String GET_RESOURCE_BY_ID = "/login/resource/{id}";
    
    @Resource
	private UserService service;
    
	@RequestMapping(value = POST, method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public JsonResponse login(@RequestParam(value = "account", required = false) String account,@RequestParam(value = "password", required = false) String password) {
		try {
			UserModel user = service.getUserByAccount(account);
			if(user!= null && user.getPassword().equals(password)){
				user.setRoleList(service.getRoleListByUserId(user.getId()));
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

}
