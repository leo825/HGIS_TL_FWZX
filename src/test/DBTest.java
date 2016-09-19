/***
 * *author Administrator
 * *date 2016/8/30
 ***/

import cn.geobeans.common.util.ProjectUtil;
import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by Administrator on 2016/8/30.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class DBTest {

    private static Logger logger = Logger.getLogger(DBTest.class);

    @Resource
    private DataSource dataSource;

    @Test
    public void InitDBUtilTest() {
        try {
            Connection conn =  dataSource.getConnection();
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            runner.runScript(Resources.getResourceAsReader("table.sql"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    private class ApplicationContextHolder {
    }
}
