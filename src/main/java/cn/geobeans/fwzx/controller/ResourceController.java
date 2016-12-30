//package cn.geobeans.fwzx.controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.geobeans.common.enums.JsonResponseStatusEnum;
//import cn.geobeans.common.model.JsonResponse;
//import cn.geobeans.fwzx.model.Resource;
//import cn.geobeans.fwzx.model.Role;
//import cn.geobeans.fwzx.service.ResourceService;
//import cn.geobeans.fwzx.service.RoleResourceService;
//import cn.geobeans.fwzx.service.RoleService;
//import cn.geobeans.fwzx.util.StringUtil;
//
///**
// * @author liuxi
// * @parameter E-mail:15895982509@163.com
// * @version 创建时间:2016-5-26下午11:04:14
// */
//@Controller
//@RequestMapping("/rest")
//public class ResourceController {
//
//	private static Logger logger = Logger.getLogger(ResourceController.class);
//
//	private static final String POST = "/resource";
//	private static final String DELETE = "/resource/{id}";
//	private static final String PUT = "/resource";
//	private static final String GET_ALL = "/resource";
//	private static final String GET_BY_ID = "/resource/{id}";
//	private static final String GET_EASYUI_QUERY = "/resource/easyui_query";
//	private static final String ADD_PREMISSION = "/resource/add_premission";
//
//	@javax.annotation.Resource
//	private ResourceService service;
//
//	@javax.annotation.Resource
//	private RoleService roleService;
//
//	@javax.annotation.Resource
//	private RoleResourceService roleResourceService;
//
//	@RequestMapping(value = POST, method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResponse save(@RequestBody Resource mode) {
//		try {
//			int result = service.insert(mode);
//			if (result > -1) {
//				return new JsonResponse(mode);
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "服务已存在,保存失败");
//	}
//
//	/**
//	 * 删除资源权限
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
//			if (count > -1) {
//				return new JsonResponse();
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "删除失败");
//	}
//
//	/**
//	 * 更新资源权限
//	 *
//	 * @param resource
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = PUT, method = RequestMethod.PUT)
//	@ResponseBody
//	public JsonResponse update(@RequestBody Resource resource) {
//		try {
//			int result = service.update(resource);
//			if (result > -1) {
//				return new JsonResponse(resource);
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "更新失败");
//	}
//
//	/**
//	 * 获取所有资源权限
//	 *
//	 * @return
//	 */
//	@RequestMapping(value = GET_ALL, method = RequestMethod.GET)
//	@ResponseBody
//	public JsonResponse getAll() {
//		try {
//			List<Resource> resources = service.findList();
//			return new JsonResponse(resources);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
//	}
//
//	/**
//	 * 查询所有的资源权限信息
//	 *
//	 * @throws IOException
//	 *
//	 *
//	 * */
//	@RequestMapping(value = GET_EASYUI_QUERY, method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//	public JSONObject[] queryOfEasyui(@RequestParam(value = "id", required = false) String id) {
//
//		JSONObject json = new JSONObject();
//		JSONArray children = new JSONArray();
//		try {
//			List<Resource> resources = service.findList();
//			List<Resource> roleResources = roleService.getResourceByRoleId(id);
//			for (int i = 0; !StringUtil.isListEmpty(resources) && i < resources.size(); i++) {
//				JSONObject temp = new JSONObject();
//				temp.put("text", resources.get(i).getName());
//				if (roleResources.contains(resources.get(i))) {
//					temp.put("checked", true);
//				}
//				children.add(temp);
//			}
//			json.put("text", "全部权限");
//			json.put("state", "open");
//			json.put("children", children);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return new JSONObject[] { json };
//	}
//
//	/**
//	 * 给角色赋权限
//	 *
//	 * @return
//	 */
//	@RequestMapping(value = ADD_PREMISSION, method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResponse addPremission(@RequestBody JSONObject model) {
//		try {
//			String resourceNames[] = (model.getString("resourceNames")).split(",");
//			String roleId = model.getString("roleId");
//			Role role = null;
//			Resource resource = null;
//			int total = resourceNames.length;
//			int successed = 0;
//			JSONObject resultJson = new JSONObject();
//			String roleString;
//			String resourceString;
//			if (!StringUtil.isListEmpty(roleService.getResourceByRoleId(roleId))) {
//				roleResourceService.deleteAllRoleResourceByRoleId(roleId);
//			}
//			for (int i = 0; i < resourceNames.length; i++) {
//				role = roleService.get(roleId);
//				resource = service.getResourceByName(resourceNames[i]);
//				roleString = JSONObject.fromObject(role).toString();
//				resourceString = JSONObject.fromObject(resource).toString();
//				int result = -1;
//				result = roleResourceService.insert(role, resource);
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
//}
