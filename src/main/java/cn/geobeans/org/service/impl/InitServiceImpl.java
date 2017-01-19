package cn.geobeans.org.service.impl;

import cn.geobeans.common.model.SysException;
import cn.geobeans.org.service.InitService;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Service("initService")
public class InitServiceImpl extends AbstractBaseServiceImpl implements InitService {
    @Inject
    private BeanFactory factory;

    private Element readRootElement(String filename) {
        try {
            SAXReader reader = new SAXReader();
//			System.out.println(InitService.class.getClassLoader().getResource("init/orgs.xml"));
            Document d = reader.read(InitServiceImpl.class.getClassLoader().getResourceAsStream("init/" + filename));
            return d.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initEntityByXml(String filename) {
        Element root = readRootElement(filename);
        String pname = root.attributeValue("package");
        List<Element> initEntitys = root.selectNodes("/entitys/initEntity");
        for (Element e : initEntitys) {
            //如果这个实体存在就不添加
            if (e.attributeValue("exist").equals("1")) {
                continue;
            }
            String ipname = e.attributeValue("package");
            String cname = e.attributeValue("class");
            if (ipname != null && !"".equals(ipname)) {
                cname = ipname + "." + cname;
            } else {
                cname = pname + "." + cname;
            }
            String method = e.attributeValue("method");
            List<Element> entitys = (List<Element>) e.selectNodes("entity");
            addElements(cname, method, entitys);
        }
    }

    private void addElements(String cname, String method, List<Element> entitys) {
        for (Element e : entitys) {
            addElement(cname, method, e, null);
        }
    }

    private void addElement(String cname, String method, Element e, Object parent) {
        try {
            //获取所有的属性
            List<Attribute> atts = (List<Attribute>) e.attributes();
            Object obj = Class.forName(cname).newInstance();
            String[] ms = method.split("\\.");
            if (ms.length != 2) throw new SysException("方法格式不正确");
            String sname = ms[0];
            String mname = ms[1];
            for (Attribute att : atts) {
                String name = att.getName();
                String value = att.getValue();
                BeanUtils.copyProperty(obj, name, value);
            }
            if (parent != null) {
                BeanUtils.copyProperty(obj, "parent", parent);
            }
            Object service = factory.getBean(sname);
            Method m = service.getClass().getMethod(mname, obj.getClass());
            m.invoke(service, obj);
            List<Element> es = e.selectNodes("entity");
            for (Element ele : es) {
                addElement(cname, method, ele, obj);
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        }
    }

}
