//package cn.geobeans.fwzx.controller;
//
//import cn.geobeans.common.enums.JsonResponseStatusEnum;
//import cn.geobeans.common.model.JsonResponse;
//import cn.geobeans.fwzx.init.InitApplicationMethod;
//import cn.geobeans.fwzx.model.Project;
//import cn.geobeans.fwzx.service.ProjectService;
//import cn.geobeans.fwzx.util.HttpUtil;
//import cn.geobeans.fwzx.util.StringUtil;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
///**
//* @author liuxi
//* @version 创建时间:2016-5-1上午10:38:21
//* @parameter E-mail:15895982509@163.com
//*/
//@Controller
//@RequestMapping("/rest")
//public class ProjectController {
//    private static Logger logger = Logger.getLogger(ProjectController.class);
//
//    private static final String POST = "/project";
//    private static final String DELETE = "/project/{id}";
//    private static final String PUT = "/project";
//    private static final String GET_ALL = "/project";
//    private static final String GET_ALL_PROVIDER = "/project/get_all_providers";
//    private static final String GET_BY_ID = "/project/{id}";
//    private static final String GET_EASYUI_QUERY = "/project/easyui_query";
//    private static final String GET_STATE = "/project/get_state";
//    private static final String GET_PROVIDERS_STATISTICS = "/project/get_providers_statistics";
//    private static final String GET_USAGES_STATISTICS = "/project/get_usages_statistics";
//    private static final String ADD_FILE = "/project/add_file";
//    private static final String DOWNLOAD_FILE = "/project/download_file";
//    private static final String CHECK_PROJECT = "/project/check_project/{id}";
//    private static final String GET_PROJECT_TREE = "/project/get_project_tree";
//
//    @Resource
//    private ProjectService service;
//
//
//    /**
//     * 添加应用
//     *
//     * @param project
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = POST, method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse save(@RequestBody Project project) {
//        try {
//            int result = service.add(project);
//            if (result > -1) {
//                return new JsonResponse(project);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "服务已存在,保存失败");
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = ADD_FILE, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public JsonResponse addFile(HttpServletRequest request) {
//
//        try {
//            String id = request.getParameter("id");
//            Project model = service.get(id);
//            String fileDir = InitApplicationMethod.FILE_PATH + File.separator + model.getName();
//            String fileName = "";
//            String fullPath = "";
//            // 创建一个通用的多部分解析器
//            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//            // 判断 request 是否有文件上传,即多部分请求
//            if (multipartResolver.isMultipart(request)) {
//                // 转换成多部分request
//                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//                // 取得request中的所有文件名
//                Iterator<String> iter = multiRequest.getFileNames();
//                while (iter.hasNext()) {
//                    // 取得上传文件
//                    MultipartFile file = multiRequest.getFile(iter.next());
//                    if (file != null) {
//                        // 取得当前上传文件的文件名称
//                        String myFileName = file.getOriginalFilename();
//                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//                        if (myFileName.trim() != "") {
//                            fileName = file.getOriginalFilename();
//                            fullPath = fileDir + File.separator + fileName;
//                            if (StringUtil.checkDir(fileDir)) {
//                                File localFile = new File(fullPath);
//                                file.transferTo(localFile);
//                                model.setFileName(fileName);
//                                model.setFilePath(fullPath);
//                                int result = service.update(model);
//                                if (result > -1) {
//                                    return new JsonResponse(model);
//                                }
//                            } else {
//                                logger.error("附件上传目录[" + fileDir + "]创建失败了！！！");
//                            }
//
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "文件上传失败");
//    }
//
//    /**
//     * 删除应用
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
//     * 更新应用
//     *
//     * @param project
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = PUT, method = RequestMethod.PUT)
//    @ResponseBody
//    public JsonResponse update(@RequestBody Project project) {
//        try {
//            Project oldProject = service.get(project.getId());
//            if (StringUtil.isNull(project.getFileName())) {
//                project.setFileName(oldProject.getFileName());
//                project.setFilePath(oldProject.getFilePath());
//            }
//            int result = service.update(project);
//            if (result > -1) {
//                return new JsonResponse(project);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "更新失败");
//    }
//
//    /**
//     * 获取所有应用
//     *
//     * @return
//     */
//    @RequestMapping(value = GET_ALL, method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getAll() {
//        try {
//            List<Project> projects = service.findList();
//            return new JsonResponse(projects);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
//    }
//
//    /**
//     * 查询所有的提供者
//     *
//     * @return List
//     * @throws IOException
//     */
//    @RequestMapping(value = GET_ALL_PROVIDER, method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getALLProvider() {
//
//        try {
//            List<String> list = service.getAllProviders();
//            return new JsonResponse(list);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "查询失败");
//    }
//
//    /**
//     * 查询所有的应用信息
//     *
//     * @throws IOException
//     */
//    @RequestMapping(value = GET_EASYUI_QUERY, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public JSONObject queryOfEasyui(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "provider", required = false) String provider, @RequestParam(value = "userId", required = false) String userId) {
//        JSONObject jsonResult = new JSONObject();
//        List<JSONObject> rows = new ArrayList<JSONObject>();
//        int total = 0;
//
//        try {
//            List<Map<String, Object>> listAll = service.getListByParams(name, provider, userId);
//            for (int i = 0; listAll != null && i < listAll.size(); i++) {
//                JSONObject json = new JSONObject();
//                json.put("id", listAll.get(i).get("ID"));
//                json.put("name", listAll.get(i).get("NAME"));
//                json.put("ip", listAll.get(i).get("IP"));
//                json.put("port", listAll.get(i).get("PORT"));
//                json.put("state", listAll.get(i).get("STATE"));
//                json.put("description", listAll.get(i).get("DESCRIPTION"));
//                json.put("provider", listAll.get(i).get("PROVIDER"));
//                json.put("fileName", listAll.get(i).get("FILE_NAME"));
//                json.put("checkState", listAll.get(i).get("CHECK_STATE"));
//                json.put("testUrl", listAll.get(i).get("TEST_URL"));
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
//    /**
//     * 获取应用状态
//     *
//     * @throws IOException
//     */
//    @RequestMapping(value = GET_STATE, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public JsonResponse getState(@RequestParam(value = "ip", required = false) String ip, @RequestParam(value = "port", required = false) String port) {
//
//        boolean isReachable = false;
//        try {
//            isReachable = HttpUtil.isReachable(ip, port, 5000);
//            return new JsonResponse(isReachable);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "获取状态失败");
//    }
//
//    /**
//     * 获取应用统计数据
//     *
//     * @throws IOException
//     */
//
//    @RequestMapping(value = GET_PROVIDERS_STATISTICS, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public JsonResponse getProvidersStatistics(@RequestParam(value = "year", required = false) String year) {
//
//        JSONObject jsonResult = new JSONObject();
//        JSONObject chat1 = new JSONObject();
//        JSONObject chat2 = new JSONObject();
//        int[] data0 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        int[] data1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        List<JSONObject> data = new ArrayList<JSONObject>();
//        String month = "";
//        try {
//            for (int i = 1; i <= 12; i++) {
//                month = i < 10 ? "0" + i : "" + i;
//                data0[i - 1] = service.getProjectCountByMonth(year + "-" + month);
//                data1[i - 1] = service.getProviderCountByMonth(year + "-" + month);
//            }
//
//            List<String> providersList = service.getProvidersByYear(year);
//            for (int i = 0; providersList != null && i < providersList.size(); i++) {
//                String p = providersList.get(i);
//                JSONObject json = new JSONObject();
//                json.put("name", p);
//                json.put("y", service.getPercentOfProjectByYear(year, p));
//                data.add(json);
//            }
//            chat1.put("title", year + "年内应用个数");
//            chat1.put("data0", data0);
//            chat1.put("data1", data1);
//            chat2.put("title", year + "年提供应用比例");
//            chat2.put("data", data);
//
//            jsonResult.put("chat1", chat1);
//            jsonResult.put("chat2", chat2);
//
//            return new JsonResponse(jsonResult);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "获取状态失败");
//    }
//
//    /**
//     * 获取使用者统计
//     */
//    @RequestMapping(value = GET_USAGES_STATISTICS, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public JsonResponse getUsagesStatistics() {
//
//        JSONObject jsonResult = new JSONObject();
//        List<JSONObject> data = new ArrayList<JSONObject>();
//        try {
//            List<Project> projects = service.findList();
//            for (int i = 0; !StringUtil.isListEmpty(projects) && i < projects.size(); i++) {
//                Project project = projects.get(i);
//                JSONObject json = new JSONObject();
//                json.put("name", project.getName());
//                json.put("y", service.getPercentOfUsages(project.getId()));
//                data.add(json);
//            }
//            jsonResult.put("title", "使用者使用统计");
//            jsonResult.put("data", data);
//            return new JsonResponse(jsonResult);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "获取状态失败");
//    }
//
//    /**
//     * 下载
//     *
//     * @param id
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = DOWNLOAD_FILE, method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse download(@RequestParam(value = "id", required = false) String id, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            Project model = service.get(id);
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("multipart/form-data");
//            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(model.getFileName().getBytes("utf-8"), "ISO8859-1"));
//            InputStream inputStream = new FileInputStream(new File(model.getFilePath()));
//
//            OutputStream os = response.getOutputStream();
//            byte[] b = new byte[2048];
//            int length;
//            while ((length = inputStream.read(b)) > 0) {
//                os.write(b, 0, length);
//            }
//            os.flush();
//            os.close();
//            inputStream.close();
//            return new JsonResponse();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return new JsonResponse(JsonResponseStatusEnum.ERROR, e.getMessage());
//        }
//    }
//
//    /**
//     * 审核通过应用
//     *
//     * @param id
//     */
//    @RequestMapping(value = CHECK_PROJECT, method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse checkProject(@PathVariable String id) {
//        try {
//            Project model = service.get(id);
//            model.setCheckState("审核通过");
//            int count = service.updateProjectCheckState(model);
//            if (count > -1) {
//                return new JsonResponse(model);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "审核失败");
//    }
//
//    /**
//     * 查看应用信息
//     *
//     * @param id
//     */
//    @RequestMapping(value = GET_BY_ID, method = RequestMethod.GET)
//    @ResponseBody
//    public JsonResponse getById(@PathVariable String id) {
//        try {
//            Project model = service.get(id);
//            if (model != null) {
//                return new JsonResponse(model);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JsonResponse(JsonResponseStatusEnum.ERROR, "审核失败");
//    }
//
//    /**
//     * 获取所有应用并且返回json树
//     *
//     * @return
//     */
//    @RequestMapping(value = GET_PROJECT_TREE, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public JSONObject[] getProjectTree() {
//
//        JSONObject json = new JSONObject();
//        JSONArray children = new JSONArray();
//        try {
//            List<Project> projects = service.findList();
//            for (int i = 0; !StringUtil.isListEmpty(projects) && i < projects.size(); i++) {
//                JSONObject temp = new JSONObject();
//                temp.put("text", projects.get(i).getName());
//                children.add(temp);
//            }
//            json.put("text", "全部应用");
//            json.put("state", "open");
//            json.put("children", children);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return new JSONObject[]{json};
//    }
//}
