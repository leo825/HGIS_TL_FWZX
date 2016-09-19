/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-4-26下午2:50:46
 */
import java.util.List;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cn.geobeans.fwzx.model.UsageModel;
import cn.geobeans.fwzx.service.UsageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class UsageServiceTest {

	@Resource
    private UsageService service;

//    @Test
//    public void testInsert() throws Exception {
//
//
//    	for(int i = 21; i < 25; i++){
//        	UsageModel usage = new UsageModel();
//        	usage.setIp("192.168.0.10"+i);
//        	usage.setName("海关2");
//        	usage.setDescription("海关项目");
//        	service.insert(usage);
//    	}
//    }
//    
    @Test
    public void testGet() throws Exception{
    	
    	UsageModel u = service.get("43cc98fda158461f82b1c848f4abf5ed");
    	System.out.println();
    	System.out.println("ip="+u.getIp()+",name="+u.getName()+",description="+u.getDescription()+",list="+u.getProjectList());
//    	for(ProjectModel p : u.getProjectList()){
//    		System.out.println("projectName="+p.getName());
//    	}
    }
    
    @Test
    public void testFindList() throws Exception{
    	int page = 1;
    	int rows = 10;
    	List <UsageModel> list = service.findList();
    	
    }
    
	
}