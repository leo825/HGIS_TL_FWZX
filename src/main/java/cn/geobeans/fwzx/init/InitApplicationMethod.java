/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/9/22
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
package cn.geobeans.fwzx.init;

import cn.geobeans.common.util.ProjectUtil;
import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.RouteModel;
import cn.geobeans.fwzx.service.ProjectService;
import cn.geobeans.fwzx.service.RouteService;
import cn.geobeans.fwzx.util.HttpUtil;
import cn.geobeans.fwzx.util.StringUtil;
import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMessage;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.PolicyNode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.apache.camel.builder.Builder.constant;

/**
 * Created by Administrator on 2016/9/22.
 */

@Component
public class InitApplicationMethod {

    private static Logger logger = Logger.getLogger(InitApplicationMethod.class);

    private static final String INIT_DATA = ProjectUtil.getProperty("initData");//是否初始化数据

    private static final String REPORTS_PATH = ProjectUtil.getProperty("file.reports");//报告上传目录
    private static final String FILE_PATH = ProjectUtil.getProperty("file.upload");//文件上传目录

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

    public void buildRoute(ProjectModel project) {
        try {
            for (int i = 1; i <= MAX_PATH_LEVEL; i++) {
                final int contextPage = i;
                RouteBuilder route = new RouteBuilder() {
                    public void configure() throws Exception {
                        String jettyURL = "http://" + JETTY_HOST + ":" + JETTY_PORT + "/" + project.getName();
                        for (int j = 1; j <= contextPage; j++) {//匹配多层路径
                            jettyURL += "/{}";
                        }
                        System.out.println(jettyURL);
                        from("jetty:" + jettyURL)
                                .process(new Processor() {
                                    @Override
                                    public void process(Exchange exchange) {

                                        try {
                                            HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
                                            String realRequestUrI = request.getRequestURI();
                                            if ("GET".equals(request.getMethod())) {
                                                String requestUrl = "http://" + project.getIp() + ":" + project.getPort() + realRequestUrI + "?" + request.getQueryString();
                                                if ("PGIS_S_TileMapServer".equals(project.getName())) {
                                                    exchange.getOut().setBody("<html><body><img src='" + requestUrl + "'></body></html>");
                                                } else {
                                                    exchange.getOut().setBody(HttpUtil.getStringByGet(requestUrl, "UTF-8"));
                                                }
                                            } else {
                                                String params = exchange.getIn().getBody(String.class);
                                                String requestUrl = "http://" + project.getIp() + ":" + project.getPort() + realRequestUrI;
                                                exchange.getOut().setBody(HttpUtil.getStringByPost(requestUrl, params, "UTF-8"));
                                            }
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
        if (StringUtil.checkDir(REPORTS_PATH) && StringUtil.checkDir(FILE_PATH)) {
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
     * 初始化接口路由
     */
    public void initServletRoutes() {
        try {
            List<RouteModel> routesList = routeService.findList();
            if (!StringUtil.isListEmpty(routesList)) {
                for (RouteModel tempRoute : routesList) {
                    RouteBuilder route = new RouteBuilder() {
                        public void configure() throws Exception {
                            from("servlet:///" + tempRoute.getServerName())
                                    .process(new ProcessBegin())
                                    .to(tempRoute.getServerAddr())
                                    .process(new ProcessEnd());
                        }
                    };
                    camelContext.addRoutes(route);
                }
            } else {
                logger.info("路由路径为空。");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }


    private class ProcessBegin implements Processor {
        @Override
        public void process(Exchange exchange) {
            String params = null;
            try {
                HttpServletRequest request = exchange.getIn(HttpMessage.class).getRequest();
                if ("GET".equals(request.getMethod())) {
                    params = request.getQueryString();
                    exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant(params));
                } else {
                    params = exchange.getIn().getBody(String.class);
                    exchange.getOut().setHeader(Exchange.HTTP_METHOD, constant("POST"));
                    exchange.getOut().setHeader(Exchange.HTTP_QUERY, constant(params));
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    private class ProcessEnd implements Processor {
        @Override
        public void process(Exchange exchange) {
            String data = null;
            try {
                InputStream inputStream = (InputStream) exchange.getIn().getBody();
                data = IOUtils.toString(inputStream);
                exchange.getOut().setBody(data);
//                byte[] b = IOUtils.toByteArray(inputStream);

            } catch (Exception e) {
                logger.error(e);
            }
        }
    }
}
