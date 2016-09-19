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

import cn.geobeans.fwzx.model.OperationModel;
import cn.geobeans.fwzx.service.OperationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class OperationServiceTest {

	@Resource
    private OperationService service;

    @Test
	public void testInsert() throws Exception {
    	System.out.println();
		for (int i = 0; i < 5; i++) {
			OperationModel usage = new OperationModel();
			usage.setIp("192.168.0.110");
			usage.setServerName("department0");
			usage.setProjectName("DocShare15");
			usage.setResult("成功");
			int result = service.insert(usage);
			if(result > -1){
				System.out.println("插入成功");
			}
		}

	}
    
    @Test
    public void testGet() throws Exception{
    	
    }
    
	
}