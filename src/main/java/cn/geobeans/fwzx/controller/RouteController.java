package cn.geobeans.fwzx.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.geobeans.common.util.CalendarUtil;
import cn.geobeans.fwzx.init.InitApplicationMethod;
import cn.geobeans.fwzx.util.POIUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author liuxi
 * @version 创建时间:2016-4-27上午10:57:58
 * @parameter E-mail:15895982509@163.com
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
    private static final String BATCH_ADD = "/service/batch_add";

    @Resource
    private RouteService service;
    @Resource
    private ProjectService projectService;
    @Resource
    private InitApplicationMethod initApplicationMethod;

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
            int result1 = service.insert(route);
            int result2 = initApplicationMethod.addServletRoute(route);
            if (result1 > -1 && result2 > -1) {
                return new JsonResponse(route);
            }
        } catch (Exception e) {
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

        RouteModel routeModel = null;
        try {
            routeModel = service.get(id);
            int count1 = service.delete(id);
            int count2 = initApplicationMethod.deleteServletRoute(routeModel);
            if (count1 > -1 && count2 > -1) {
                return new JsonResponse(JsonResponseStatusEnum.SUCCESS, "删除成功");
            }
        } catch (Exception e) {
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

        RouteModel oldRoute = null;
        try {
            oldRoute = service.get(route.getId());
            int result1 = service.update(route);
            int result2 = initApplicationMethod.updateServletRoute(oldRoute, route);

            if (result1 > -1 && result2 > -1) {
                return new JsonResponse(route);
            }
        } catch (Exception e) {
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
    public JsonResponse get(@PathVariable("id") String id) {
        try {
            RouteModel project = service.get(id);
            return new JsonResponse(project);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
    }

    /**
     * 查询所有服务路由信息
     *
     * @throws IOException
     */
    @RequestMapping(value = GET_EASYUI_QUERY, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JSONObject queryOfEasyui(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "projectId", required = false) String projectId) {
        JSONObject jsonResult = new JSONObject();
        List<JSONObject> rows = new ArrayList<JSONObject>();
        int total = 0;

        try {
            List<Map<String, Object>> listAll = service.getListByNameOrProjectName(name, projectId);
            for (int i = 0; !StringUtil.isListEmpty(listAll) && i < listAll.size(); i++) {
                JSONObject json = new JSONObject();
                ProjectModel pModel = projectService.get((String) listAll.get(i).get("PROJECT_ID"));
                json.put("id", listAll.get(i).get("ID"));
                json.put("name", listAll.get(i).get("SERVER_NAME"));
                json.put("serverAddress", listAll.get(i).get("SERVER_ADDR"));
                json.put("project", pModel.getName());
                json.put("dataReturnType", listAll.get(i).get("DATA_RETURN_TYPE"));
                json.put("description", listAll.get(i).get("DESCRIPTION"));
                rows.add(json);
            }
            total = rows.size();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        jsonResult.put("total", total);
        jsonResult.put("rows", rows);
        return jsonResult;
    }


    /**
     * 上传文件
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = BATCH_ADD, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addExcelFile(HttpServletRequest request) {
        String fileName = "";
        String fullPath = "";
        String projectId = "";
        JSONObject result = null;
        try {
            projectId = request.getParameter("projectId");

            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        // 取得当前上传文件的文件名称
                        String myFileName = file.getOriginalFilename();
                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if (myFileName.trim() != "") {
                            fileName = file.getOriginalFilename();

                            fullPath = InitApplicationMethod.EXCEL_PATH + File.separator + addTimeStampTofile(fileName);
                            File localFile = new File(fullPath);
                            file.transferTo(localFile);
                            result = batchAdd(projectId, fullPath);
                            return result.toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result.toString();
    }


    /***
     * 此处是批量添加接口路由的方法
     *
     * @param projectId 就是应用ID
     */
    private JSONObject batchAdd(String projectId, String fullPath) {
        int faileCount = 0;
        Sheet sheet = POIUtil.checkSheetvalidity(fullPath);
        JSONObject result = new JSONObject();
        if (sheet != null) {
            //获得所有数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                //获得第i行对象
                Row row = sheet.getRow(i);
                try {
                    RouteModel routeModel = new RouteModel();
                    routeModel.setProjectId(projectId);
                    routeModel.setServerName(String.valueOf(row.getCell(0)));
                    routeModel.setServerAddr(String.valueOf(row.getCell(1)));
                    routeModel.setDescription(String.valueOf(row.getCell(2)));
                    routeModel.setDataReturnType(String.valueOf(row.getCell(3)));
                    if (service.insert(routeModel) == -1) {
                        logger.error("增加[ " + routeModel.getServerName() + " ]接口失败了，因为在系统中已经存在了！！");
                        faileCount++;
                    }
                } catch (Exception e) {
                    logger.error("获取单元格错误");
                }
            }
            result.put("total", sheet.getLastRowNum());
            result.put("failed", faileCount);
        } else {
            logger.error("exls读取数据异常");
        }
        return result;
    }


    private String addTimeStampTofile(String fileName) {
        String[] tempArray = fileName.split("\\.");
        String newFileName = tempArray[0] + "_" + CalendarUtil.getTimeStr() + "." + tempArray[1];
        return newFileName;
    }
}
