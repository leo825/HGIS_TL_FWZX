package cn.geobeans.fwzx.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
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
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.service.ProjectService;
import cn.geobeans.fwzx.service.UsageProjectService;
import cn.geobeans.fwzx.service.UsageService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @version 创建时间:2016-5-1下午1:02:35
 * @parameter E-mail:15895982509@163.com
 */
@Controller
@RequestMapping("/rest")
public class UsageController {
    private static Logger logger = Logger.getLogger(UsageController.class);

    private static final String POST = "/usage";
    private static final String DELETE = "/usage/{id}";
    private static final String PUT = "/usage";
    private static final String GET_ALL = "/usage";
    private static final String GET_BY_ID = "/usage/{id}";
    private static final String GET_EASYUI_QUERY = "/usage/easyui_query";
    private static final String GET_USAGES_EASYUI_QUERY = "/usage/usages_easyui_query";
    private static final String BATCH_ADD = "/usage/batch_add";
    private static final String BATCH_ADD_PROJECT = "/usage/batch_add_project";
    private static final String Update_USAGE_PROJECT = "/usage/update_usage_project";

    @Resource
    private UsageService service;

    @Resource
    private ProjectService projectService;

    @Resource
    private UsageProjectService usageProjectService;

    /**
     * 添加使用者
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = POST, method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse save(@RequestBody UsageModel model) {
        try {
            int result = service.insert(model);
            if (result > 0) {
                return new JsonResponse(model);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "保存失败");
    }

    /**
     * 批量添加使用者
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = BATCH_ADD, method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse batchAdd(@RequestBody JSONObject model) {
        try {

            String ipPrefix = model.getString("ipPrefix");
            String start = model.getString("start");
            String end = model.getString("end");
            String name = model.getString("name");
            String description = model.getString("description");
            int total = Integer.parseInt(end) - Integer.parseInt(start) + 1;
            int successed = 0;
            JSONObject resultJson = new JSONObject();

            for (int i = Integer.parseInt(start); i <= Integer.parseInt(end); i++) {
                UsageModel usage = new UsageModel();
                usage.setIp(ipPrefix + i);
                usage.setName(name);
                usage.setDescription(description);
                int result = service.insert(usage);
                if (result > 0) {
                    successed++;
                    String modelString = JSONObject.fromObject(model).toString();
                    logger.info(modelString + ",用户添加成功");
                }
            }
            resultJson.put("total", total);
            resultJson.put("successed", successed);
            return new JsonResponse(resultJson);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "保存失败");
    }

    /**
     * 批量给使用者赋应用
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = BATCH_ADD_PROJECT, method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse batchAddProject(@RequestBody JSONObject model) {
        int successed = 0;
        UsageModel usage = null;
        ProjectModel project = null;
        String usageString;
        String projectString;
        try {
            JSONObject resultJson = new JSONObject();
            String ipPrefix = model.getString("ipPrefix");
            String start = model.getString("start");
            String end = model.getString("end");
            String projectNames[] = (model.getString("projectNames")).split(",");
            int total = Integer.parseInt(end) - Integer.parseInt(start) + 1;

            for (int i = Integer.parseInt(start); i <= Integer.parseInt(end); i++) {
                usage = service.getByIp(ipPrefix + i);
                if (usage != null) {
                    for (int j = 0; j < projectNames.length; j++) {
                        project = projectService.getProjectByName(projectNames[j]);
                        usageString = JSONObject.fromObject(usage).toString();
                        projectString = JSONObject.fromObject(project).toString();
                        if (!usageProjectService.isUsageProjectExist(usage.getId(), project.getId())) {
                            int result = -1;
                            result = usageProjectService.insert(usage, project);
                            if (result > 0) {
                                successed++;
                                logger.info(usageString + "用户成功添加了应用 " + projectString);
                            }
                        } else {
                            logger.error(usageString + "用户已经存在应用 " + projectString);
                        }
                    }
                }
            }
            resultJson.put("total", total);
            resultJson.put("successed", successed);
            return new JsonResponse(resultJson);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "保存失败");
    }

    /**
     * 删除使用者
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = DELETE, method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse delete(@PathVariable String id, @RequestParam(value = "projectId", required = false) String projectId) {
        try {
            int count = service.delete(id, projectId);
            if (count > 0) {
                return new JsonResponse();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "删除失败");
    }

    /**
     * 更新使用者
     *
     * @param usage
     * @return
     * @throws Exception
     */
    @RequestMapping(value = PUT, method = RequestMethod.PUT)
    @ResponseBody
    public JsonResponse update(@RequestBody UsageModel usage) throws Exception {
        try {
            int result = service.update(usage);
            if (result > 0) {
                return new JsonResponse(usage);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "更新失败");
    }

    /**
     * 通过ID获取使用者信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = GET_BY_ID, method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse get(@PathVariable("id") String id) {
        try {
            UsageModel model = service.get(id);
            return new JsonResponse(model);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
    }

    /**
     * 获取所有使用者
     *
     * @return
     */
    @RequestMapping(value = GET_ALL, method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAll() {
        try {
            List<UsageModel> usages = service.findList();
            return new JsonResponse(usages);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
    }

    /**
     * 获取所有使用者返回easyui
     */
    @RequestMapping(value = GET_USAGES_EASYUI_QUERY, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JSONObject getUsagesOfEasyui(@RequestParam(value = "ip", required = false) String ip,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "page", required = false) Integer page,
                                        @RequestParam(value = "rows", required = false) Integer rows) {

        JSONObject jsonResult = new JSONObject();
        List<JSONObject> rowsReturn = new ArrayList<JSONObject>();
        int total = 0;
        try {
            List<UsageModel> usages = service.getListByParms(ip, name);
            for (int i = 0; !StringUtil.isListEmpty(usages) && i < usages.size(); i++) {
                JSONObject json = new JSONObject();
                json.put("id", usages.get(i).getId());
                json.put("ip", usages.get(i).getIp());
                json.put("user", usages.get(i).getName());
                json.put("description", usages.get(i).getDescription());
                rowsReturn.add(json);
            }
            total = StringUtil.isListEmpty(rowsReturn) ? 0 : rowsReturn.size();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        jsonResult.put("total", total);
        jsonResult.put("rows", rowsReturn);
        return jsonResult;
    }

    /**
     * 获取所有的使用者的信息
     *
     * @throws IOException
     */
    @RequestMapping(value = GET_EASYUI_QUERY, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JSONObject queryOfEasyui(@RequestParam(value = "ip", required = false) String ip,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "projectId", required = false) String projectId,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "rows", required = false) Integer rows) {
        JSONObject jsonResult = new JSONObject();
        List<JSONObject> rowsReturn = new ArrayList<JSONObject>();
        int total = 0;

        try {
            List<UsageModel> usageList = usageProjectService.getUsageListByParams(ip, name, projectId);
            for (int i = 0; !StringUtil.isListEmpty(usageList) && i < usageList.size(); i++) {

                if (!StringUtil.isListEmpty(usageList.get(i).getProjectList())) {
                    for (int j = 0; j < usageList.get(i).getProjectList().size(); j++) {
                        JSONObject json = new JSONObject();
                        json.put("id", usageList.get(i).getId());
                        json.put("ip", usageList.get(i).getIp());
                        json.put("user", usageList.get(i).getName());
                        json.put("description", usageList.get(i).getDescription());
                        json.put("projectId", usageList.get(i).getProjectList().get(j).getId());
                        json.put("projectName", usageList.get(i).getProjectList().get(j).getName());
                        json.put("provider", usageList.get(i).getProjectList().get(j).getProvider());
                        rowsReturn.add(json);
                    }
                }
            }
            total = StringUtil.isListEmpty(rowsReturn) ? 0 : rowsReturn.size();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        jsonResult.put("total", total);
        jsonResult.put("rows", rowsReturn);
        return jsonResult;
    }

    /**
     * 获取所有的使用者的信息
     *
     * @throws IOException
     */
    @RequestMapping(value = Update_USAGE_PROJECT, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JsonResponse updateUsageProject(@RequestParam(value = "usageId", required = false) String usageId,
                                         @RequestParam(value = "projectId", required = false) String projectId,
                                         @RequestParam(value = "newProjectId", required = false) String newProjectId) {
        JSONObject jsonResult = new JSONObject();
        try {
            if (usageProjectService.isUsageProjectExist(usageId, newProjectId)) {
                jsonResult.put("result", false);
                jsonResult.put("data", "这个应用已经添加到使用者上了");
            } else {
                service.delete(usageId, projectId);
                UsageModel usage = service.get(usageId);
                ProjectModel project = projectService.get(newProjectId);
                int result = -1;
                result = usageProjectService.insert(usage, project);
                if (result > 0) {
                    logger.info(usage.getIp() + "用户成功修改了应用" + project.getName());
                    jsonResult.put("result",true);
                    jsonResult.put("data","修改成功");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonResult.put("result", false);
            jsonResult.put("data", e.getMessage());
        }
        return new JsonResponse(jsonResult);
    }


}
