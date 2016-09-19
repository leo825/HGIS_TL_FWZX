import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.geobeans.fwzx.model.ResourceModel;
import cn.geobeans.fwzx.service.ResourceService;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-5-22下午9:22:09
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class ResourceServiceTest {

	@Resource
	private ResourceService service;
	
	@Test
	public void testInsert() throws Exception{
		ResourceModel model = new ResourceModel();
		model.setParentId("0");
		model.setName("check");
		model.setNickname("审核");
		model.setDescription("审核");
		int result = service.insert(model);
		System.out.println();
		System.out.println("增加结果："+result);
	}
}
