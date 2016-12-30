package cn.geobeans.fwzx.dao;

import cn.geobeans.fwzx.model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.inject.Inject;


/**
 * Created by LX on 2016/12/30.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:springContext-config.xml"})
public class TestProjectDao {

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private ProjectDao projectDao;

    @Before
    public void setUp() {
        //此时最好不要使用Spring的Transactional来管理，因为dbunit是通过jdbc来处理connection，再使用spring在一些编辑操作中会造成事务shisu
        Session s = sessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
        //SystemContext.setRealPath("D:\\teach_source\\class2\\j2EE\\dingan\\cms-da\\src\\main\\webapp");
    }

    @After
    public void tearDown() {
        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        Session s = holder.getSession();
        s.flush();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }

    @Test
    public void testAdd(){
        Project project = new Project("192.168.0.1","FWZX5",10,"测试应用","华迪公司","http://www.baidu.com");
        projectDao.add(project);
    }

    @Test
    public void testDelete(){
        projectDao.delete("33aeb2c986f64608aba05315e90bc196");
    }


}
