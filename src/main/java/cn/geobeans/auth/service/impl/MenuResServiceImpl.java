package cn.geobeans.auth.service.impl;

import cn.geobeans.auth.annotation.ModelMenu;
import cn.geobeans.auth.annotation.NavMenu;
import cn.geobeans.auth.dao.MenuResDao;
import cn.geobeans.auth.model.AuthFinalVal;
import cn.geobeans.auth.model.MenuResources;
import cn.geobeans.auth.service.MenuResService;
import cn.geobeans.dto.LeftMenuDto;
import cn.geobeans.dto.TreeDto;
import cn.geobeans.kit.BasicSysKit;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service("menuResService")
public class MenuResServiceImpl implements MenuResService {

    @Inject
    private MenuResDao menuResDao;

    @Override
    public void add(MenuResources mr, String psn) {
        menuResDao.add(mr, psn);
    }

    @Override
    public List<MenuResources> listModelMenuByType(String psn, int pos) {
        return menuResDao.listModelMenuByType(psn, pos);
    }

    @Override
    public List<MenuResources> listTopMenu() {
        return menuResDao.listTopMenu();
    }

    @Override
    public List<LeftMenuDto> listLeftNav() {
        return menuResDao.listLeftNav();
    }

    @Override
    public List<TreeDto> tree() {
        return menuResDao.tree();
    }

    @Override
    public List<MenuResources> listByParent(String pid) {
        return menuResDao.listByParent(pid);
    }

    @Override
    public void add(MenuResources mr) {
        menuResDao.add(mr, mr.getPsn());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void initMenuResources(String[] packages) {
        List<Class> clzs = new ArrayList<Class>();
        List<Class> tmpClzs = null;
        for (String p : packages) {
            tmpClzs = BasicSysKit.listByClass(p);
            BasicSysKit.mergeList(clzs, tmpClzs);
        }
        for (Class c : clzs) {
            //添加顶部的导航菜单
            addTopNavMenu(c);
            if (c.isAnnotationPresent(NavMenu.class)) {
                //如果在类上面有NavMenu存在，就添加导航菜单和子菜单
                MenuResources mr = addLeftNavMenu(c);
                addModelMenu(mr, c);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addModelMenu(MenuResources pmr, Class c) {
        String path = null;
        if (c.isAnnotationPresent(RequestMapping.class)) {
            path = ((RequestMapping) c.getAnnotation(RequestMapping.class)).value()[0];
        }
        String cname = c.getSimpleName();
        Method[] ms = c.getDeclaredMethods();
        MenuResources mr = null;
        for (Method m : ms) {
            if (m.isAnnotationPresent(ModelMenu.class)) {
                String mname = m.getName();
                ModelMenu mm = m.getAnnotation(ModelMenu.class);
                RequestMapping rm = m.getAnnotation(RequestMapping.class);
                path += rm.value()[0];
                String sn = getMenuSn(cname, mname, mm.sn());
                mr = menuResDao.loadBySn(sn);
                if (mr == null) mr = new MenuResources();
                mr.setDisplay(mm.display());
                mr.setHref(path);
                mr.setIcon(mm.icon());
                mr.setMenuPos(mm.menuPos());
                mr.setName(getModelMenuName(mname, mm));
                mr.setOrderNum(mm.orderNum());
                mr.setSn(sn);
                this.add(mr, pmr.getSn());
            }
        }
    }

    private String getModelMenuName(String mname, ModelMenu mm) {
        String name = mm.name();
        if (!BasicSysKit.isEmpty(name)) return name;
        if (mname.startsWith("add")) return "添加";
        else if (mname.startsWith("update")) return "更新";
        else if (mname.startsWith("delete")) return "删除";
        else if (mname.startsWith("show")) return "查询";
        else if (mname.startsWith("list")) return "列表";
        return "";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private MenuResources addLeftNavMenu(Class c) {
        String path = null;
        if (c.isAnnotationPresent(RequestMapping.class)) {
            path = ((RequestMapping) c.getAnnotation(RequestMapping.class)).value()[0];
        }
        String cname = c.getSimpleName();
        NavMenu nm = (NavMenu) c.getAnnotation(NavMenu.class);
        String sn = getMenuSn(cname, null, nm.sn());
        MenuResources mr = menuResDao.loadBySn(sn);
        if (mr == null) mr = new MenuResources();
        mr.setDisplay(nm.display());
        mr.setHref(path + nm.href());
        mr.setIcon(nm.icon());
        mr.setMenuPos(nm.menuPos());
        mr.setName(nm.name());
        mr.setOrderNum(nm.orderNum());
        mr.setSn(sn);
        this.add(mr, nm.psn());
        return mr;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addTopNavMenu(Class c) {
        String path = null;
        if (c.isAnnotationPresent(RequestMapping.class)) {
            path = ((RequestMapping) c.getAnnotation(RequestMapping.class)).value()[0];
        }
        Method[] ms = c.getDeclaredMethods();
        String cname = c.getSimpleName();
        String sn = null;
        MenuResources mr = null;
        for (Method m : ms) {
            String mname = m.getName();
            if (m.isAnnotationPresent(NavMenu.class)) {
                path += ((RequestMapping) m.getAnnotation(RequestMapping.class)).value()[0];
                NavMenu nm = m.getAnnotation(NavMenu.class);
                sn = getMenuSn(cname, mname, nm.sn());
                mr = menuResDao.loadBySn(sn);
                if (mr == null) {
                    mr = new MenuResources();
                }
                mr.setDisplay(nm.display());
                mr.setHref(path);
                mr.setIcon(nm.icon());
                mr.setMenuPos(AuthFinalVal.MENU_TOP_NAV);
                mr.setName(nm.name());
                mr.setOrderNum(nm.orderNum());
                mr.setSn(sn);
                this.add(mr, nm.psn());
            }
        }
    }

    private String getMenuSn(String cname, String mname, String asn) {
        String sn = null;
        if (BasicSysKit.isEmpty(asn)) {
            if (BasicSysKit.isEmpty(mname)) sn = cname;
            else sn = cname + "." + mname;
        } else sn = asn;
        return sn;
    }

}
