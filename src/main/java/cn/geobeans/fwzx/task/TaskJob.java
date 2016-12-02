/***
 * *author Administrator
 * *date 2016/9/14
 ***/
package cn.geobeans.fwzx.task;

/**
 * Created by Administrator on 2016/9/14.
 */
import cn.geobeans.fwzx.model.OperationModel;
import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.service.OperationService;
import cn.geobeans.fwzx.service.ProjectService;
import cn.geobeans.fwzx.service.UsageService;
import cn.geobeans.fwzx.util.HttpUtil;
import cn.geobeans.fwzx.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class TaskJob {

    private static Logger logger = Logger.getLogger(TaskJob.class);

    private static ConcurrentLinkedQueue<OperationModel> operationQueue = new ConcurrentLinkedQueue<OperationModel>();
    @Resource
    private ProjectService projectService;
    @Resource
    private OperationService operationService;
    @Resource
    private UsageService usageService;

    /**
     * 检测应用状态的任务
     * */
    public void checkProjectStateTask() {
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

}