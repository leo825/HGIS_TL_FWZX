package cn.geobeans.kit;

import cn.geobeans.common.model.SysException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicSysKit {
	/**
	 * 把(22)(33)(55)取出形成List
	 * @param str
	 * @return
	 */
	public static List<String> braceStr2List(String str) {
		Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
		Matcher m = pattern.matcher(str);
		List<String> list = new ArrayList<String>();
		while(m.find()) {
			list.add(m.group());
		}
		return list;
	}

	public static boolean isEmpty(Object obj) {
		if(obj==null) return true;
		if(obj instanceof String) {
			if("".equals(obj)) return true;
		}
		if(obj instanceof Collection<?>) {
			if(((Collection) obj).size()>0) return true;
		}
		return false;
	}

	public static <N extends Object>void mergeList(List<N> baseList,List<N> mergeList) {
		for(N o:mergeList) {
			if(!baseList.contains(o)) {
				baseList.add(o);
			}
		}
	}


	public static List<Class> listByClass(String p) {
		try {
			List<Class> clzs = new ArrayList<Class>();
			String packagePath = p.replace(".", "/");
			URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);
			File file = new File(url.getPath());
			if(!file.exists()) throw new SysException("初始化的包名路径不正确");
			File []fs = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(pathname.getName().endsWith(".class")) return true;
					return false;
				}
			});
			for(File f:fs) {
				String cname = p+"."+FilenameUtils.getBaseName(f.getName());
				Class clz = Class.forName(cname);
				clzs.add(clz);
			}
			return clzs;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static List<Class> listByClassAnnotation(String p,Class an) {
		try {
			List<Class> clzs = new ArrayList<Class>();
			String packagePath = p.replace(".", "/");
			URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);
			File file = new File(url.getPath());
			if(!file.exists()) throw new SysException("初始化的包名路径不正确");
			File []fs = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(pathname.getName().endsWith(".class")) return true;
					return false;
				}
			});
			for(File f:fs) {
				String cname = p+"."+FilenameUtils.getBaseName(f.getName());
				Class clz = Class.forName(cname);
				if(clz.isAnnotationPresent(an))
					clzs.add(clz);
			}
			return clzs;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
