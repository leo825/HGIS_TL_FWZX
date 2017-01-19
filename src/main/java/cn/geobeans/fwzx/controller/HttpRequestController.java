//package cn.geobeans.fwzx.controller;
//
//import cn.geobeans.fwzx.init.InitApplicationMethod;
//import cn.geobeans.fwzx.service.ProjectService;
//import cn.geobeans.fwzx.service.RouteService;
//import cn.geobeans.fwzx.task.TaskJob;
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//
///**
//* @author liuxi E-mail:15895982509@163.com
//* @version 创建时间:2016-3-27下午5:00:01
//*/
//
//@Controller
//@RequestMapping("/http")
//public class HttpRequestController {
//    private static Logger logger = Logger.getLogger(HttpRequestController.class);
//    @Resource
//    private RouteService routeService;
//    @Resource
//    private ProjectService projectService;
//    @Resource
//    private TaskJob taskJob;
//    @Resource
//    private InitApplicationMethod initApplicationMethod;
//
//
//    /**
//     * 初始化方法，启动进程,并且完成数据库表和数据的初始化
//     */
//    @PostConstruct
//    public void init() {
//        try {
//            initApplicationMethod.initDataBase();//初始化数据库数据
//            initApplicationMethod.initFileDirectory();//初始化文件目录
////          initApplicationMethod.initRoutes();//初始化路由信
//            initApplicationMethod.initServletRoutes();//路由所有接口
//
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }
//}
