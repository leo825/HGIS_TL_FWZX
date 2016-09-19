import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.geobeans.fwzx.model.RoleModel;
import cn.geobeans.fwzx.service.RoleService;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-5-22下午4:52:31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class RoleServiceTest {

	@Resource
    private RoleService service;
	
	@Test
	public void testInsert() throws Exception {

		RoleModel role = new RoleModel();
		role.setName("usager");
		role.setDescription("服务使用者");
		int result = service.insert(role);
		System.out.println("");
		System.out.println("增加结果是: "+result);

	}
	
//	@Test
//	public void testUpdate() throws Exception {
//
//		RoleModel role = service.get("94ba1eb205124e0c993c895fdbaa2646");
//		role.setName("superAdministrator");
//		role.setDescription("超级管理员");
//		int result = service.update(role);
//		System.out.println("");
//		System.out.println("修改结果是: "+result);
//
//	}

}
