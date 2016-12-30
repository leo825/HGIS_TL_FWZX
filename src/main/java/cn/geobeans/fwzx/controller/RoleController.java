//package cn.geobeans.fwzx.controller;
//
//import cn.geobeans.common.enums.JsonResponseStatusEnum;
//import cn.geobeans.common.model.JsonResponse;
//import cn.geobeans.fwzx.model.Role;
//import cn.geobeans.fwzx.service.RoleService;
//import cn.geobeans.fwzx.util.StringUtil;
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
// * @author liuxi
// * @version 创建时间:2016-5-26下午7:24:22
// * @parameter E-mail:15895982509@163.com
// */
//
//@Controller
//@RequestMapping("/rest")
//public class RoleController {
//
//    private static Logger logger = Logger.getLogger(RoleController.class);
//
//    private static final String POST = "/role";
//    private static final String DELETE = "/role/{id}";
//    private static final String PUT = "/role";
//    private static final String GET_ALL = "/role";
//    private static final String GET_BY_ID = "/role/{id}";
//    private static final String GET_EASYUI_QUERY = "/role/easyui_query";
//
//    @Resource
//    private RoleService service;
//
//    @RequestMapping(value = POST, method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse save(@RequestBody Role mode) {
//        try {
//            int result = service.insert(mode);
//            if (result > -1) {
//                return new JsonResponse(mode);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "服务已存在,保存失败");
//    }
//
//    /**
//     * 删除角色
//     *
//     * @param id
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = DELETE, method = RequestMethod.DELETE)
//    @ResponseBody
//    public JsonResponse delete(@PathVariable String id) {
//        try {
//            int count = service.delete(id);
//            if (count > -1) {
//                return new JsonResponse();
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "删除失败");
//    }
//
//    /**
//     * 更新角色
//     *
//     * @param role
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = PUT, method = RequestMethod.PUT)
//    @ResponseBody
//    public JsonResponse update(@RequestBody Role role) {
//        try {
//            int result = service.update(role);
//            if (result > -1) {
//                return new JsonResponse(role);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "更新失败");
//    }
//
//    /**
//     * 获取所有角色
//     *
//     * @return
//     */
//    @RequestMapping(value = GET_ALL, method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getAll() {
//        try {
//            List<Role> roles = service.findList();
//            return new JsonResponse(roles);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
//    }
//
//    /**
//     * 查询所有的角色信息
//     *
//     * @throws IOException
//     */
//    @RequestMapping(value = GET_EASYUI_QUERY, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public JSONObject queryOfEasyui(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "description", required = false) String description) {
//        JSONObject jsonResult = new JSONObject();
//        List<JSONObject> rows = new ArrayList<JSONObject>();
//        int total = 0;
//
//        try {
//            List<Role> listAll = service.getListByNameAndDescription(name, description);
//            for (int i = 0; !StringUtil.isListEmpty(listAll) && i < listAll.size(); i++) {
//                JSONObject json = new JSONObject();
//                json.put("id", listAll.get(i).getId());
//                json.put("name", listAll.get(i).getName());
//                json.put("description", listAll.get(i).getDescription());
//                rows.add(json);
//            }
//            total = rows.size();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        jsonResult.put("total", total);
//        jsonResult.put("rows", rows);
//        return jsonResult;
//    }
//
//}
