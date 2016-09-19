/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-4-26下午2:50:46
 */
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.RouteModel;
import cn.geobeans.fwzx.service.ProjectService;
import cn.geobeans.fwzx.service.RouteService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class RouteServiceTest {

	@Resource
    private RouteService service;

//    @Test
//    public void testInsert() throws Exception {
//    	for(int i = 10; i < 15; i++){
//        	RouteModel model = new RouteModel();
//        	model.setServerName("department"+i);
//        	model.setProjectId("84aeb2585191430b995057fcde72b015");
//        	model.setServerAddr("http://210.72.239.121:8080/SSIL/department"+i);
//        	model.setDataReturnType("xml");
//        	model.setDescription("获取部门的服务");
//        	int result = service.insert(model);
//        	System.out.println("添加测试");//换行
//        	System.out.println("增加结果是:"+result);
//    	}
//
//    }
//    
//    @Test
//    public void testGet() throws Exception{
//    	
//    }
    
//    @Test
//    public void testDelete() throws Exception{
//    	System.out.println("删除测试");
//    	int result = service.delete("eb5adbeda16549fca3ee67397cc1c725");
//    	System.out.println("删除结果:"+result);
//    }
	
//	@Test
//	public void testGetListByNameOrProvider() throws Exception{
//		String name = "DocShare";
//		String provider = "中遥地网";
//		List<Map<String, Object>> list = service.getListByNameOrProvider(name, provider);
//		
//		for (Map<String, Object> map : list){
//			System.out.println("name="+map.get("name"));
//		}
//	}
	
}