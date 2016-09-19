/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-4-26下午2:50:46
 */
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.service.ProjectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:WEB-INF/spring-servlet.xml"})
public class ProjectServiceTest {

	@Resource
    private ProjectService service;

//    @Test
//    public void testInsert() throws Exception {
//    	
//    	for(int i = 32; i < 35;i++){
//        	ProjectModel model = new ProjectModel();
//        	model.setIp("210.72.239.12"+i);
//        	model.setName("DocShare"+i);
//        	model.setPort("8077");
//        	model.setState("正常");
//        	model.setDescription("目录管理服务"+i);
//        	model.setProvider("company5");
//        	int result = service.insert(model);
//        	System.out.println("添加测试");//换行
//        	System.out.println("增加结果是:"+result);
//    	}
//    	
//
//    }
	
	@Test
	public void testGet() throws Exception{
		
		ProjectModel p = service.get("2fdbbff827bd4038bc3431769c15591c");
		System.out.println();
		System.out.println("["+p.getRegTime()+"]");
	}
    

    
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
//	@Test
//	public void testGetProjectCountByMonth(){
//		
//		int[] data0={0,0,0,0,0,0,0,0,0,0,0,0};
//		String year = "2016";
//		String month = "";
//		
//		for(int i=1;i<=12;i++){
//			month = i < 10 ? "0"+i : ""+i; 
//			data0[i-1] = service.getProjectCountByMonth(year+"-"+month);
//		}
//		
//		for(int j = 0; j < data0.length;j++){
//			System.out.println("data["+j+"]="+data0[j]);
//		};
//	}
	
//	@Test
//	public void testGetPercentOfProjectByYear(){
//		String year = "2016";
//		String provider = "company5";
//		double f = service.getPercentOfProjectByYear(year, provider);
//		System.out.println("company5="+f);
//	}
//	@Test
//	public void testGetProvidersByYear(){
//		String year = "2016";
//		List<String> list = null;
//		list = service.getProvidersByYear(year);
//		for(String s : list){
//			System.out.println(s);
//		}
//	}
	
	
	
	@Test
	public void testGetPercentOfUsages(){
		//DocShare14
		String projectName = "DocShare14";
		float f = service.getPercentOfUsages(projectName);
		System.out.println();
		System.out.println(f);
	}
}