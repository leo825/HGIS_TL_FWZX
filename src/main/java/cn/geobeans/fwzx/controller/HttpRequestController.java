package cn.geobeans.fwzx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import cn.geobeans.fwzx.task.TaskJob;
import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.geobeans.common.util.ProjectUtil;
import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.RouteModel;
import cn.geobeans.fwzx.service.ProjectService;
import cn.geobeans.fwzx.service.RouteService;
import cn.geobeans.fwzx.util.HttpUtil;
import cn.geobeans.fwzx.util.StringUtil;
import cn.geobeans.fwzx.util.XmlJsonUtil;
import cn.geobeans.fwzx.init.InitApplicationMethod;

/**
 * @author liuxi E-mail:15895982509@163.com
 * @version 创建时间:2016-3-27下午5:00:01
 */

@Controller
@RequestMapping("/http")
public class HttpRequestController {
    private static Logger logger = Logger.getLogger(HttpRequestController.class);
    @Resource
    private RouteService routeService;
    @Resource
    private ProjectService projectService;

    @Resource
    private TaskJob taskJob;

    @Resource
    private InitApplicationMethod initApplicationMethod;



    /**
     * 初始化方法，启动进程,并且完成数据库表和数据的初始化
     */
    @PostConstruct
    public void init() {

        try {

            initApplicationMethod.initDataBase();//初始化数据库数据
            initApplicationMethod.initFileDirectory();//初始化文件目录
            initApplicationMethod.initRoutes();//初始化路由信

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 转发get请求
     */
    @RequestMapping(value = "/redirect", method = {RequestMethod.GET, RequestMethod.POST})
    public String redirectGet(HttpServletRequest request, HttpServletResponse response) {
        /**
         * 获取项目名称 progectName 获取接口名称 serviceName 获取参数类型 serviceParms http://210.72.239.121:8088/SSIL/department.do?userid=jiangsudiaodu&username=江苏调度
         * http://localhost:8080/HGIS_TL_FWZX/http/redirect?projectName=SSIL&serverName=department&userid=jiangsudiaodu&username=江苏调度
         * */
        String projectName = request.getParameter("projectName");// 获取服务的名称
        String serverName = request.getParameter("serverName");// 获取服务的接口
        String serverAddr = "";
        String remoteParams = "";
        JSONObject json = new JSONObject();

        try {
            if (!StringUtil.isNull(projectName) && !StringUtil.isNull(serverName)) {
                ProjectModel project = projectService.getProjectByName(projectName);

                if (project != null) {
                    RouteModel route = routeService.get(project.getId(), serverName);
                    if (route != null) {
                        serverAddr = route.getServerAddr();
                        String parms = request.getQueryString();// 获取请求的所有参数
                        int positon = StringUtil.getCharacterPosition(parms, 2, "&") + 1;// 是否有参数传递过来，并且计算参数的位置
                        if (positon != 0) {
                            remoteParams = parms.substring(positon, parms.length());// 获取需要转发的请求的参数
                        }
                        taskJob.addOperationLog(HttpUtil.getRemoteHost(request), serverName, projectName, "成功", "访问成功");// 写入日志
                        response.setHeader("Location", serverAddr + "?" + remoteParams);
                        if ("GET".equals(request.getMethod())) {
                            response.setStatus(302);
                            return null;
                        }
                        if ("POST".equals(request.getMethod())) {
                            response.setStatus(307);
                            return null;
                        }
                    }
                }
            }
            taskJob.addOperationLog(HttpUtil.getRemoteHost(request), serverName, projectName, "失败", "访问失败了");// 写入日志
            json.put("result", false);
            json.put("data", "projectName or serviceName is not exist");
            PrintWriter out = response.getWriter();
            out.write(json.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    /**
     * 数据格式的转换
     */
    @RequestMapping(value = "/transform", method = {RequestMethod.GET, RequestMethod.POST})
    public void transform(HttpServletRequest request, HttpServletResponse response) {
        String projectName = request.getParameter("projectName");// 获取服务的名称
        String serverName = request.getParameter("serverName");// 获取服务的接口
        String dataReturnType = request.getParameter("dataReturnType");// 获希望返回的数据类型
        String serverAddr = "";
        String remoteParams = "";
        try {
            if (!StringUtil.isNull(projectName) && !StringUtil.isNull(serverName)) {
                ProjectModel project = projectService.getProjectByName(projectName);
                RouteModel route = routeService.get(project.getId(), serverName);
                if (route != null) {
                    serverAddr = route.getServerAddr();
                    String parms = request.getQueryString();// 获取请求的所有参数
                    int positon = StringUtil.getCharacterPosition(parms, 3, "&") + 1;// 是否有参数传递过来，并且计算参数的位置
                    if (positon != 0) {
                        remoteParams = parms.substring(positon, parms.length());// 获取需要转发的请求的参数
                    }
                    if (route.getDataReturnType().equals(dataReturnType)) {
                        taskJob.addOperationLog(HttpUtil.getRemoteHost(request), serverName, projectName, "失败", "数据返回格式与之前相同");// 写入日志
                        response.sendRedirect(serverAddr + "?" + remoteParams);// 重定向到apage.jsp
                    } else {
                        if (dataReturnType.equals("json")) {
                            Document xml = HttpUtil.getDocByPost(serverAddr + "?" + remoteParams, "UTF-8");
                            String xmlStr = xml.asXML();
                            PrintWriter out = response.getWriter();
                            out.write(XmlJsonUtil.xml2Json(xmlStr));
                            out.flush();
                            out.close();
                        }
                        if (dataReturnType.equals("xml")) {
                            String json = HttpUtil.getStringByPost(serverAddr, remoteParams, "UTF-8");
                            PrintWriter out = response.getWriter();
                            out.write(XmlJsonUtil.json2Xml(json));
                            out.flush();
                            out.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }


}
