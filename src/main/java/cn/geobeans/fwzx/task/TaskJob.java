/***
 * *author Administrator
 * *date 2016/9/14
 ***/
package cn.geobeans.fwzx.task;

/**
 * Created by Administrator on 2016/9/14.
 */

import cn.geobeans.common.util.CalendarUtil;
import cn.geobeans.common.util.ProjectUtil;
import cn.geobeans.fwzx.model.OperationModel;
import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.service.OperationService;
import cn.geobeans.fwzx.service.ProjectService;
import cn.geobeans.fwzx.service.UsageService;
import cn.geobeans.fwzx.util.HttpUtil;
import cn.geobeans.fwzx.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component("taskJob")
public class TaskJob {

    private static Logger logger = Logger.getLogger(TaskJob.class);

    private static ConcurrentLinkedQueue<OperationModel> operationQueue = new ConcurrentLinkedQueue<OperationModel>();

    private static final String REPORTS_PATH = ProjectUtil.getProperty("file.reports");

    @Resource
    private ProjectService projectService;

    @Resource
    private OperationService operationService;

    @Resource
    private UsageService usageService;

    /**
     * 检测应用状态的任务
     * */
    @Scheduled(cron = "*/30 * * * * ?")
    public void chekckProjectStateTask() {
        try {
            List<ProjectModel> projectList = projectService.findList();
            for (int i = 0; !StringUtil.isListEmpty(projectList) && i < projectList.size(); i++) {
                boolean isReachable = HttpUtil.isReachable(projectList.get(i).getIp(), projectList.get(i).getPort(), 1000);
                boolean isTestUrlReachable = HttpUtil.isReacheable(projectList.get(i).getTestUrl(), 1000);
                if (isReachable && isTestUrlReachable) {
                    if (!StringUtil.isNull(projectList.get(i).getState()) && projectList.get(i).getState().equals("正常")) {
                        logger.info("应用: " + projectList.get(i).getName() + " 运行正常");
                    } else {
                        projectList.get(i).setState("正常");
                        projectService.updataProjectState(projectList.get(i));
                        logger.info("应用: " + projectList.get(i).getName() + " 运行正常");
                    }
                } else {
                    projectList.get(i).setState("断开");
                    projectService.update(projectList.get(i));
                    logger.info("应用: " + projectList.get(i).getName() + " 断开");
                }
            }
        } catch (Exception e) {
            logger.error("任务异常:" + e.getMessage());
        }
    }

    /**
     * 增加日志的任务
     * */
    @Scheduled(cron = "*/1 * * * * ?")
    public void addOperationLogTask() {
        if (!operationQueue.isEmpty()) {
            OperationModel operation = operationQueue.poll();
            operationService.insert(operation);
        }
    }

    /**
     * 添加日志
     */
    public void addOperationLog(String ip, String serverName, String projectName, String result, String description) {
        OperationModel operation = new OperationModel();
        UsageModel u = usageService.getByIp(ip);
        String userName = u != null ? u.getName() : "未知";
        operation.setIp(ip);
        operation.setUserName(userName);
        operation.setServerName(serverName);
        operation.setProjectName(projectName);
        operation.setResult(result);
        operation.setDescription(description);
        operationQueue.add(operation);
        if (result.equals("失败")) {
            addFailLogToReport(operation);
        }
    }

    /**
     * 把失败日志添加到报告文件中 2016年4月28号运行报告
     */
    public void addFailLogToReport(OperationModel operation) {
        String filePath = "";
        String date = CalendarUtil.getDateFormatYMDstr(new Date());
        filePath = REPORTS_PATH + File.separator + date + "运行报告.log";
        try {
            String str = logStringFormate(operation.getOperateTime(), operation.getResult(), operation.getIp(), operation.getUserName(), operation.getServerName(), operation.getProjectName());
            File dest = new File(filePath);
            if (!dest.exists()) {
                dest.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(dest, true));
            writer.write(str + System.getProperty("line.separator"));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static String logStringFormate(String operateTime, String result, String ip, String userName, String serverName, String projectName) {
        return "[" + operateTime + "]" + " " + "[" + result + "]" + "ip=" + ip + ",userName=" + userName + ",serverName=" + serverName + ",projectName=" + projectName;
    }
}