package cn.geobeans.fwzx.init;

import cn.geobeans.common.enums.OperateDescriptionEnum;
import cn.geobeans.common.enums.OperateResultEnum;
import cn.geobeans.common.util.CalendarUtil;
import cn.geobeans.common.util.ProjectUtil;
import cn.geobeans.fwzx.model.OperationModel;
import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.RouteModel;
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.service.*;
import cn.geobeans.fwzx.util.HttpUtil;
import cn.geobeans.fwzx.util.StringUtil;
import cn.geobeans.fwzx.util.XmlJsonUtil;
import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import net.sf.json.JSONObject;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMessage;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.apache.camel.builder.Builder.constant;

/**
 * Created by Administrator on 2016/9/22.
 */

@Component
public class InitApplicationMethod {

    private static Logger logger = Logger.getLogger(InitApplicationMethod.class);
    private static final String INIT_DATA = ProjectUtil.getProperty("initData");//是否初始化数据
    public static final String REPORTS_PATH = ProjectUtil.getProperty("file.reports");//报告上传目录
    public static final String FILE_PATH = ProjectUtil.getProperty("file.upload");//文件上传目录
    public static final String EXCEL_PATH = ProjectUtil.getProperty("file.excel");//excel上传目录

    private static final String JETTY_HOST = ProjectUtil.getProperty("jetty.host");//jetty的host
    private static final String JETTY_PORT = ProjectUtil.getProperty("jetty.port");//jetty的端口
    private static final int MAX_PATH_LEVEL = Integer.parseInt(ProjectUtil.getProperty("max_path_level"));//获取web应用中最深路径

    @Resource
    private DataSource dataSource;
    @Resource
    private CamelContext camelContext;
    @Resource
    private ProjectService projectService;
    @Resource
    private RouteService routeService;
    @Resource
    private OperationService operationService;
    @Resource
    private UsageService usageService;
    @Resource
    private UsageProjectService usageProjectService;


