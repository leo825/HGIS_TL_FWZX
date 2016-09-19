import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.geobeans.fwzx.model.RoleModel;
import cn.geobeans.fwzx.model.UserModel;
import cn.geobeans.fwzx.service.UserService;

/**
 *@author liuxi 
 *@parameter E-mail:15895982509@163.com
 *@version 创建时间:2016-5-22上午10:39:38
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class UserServiceTest {

	@Resource
    private UserService service;
	
//	@Test
//	public void testInsert() throws Exception {
//
//		UserModel user = new UserModel();
//		user.setAccount("usager");
//		user.setPassword("usager");
//		user.setNickname("服务使用者者");
//		user.setAccountState("正常");
//		int result = service.insert(user);
//		System.out.println("");
//		System.out.println("增加结果是: "+result);
//
//	}
	
//	@Test
//	public void testUpdate() throws Exception {
//
//		UserModel user = new UserModel();
//		user.setId("5a082ef279f04b5496fd363df0b170b4");
//		user.setAccountState("正常");
//		int result = service.update(user);
//		System.out.println("");
//		System.out.println("修改的结果是: "+result);
//
//	}
	
	
//	@Test
//	public void testDelete() throws Exception {
//
//		int result = service.delete("5a082ef279f04b5496fd363df0b170b4");
//		System.out.println("");
//		System.out.println("修改的结果是: "+result);
//
//	}
	
	@Test
	public void testGetRoleListByUserId() throws Exception{
		
		String id = "bddacea3a5c2493f97fdcde00c616b5b";
		List <RoleModel> list = service.getRoleListByUserId(id);
		System.out.println(list.size());
	}
	

  

}
