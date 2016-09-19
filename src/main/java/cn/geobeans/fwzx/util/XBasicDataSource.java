/***
 * *author Administrator
 * *date 2016/9/1
 ***/
package cn.geobeans.fwzx.util;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/9/1.
 */
public class XBasicDataSource extends BasicDataSource {
    @Override
    public synchronized void close() throws SQLException {
        System.out.println("......输出数据源Driver的url：" + DriverManager.getDriver(url));
        DriverManager.deregisterDriver(DriverManager.getDriver(url));
        super.close();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
