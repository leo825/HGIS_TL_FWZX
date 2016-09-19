import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.geobeans.fwzx.model.ProjectModel;
import cn.geobeans.fwzx.model.RouteModel;
import cn.geobeans.fwzx.util.HttpUtil;
import cn.geobeans.fwzx.util.StringUtil;

/**
 * @author liuxi
 * @parameter E-mail:15895982509@163.com
 * @version 创建时间:2016-6-14下午5:13:10
 */
@Controller
@RequestMapping("/test")
public class TestController {

	/**
	 * 转发get请求
	 * @throws IOException 
	 * */
	@RequestMapping(value = "/get", method = { RequestMethod.GET })
	public void testGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String url = "http://192.168.0.101/PGIS_S_TileMapServer/Maps/vec_tj/EzMap?Service=getImage&Type=RGB&V=0.3&Col=935&Row=314&Zoom=12";
		String encoding = "UTF-8";
		
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setDoOutput(true);
		StringBuffer data = new StringBuffer();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		char[] cbuf = new char[1024];
		response.setContentType("image/png;charset=base64");
		PrintWriter out = response.getWriter();
		int rc = 0;
		while ( (rc = rd.read(cbuf, 0, 1024)) > 0) {
			out.write(cbuf);
			out.flush();
			out.close();
		}
		rd.close();

	}
}
