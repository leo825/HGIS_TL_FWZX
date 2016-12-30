//package cn.geobeans.fwzx.controller;
//
//import cn.geobeans.common.enums.JsonResponseStatusEnum;
//import cn.geobeans.common.model.JsonResponse;
//import cn.geobeans.fwzx.model.Role;
//import cn.geobeans.fwzx.model.User;
//import cn.geobeans.fwzx.service.RoleService;
//import cn.geobeans.fwzx.service.UserRoleService;
//import cn.geobeans.fwzx.service.UserService;
//import cn.geobeans.fwzx.util.StringUtil;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *@author liuxi
// *@parameter E-mail:15895982509@163.com
// *@version 创建时间:2016-6-2下午6:38:22
// */
//@Controller
//@RequestMapping("/rest")
//public class UserController {
//	private static Logger logger = Logger.getLogger(UserController.class);
//
//	private static final String POST = "/user";
//	private static final String DELETE = "/user/{id}";
//	private static final String PUT = "/user";
//	private static final String GET_ALL = "/user";
//	private static final String GET_BY_ID = "/user/{id}";
//	private static final String GET_ROLE_EASYUI_QUERY = "/user/get_role_easyui_query";
//	private static final String GET_USERS_EASYUI_QUERY = "/user/get_users_easyui_query";
//	private static final String ADD_ROLE = "/user/add_role";
//
//	@Resource
//	private UserService service;
//
//	@Resource
//	private RoleService roleService;
//
//	@Resource
//	private UserRoleService userRoleService;
//
//	/**
//	 * 添加用户
//	 *
//	 * @param model
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = POST, method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResponse save(@RequestBody User model) {
//		try {
//			int result = service.insert(model);
//			if (result > 0) {
//				return new JsonResponse(model);
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "保存失败");
//	}
//
//
//	/**
//	 * 批量给用户赋应用
//	 *
//	 * @param model
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = ADD_ROLE, method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResponse batchAddProject(@RequestBody JSONObject model) {
//		try {
//			String roleNames[] = (model.getString("roleNames")).split(",");
//			String userId = model.getString("userId");
//			User user = null;
//			Role role = null;
//			int total = roleNames.length;
//			int successed = 0;
//			JSONObject resultJson = new JSONObject();
//			String roleString;
//			String resourceString;
//			if (!StringUtil.isListEmpty(userRoleService.getRoleListByUserId(userId))) {
//				userRoleService.deleteAllUserRoleByUserId(userId);
//			}
//			for (int i = 0; i < roleNames.length; i++) {
//				user = service.get(userId);
//				role = roleService.getRoleByName(roleNames[i]);
//				roleString = JSONObject.fromObject(user).toString();
//				resourceString = JSONObject.fromObject(role).toString();
//				int result = -1;
//				result = userRoleService.insert(user, role);
//				if (result > 0) {
//					successed++;
//					logger.info(roleString + "角色成功添加了资源 " + resourceString);
//				} else {
//					logger.error(roleString + "角色添加资源 " + resourceString + "失败了");
//				}
//			}
//			resultJson.put("total", total);
//			resultJson.put("successed", successed);
//			return new JsonResponse(resultJson);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "更新权限失败");
//	}
//
//	/**
//	 * 删除用户
//	 *
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = DELETE, method = RequestMethod.DELETE)
//	@ResponseBody
//	public JsonResponse delete(@PathVariable String id) {
//		try {
//			int count = service.delete(id);
//			if (count > 0) {
//				return new JsonResponse();
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "删除失败");
//	}
//
//	/**
//	 * 更新用户
//	 *
//	 * @param user
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = PUT, method = RequestMethod.PUT)
//	@ResponseBody
//	public JsonResponse update(@RequestBody User user) throws Exception {
//		try {
//			int result = service.update(user);
//			if (result > 0) {
//				return new JsonResponse(user);
//			}
//
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "更新失败");
//	}
//
//	/**
//	 * 通过ID获取用户信息
//	 *
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = GET_BY_ID, method = RequestMethod.GET)
//	@ResponseBody
//	public JsonResponse get(@PathVariable("id") String id) {
//		try {
//			User model = service.get(id);
//			return new JsonResponse(model);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
//	}
//
//	/**
//	 * 获取所有用户
//	 *
//	 * @return
//	 */
//	@RequestMapping(value = GET_ALL, method = RequestMethod.GET)
//	@ResponseBody
//	public JsonResponse getAll() {
//		try {
//			List<User> users = service.findList();
//			return new JsonResponse(users);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
//	}
//
//	/**
//	 *
//	 * 获取所有用户返回easyui
//	 *
//	 * */
//	@RequestMapping(value = GET_USERS_EASYUI_QUERY, method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public JSONObject getusersOfEasyui(@RequestParam(value = "account", required = false) String account,
//										@RequestParam(value = "nickname", required = false) String nickname,
//										@RequestParam(value = "page", required = false) Integer page,
//										@RequestParam(value = "rows", required = false) Integer rows) {
//
//		JSONObject jsonResult = new JSONObject();
//		List<JSONObject> rowsReturn = new ArrayList<JSONObject>();
//		int total = 0;
//		try {
//			List<User> users = service.getUsersByParams(account, nickname);
//			for (int i = 0; !StringUtil.isListEmpty(users) && i < users.size(); i++) {
//				JSONObject json = new JSONObject();
//				json.put("id", users.get(i).getId());
//				json.put("account", users.get(i).getAccount());
//				json.put("nickname", users.get(i).getNickname());
//				json.put("telephone", users.get(i).getTelephone());
//				rowsReturn.add(json);
//			}
//			total = StringUtil.isListEmpty(rowsReturn) ? 0 : rowsReturn.size();
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//
//		jsonResult.put("total", total);
//		jsonResult.put("rows", rowsReturn);
//		return jsonResult;
//	}
//
//	/**
//	 * 查询用户的角色
//	 *
//	 * @throws IOException
//	 *
//	 *
//	 * */
//	@RequestMapping(value = GET_ROLE_EASYUI_QUERY, method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public JSONObject[] queryOfEasyui(@RequestParam(value = "id", required = false) String id) {
//
//		JSONObject json = new JSONObject();
//		JSONArray children = new JSONArray();
//		try {
//			List<Role> roles = roleService.findList();
//			List<Role> userRoles = service.getRoleListByUserId(id);
//			for (int i = 0; !StringUtil.isListEmpty(roles) && i < roles.size(); i++) {
//				JSONObject temp = new JSONObject();
//				temp.put("text", roles.get(i).getName());
//				if (userRoles.contains(roles.get(i))) {
//					temp.put("checked", true);
//				}
//				children.add(temp);
//			}
//			json.put("text", "全部角色");
//			json.put("state", "open");
//			json.put("children", children);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JSONObject[] { json };
//	}
//
//}
