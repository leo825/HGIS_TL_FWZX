package cn.geobeans.fwzx.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.geobeans.fwzx.init.InitApplicationMethod;
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
import cn.geobeans.common.util.CalendarUtil;
import cn.geobeans.common.util.ProjectUtil;
import cn.geobeans.fwzx.model.OperationModel;
import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.service.OperationService;
import cn.geobeans.fwzx.service.ProjectService;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @version 创建时间:2016-5-5上午10:14:56
 * @parameter E-mail:15895982509@163.com
 */
@Controller
@RequestMapping("/rest")
public class OpreationController {

    private static Logger logger = Logger.getLogger(OpreationController.class);

    private static final String POST = "/operation";
    private static final String DELETE = "/operation/{id}";
    private static final String GET_ALL = "/operation";
    private static final String GET_EASYUI_QUERY = "/operation/easyui_query";
    private static final String GET_COORDINATE = "/operation/get_coordinate";
    private static final String GET_OPERATION_REPORT = "/operation/get_opreation_report";
    private static final String GET_DOWNLOAD = "/operation/download_report";
    private static final String DELETE_REPORT = "/operation/delete_report";


    @Resource
    private OperationService service;

    @Resource
    private ProjectService projectService;


    /**
     * 添加操作记录
     *
     * @param operation
     * @return
     * @throws Exception
     */
    @RequestMapping(value = POST, method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse save(@RequestBody OperationModel operation) {
        try {
            int result = service.insert(operation);
            if (result > -1) {
                return new JsonResponse(operation);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "服务已存在,保存失败");
    }

    /**
     * 删除操作记录
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
            if (count > -1) {
                return new JsonResponse();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "删除失败");
    }

    /**
     * 获取所有操作记录
     *
     * @return
     */
    @RequestMapping(value = GET_ALL, method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAll() {
        try {
            List<OperationModel> operations = service.findList();
            return new JsonResponse(operations);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
    }


    /**
     * 查询所有记录
     *
     * @throws IOException
     */
    @RequestMapping(value = GET_EASYUI_QUERY, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JSONObject queryOfEasyui(@RequestParam(value = "result", required = false) String result,
                                    @RequestParam(value = "projectName", required = false) String projectName,
                                    @RequestParam(value = "startTime", required = false) String startTime,
                                    @RequestParam(value = "endTime", required = false) String endTime) {
        JSONObject jsonResult = new JSONObject();
        List<JSONObject> rows = new ArrayList<JSONObject>();
        int total = 0;

        try {
            result = StringUtil.isNull(result) ? "" : result;
            projectName = StringUtil.isNull(projectName) ? "" : StringUtil.encodeStr(projectName);

            List<Map<String, Object>> listAll = service.getListByParms(result, projectName, startTime, endTime);
            for (int i = 0; !StringUtil.isListEmpty(listAll) && i < listAll.size(); i++) {
                JSONObject json = new JSONObject();
                json.put("id", listAll.get(i).get("ID"));
                json.put("ip", listAll.get(i).get("IP"));
                json.put("user", listAll.get(i).get("USER_NAME"));
                json.put("server", listAll.get(i).get("SERVER_NAME"));
                json.put("project", listAll.get(i).get("PROJECT_NAME"));
                json.put("result", listAll.get(i).get("RESULT"));
                json.put("date", listAll.get(i).get("TO_CHAR(OPERATE_TIME,'YYYY-MM-DDHH24:MI:SS')").toString());
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
     * 获取时间点的记录
     *
     * @throws IOException
     */
    @RequestMapping(value = GET_COORDINATE, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JsonResponse GetCoordinate(@RequestParam(value = "projectId", required = false) String projectId) {
        JSONObject jsonObject = new JSONObject();
        projectId = StringUtil.isNull(projectId) ? "" : projectId;
        Calendar cal = Calendar.getInstance();
        long getTime = cal.getTimeInMillis();
        ProjectModel project = null;
        String projectName = "";
        try {
            int count = service.getCountOfProjectByTime(projectId, CalendarUtil.getTimeStr(cal.getTime()));
            if (!StringUtil.isNull(projectId)) {
                project = projectService.get(projectId);
                projectName = project.getName();
            }
            jsonObject.put("projectName", projectName);
            jsonObject.put("x", getTime);
            jsonObject.put("y", count);
            return new JsonResponse(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "获取失败");
    }

    /**
     * 加载日志文件
     */
    @RequestMapping(value = GET_OPERATION_REPORT, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JSONObject getOperationReport() {
        JSONObject jsonResult = new JSONObject();
        List<JSONObject> list = new ArrayList<JSONObject>();
        int total = 0;
        try {
            File directory = new File(InitApplicationMethod.REPORTS_PATH);
            if (directory.exists()) {
                File[] files = directory.listFiles();
                total = files.length;
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName();
                    JSONObject json = new JSONObject();
                    json.put("id", fileName.substring(0, 10));
                    json.put("name", fileName);
                    json.put("date", fileName.substring(0, 10));
                    json.put("type", "log");
                    list.add(json);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        Collections.reverse(list);
        jsonResult.put("total", total);
        jsonResult.put("rows", list);
        return jsonResult;
    }

    /**
     * 下载
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = GET_DOWNLOAD, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JsonResponse download(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = request.getParameter("fileName");
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            String path = InitApplicationMethod.REPORTS_PATH + File.separator + fileName;
            InputStream inputStream = new FileInputStream(new File(path));

            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.flush();
            os.close();
            inputStream.close();
            return new JsonResponse();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new JsonResponse(JsonResponseStatusEnum.ERROR, e.getMessage());
        }
    }

    /**
     * 删除日志
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = DELETE_REPORT, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public JsonResponse delete_report(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = request.getParameter("fileName");
            response.setCharacterEncoding("utf-8");
            String path = InitApplicationMethod.REPORTS_PATH + File.separator + fileName;
            if (StringUtil.deleteFile(path)) {
                return new JsonResponse("删除成功");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new JsonResponse(JsonResponseStatusEnum.ERROR, e.getMessage());
        }
        return new JsonResponse(JsonResponseStatusEnum.ERROR, "文件不存在,删除失败");
    }

}