    public void buildRoute(final ProjectModel project) {
        try {
            for (int i = 1; i <= MAX_PATH_LEVEL; i++) {
                final int contextPage = i;
                RouteBuilder route = new RouteBuilder() {
                    public void configure() throws Exception {
                        String jettyURL = "http://" + JETTY_HOST + ":" + JETTY_PORT + "/" + project.getName();
                        for (int j = 1; j <= contextPage; j++) {//匹配多层路径
                            jettyURL += "/{}";
                        }
                        from("jetty:" + jettyURL)
                                .process(new Processor() {
                                    @Override
                                    public void process(Exchange exchange) {
                                        InputStream inputStream = null;
                                        try {
                                            HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
                                            String realRequestUrI = request.getRequestURI();
                                            if ("GET".equals(request.getMethod())) {
                                                String requestUrl = "http://" + project.getIp() + ":" + project.getPort() + realRequestUrI + "?" + request.getQueryString();
                                                inputStream = HttpUtil.getInputStreamsByGet(requestUrl);
                                            } else {
                                                String params = exchange.getIn().getBody(String.class);
                                                String requestUrl = "http://" + project.getIp() + ":" + project.getPort() + realRequestUrI;
                                                inputStream = HttpUtil.getInputStreamByPost(requestUrl, params);
                                            }
                                            exchange.getOut().setBody(IOUtils.toByteArray(inputStream));
                                        } catch (Exception e) {
                                            logger.error(e);
                                        }
                                    }
                                });
                    }
                };
                camelContext.addRoutes(route);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * 初始化数据库数据
     */
    public void initDataBase() {
        Connection conn = null;
        try {
            if ("yes".equals(INIT_DATA)) {
                conn = dataSource.getConnection();
                ScriptRunner runner = new ScriptRunner(conn, false, false);//初始化数据库表和数据
                runner.runScript(Resources.getResourceAsReader("table.sql"));
                logger.info("初始化数据库表和数据完成.");
            }
        } catch (SQLException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    /**
     * 初始化文件目录
     */
    public void initFileDirectory() {
        if (StringUtil.checkDir(REPORTS_PATH) && StringUtil.checkDir(FILE_PATH) && StringUtil.checkDir(EXCEL_PATH)) {
            logger.info("初始化上传目录成功.");
        } else {
            logger.error("初始化上传目录失败!!!");
        }
    }

    /**
     * 初始化路由
     */
    public void initRoutes() {
        List<ProjectModel> projectList = projectService.findList();
        try {
            if (!StringUtil.isListEmpty(projectList)) {
                for (ProjectModel project : projectList) {
                    buildRoute(project);
                }
            } else {
                logger.info("服务为空，没有路由的对象");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }


    /**
     * 动态增加一个servlet路由
     */
    public int addServletRoute(final RouteModel routeModel) {
        int result = -1;
        try {
            final ProjectModel tempProject = projectService.get(routeModel.getProjectId());
            RouteBuilder route = new RouteBuilder() {
                public void configure() {
                    //处理访问过程中出现的访问超时异常
                    onException(Exception.class)
                            .handled(true).transform()
                            .simple("${exception.message},")
                            .process(new ProcessError());

                    from("servlet:///" + tempProject.getName() + "/" + routeModel.getServerName())
                            .process(new ProcessBegin(routeModel, tempProject))
                            .choice()
                            .when(header("rightful").isEqualTo(false)).process(new ProcessLegal())
                            .otherwise()
                            .to(routeModel.getServerAddr() + "?throwExceptionOnFailure=false")
                            .process(new ProcessEnd());//throwExceptionOnFailure=false，如果后表面不加上这个条件则会抛出异常导致下面无法执行，无法捕捉404异常
                }
            };
            camelContext.addRoutes(route);
            result = 1;
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    /**
     * 根据Endpoint找到相应的route，先停止再删除
     */
    public int deleteServletRoute(RouteModel routeModel) {
        int result = -1;
        ProjectModel project = projectService.get(routeModel.getProjectId());
        try {
            String endPoint = "Endpoint[servlet:///" + project.getName() + "/" + routeModel.getServerName() + "]";
            List<Route> routeList = camelContext.getRoutes();
            for (Route route : routeList) {
                if (endPoint.equals(route.getEndpoint().toString())) {
                    camelContext.stopRoute(route.getId());
                    camelContext.removeRoute(route.getId());
                    result = 1;
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    /**
     * 更新一个路由设置
     */
    public int updateServletRoute(RouteModel oldRoute, RouteModel newRoute) {
        int result = -1;
        try {
            deleteServletRoute(oldRoute);
            addServletRoute(newRoute);
            result = 1;
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }


    /**
     * 初始化接口路由
     */
    public void initServletRoutes() {
        try {
            List<RouteModel> routesList = routeService.findList();
            if (!StringUtil.isListEmpty(routesList)) {
                for (RouteModel tempRoute : routesList) {
                    addServletRoute(tempRoute);
                }
            } else {
                logger.info("路由路径为空。");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * 开始的processor处理
     */
    private class ProcessBegin implements Processor {

        private RouteModel route;
        private ProjectModel project;

        public ProcessBegin() {
        }

        public ProcessBegin(RouteModel route, ProjectModel project) {
            this.route = route;
            this.project = project;
        }

        @Override
        public void process(Exchange exchange) {
            String params = null;
            try {
                HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
                String ip = HttpUtil.getRemoteHost(request);
                UsageModel u = usageService.getByIp(ip);

                exchange.getOut().setHeader("ip", ip);
                exchange.getOut().setHeader("serverName", route.getServerName());
                exchange.getOut().setHeader("projectName", project.getName());
                exchange.getOut().setHeader("dataReturnType", route.getDataReturnType());
                exchange.getOut().setHeader("dataTransformType", request.getParameter("dataTransformType"));//返回数据类型
                //此时ip不存在系统中因此ip不合法
                if (u == null) {
                    exchange.getOut().setHeader("rightful", false);
                    exchange.getOut().setHeader("userName", "未知");
                    exchange.getOut().setHeader("operateResult", OperateResultEnum.FAILD.toString());
                    exchange.getOut().setHeader("operateDescription", OperateDescriptionEnum.IP_NOT_ALLOW.toString());
                } else if (!usageProjectService.isUsageProjectExist(u.getId(), project.getId())) {
                    exchange.getOut().setHeader("rightful", false);
                    exchange.getOut().setHeader("userName", u.getName());
                    exchange.getOut().setHeader("operateResult", OperateResultEnum.FAILD.toString());
                    exchange.getOut().setHeader("operateDescription", OperateDescriptionEnum.PERMISSION_DENIED.toString());
                } else {
                    if ("GET".equals(request.getMethod())) {
                        params = request.getQueryString();
                        exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant(params));
                    } else {
                        params = exchange.getIn().getBody(String.class);
                        exchange.getOut().setHeader(Exchange.HTTP_METHOD, constant("POST"));
                        exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant(params));
                    }
                    exchange.getOut().setHeader("rightful", true);
                    exchange.getOut().setHeader("userName", u.getName());
                }
            } catch (Exception e) {
                logger.error(e);

            }
        }
    }


    /**
     * 处理访问过程中异常处理的processer
     */
    private class ProcessError implements Processor {

        @Override
        public void process(Exchange exchange) {
            JSONObject json = new JSONObject();
            String ip = (String) exchange.getIn().getHeader("ip");
            String serverName = (String) exchange.getIn().getHeader("serverName");
            String projectName = (String) exchange.getIn().getHeader("projectName");
            String operateResult = OperateResultEnum.FAILD.toString();
            String userName = (String) exchange.getIn().getHeader("userName");
            String operateDescription = (String) exchange.getIn().getBody() + OperateDescriptionEnum.REQUEST_UNREACHABLE.toString();
            try {
                json.put("result", operateResult);
                json.put("data", operateDescription);
                exchange.getOut().setBody(json);
                addOperationLog(ip, serverName, projectName, operateResult, userName, operateDescription);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    /**
     * 非法访问的processor处理
     */
    private class ProcessLegal implements Processor {
        @Override
        public void process(Exchange exchange) {
            JSONObject json = new JSONObject();
            String ip = (String) exchange.getIn().getHeader("ip");
            String serverName = (String) exchange.getIn().getHeader("serverName");
            String projectName = (String) exchange.getIn().getHeader("projectName");
            String operateResult = (String) exchange.getIn().getHeader("operateResult");
            String userName = (String) exchange.getIn().getHeader("userName");
            String operateDescription = (String) exchange.getIn().getHeader("operateDescription");

            try {
                json.put("result", operateResult);
                json.put("data", operateDescription);
                exchange.getOut().setBody(json);
                addOperationLog(ip, serverName, projectName, operateResult, userName, operateDescription);
            } catch (Exception e) {
                logger.error(e);

            }
        }
    }

    /**
     * 结束的processor处理
     */
    private class ProcessEnd implements Processor {
        @Override
        public void process(Exchange exchange) {
            try {
                String ip = (String) exchange.getIn().getHeader("ip");
                String serverName = (String) exchange.getIn().getHeader("serverName");
                String projectName = (String) exchange.getIn().getHeader("projectName");
                String dataReturnType = (String) exchange.getIn().getHeader("dataReturnType");
                String operateResult = OperateResultEnum.SUCCESS.toString();
                String userName = (String) exchange.getIn().getHeader("userName");
                String operateDescription = OperateDescriptionEnum.OPERATE_SUCCESS.toString();
                String dataTransformType = (String) exchange.getIn().getHeader("dataTransformType");

                int responseCode = (int) exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);//是否200返回
                JSONObject json = new JSONObject();

                if (responseCode == 200) {
                    InputStream inputStream = (InputStream) exchange.getIn().getBody();
                    if ("xml".equals(dataTransformType) && "json".equals(dataReturnType)) {
                        String jsonStr = IOUtils.toString(inputStream);
                        String xmlStr = XmlJsonUtil.json2Xml("{\"document\":" + jsonStr + "}");
                        exchange.getOut().setBody(xmlStr);
                    } else if ("json".equals(dataTransformType) && "xml".equals(dataReturnType)) {
                        String xmlStr = IOUtils.toString(inputStream);
                        String jsonStr = XmlJsonUtil.xml2Json(xmlStr);
                        exchange.getOut().setBody(jsonStr);
                    } else {
                        byte[] bytes = IOUtils.toByteArray(inputStream);
//                      注释掉的部分会报空指针异常,原因是将转换成response的时候转换会抛出异常
//                      HttpServletResponse response = exchange.getOut(HttpMessage.class).getResponse();
//                      response.getOutputStream().write(bytes);
//                      response.getOutputStream().close();
                        exchange.getOut().setBody(bytes);
                    }
                    addOperationLog(ip, serverName, projectName, operateResult, userName, operateDescription);
                } else {
                    json.put("result", false);
                    json.put("data", responseCode);
                    exchange.getOut().setBody(json);
                    addOperationLog(ip, serverName, projectName, OperateResultEnum.FAILD.toString(), userName, String.valueOf(responseCode));
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    /**
     * 添加日志
     */
    public void addOperationLog(String ip, String serverName, String projectName, String result, String userName, String description) {
        OperationModel operation = new OperationModel(ip, serverName, projectName, result, userName, description);
        operationService.insert(operation);
        if (OperateResultEnum.FAILD.toString().equals(result)) {
            addFailLogToReport(operation);
        }
    }

    /**
     * 把失败日志添加到报告文件中 2016年4月28号运行报告
     */
    public void addFailLogToReport(OperationModel operation) {
        String filePath = "";
        String date = CalendarUtil.getDateFormatYMDstr(new Date());
        filePath = InitApplicationMethod.REPORTS_PATH + File.separator + date + "运行报告.log";
        try {
            String str = logStringFormate(operation.getOperateTime(), operation.getResult(), operation.getIp(), operation.getUserName(), operation.getServerName(), operation.getProjectName(), operation.getDescription());
            File dest = new File(filePath);
            if (!dest.exists()) {
                dest.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(dest, true));
            writer.write(str + System.getProperty("line.separator"));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public static String logStringFormate(String operateTime, String result, String ip, String userName, String serverName, String projectName, String description) {
        return "[" + operateTime + "]" + " " + "[" + result + "]" + "ip=" + ip + ",用户=" + userName + ",接口=" + serverName + ",应用服务=" + projectName + ",异常描述=" + description;
    }

}
