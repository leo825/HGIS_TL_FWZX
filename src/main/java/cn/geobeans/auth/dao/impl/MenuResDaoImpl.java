package cn.geobeans.auth.dao.impl;

import cn.geobeans.auth.dao.MenuResDao;
import cn.geobeans.auth.model.AuthFinalVal;
import cn.geobeans.auth.model.MenuResources;
import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.common.model.SysException;
import cn.geobeans.dto.LeftMenuDto;
import cn.geobeans.dto.TreeDto;
import cn.geobeans.kit.BasicSysKit;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository("menuResDao")
public class MenuResDaoImpl extends BaseDaoImpl<MenuResources> implements MenuResDao {
	private String getSelectHql() {
		//int id, String name, String sn, MenuPos menuPos,
		//String href, String icon, int orderNum, String psn, int display
		return "select new MenuResources(m.id,m.name,m.sn,m.menuPos,m.href,m.icon,m.orderNum,m.psn,m.display)";
	}
	@Override
	public void add(MenuResources mr, String psn) {
		MenuResources parent = null;
		if(!BasicSysKit.isEmpty(psn)) {
			parent = (MenuResources)this.loadBySn(psn, "MenuResources");
			if(parent==null) throw new SysException("菜单的父类不存在");
			mr.setParent(parent);
			mr.setPsn(psn);
		}
		MenuResources self = (MenuResources)this.loadBySn(mr.getSn(), "MenuResources");
		if(self==null) {
			//不存在
			self = mr;
		} else {
			//存在就进行修改
			self.setDisplay(mr.getDisplay());
			self.setHref(mr.getHref());
			self.setIcon(mr.getIcon());
			self.setMenuPos(mr.getMenuPos());
			self.setName(mr.getName());
			self.setOrderNum(mr.getOrderNum());
			self.setSn(mr.getSn());
			self.setParent(mr.getParent());
			self.setPsn(mr.getPsn());
		}
		super.getSession().saveOrUpdate(self);
	}

	@Override
	public List<MenuResources> listModelMenuByType(String psn, int pos) {
		String hql = getSelectHql()+" from MenuResources m where m.psn=? and m.menuPos=? and m.display=1 order by m.orderNum";
		return super.list(hql, psn,pos);
	}

	@Override
	public List<MenuResources> listTopMenu() {
		String hql = getSelectHql()+" from MenuResources m where m.menuPos=? and m.display=1 order by m.orderNum";
		return super.list(hql, AuthFinalVal.MENU_TOP_NAV);
	}

	@Override
	public List<LeftMenuDto> listLeftNav() {
		String hql = "select m from MenuResources m where m.menuPos=?";
		List<MenuResources> mrs = super.list(hql, AuthFinalVal.MENU_LEFT_NAV);
		List<LeftMenuDto> lmds = new ArrayList<LeftMenuDto>();
		LeftMenuDto lmd = null;
		//添加菜单的父节点
		for(MenuResources mr:mrs) {
			if(mr.getParent()!=null&&mr.getParent().getMenuPos()==AuthFinalVal.MENU_ROOT) {
				//菜单是父节点菜单
				lmd = new LeftMenuDto();
				lmd.setParent(mr);
				lmd.setChilds(new ArrayList<MenuResources>());
				lmds.add(lmd);
			}
		}
		
		//添加子节点菜单
		for(MenuResources mr:mrs) {
			MenuResources mp = mr.getParent();
			if(mp!=null&&mp.getMenuPos()==AuthFinalVal.MENU_LEFT_NAV) {
				LeftMenuDto tmp = new LeftMenuDto(); tmp.setParent(mp);
				if(lmds.contains(tmp)) {
					lmds.get(lmds.indexOf(tmp)).getChilds().add(mr);
				}
			}
		}
		
		//排序父节点菜单
		Collections.sort(lmds);
		
		//排序子菜单
		for(LeftMenuDto md:lmds) {
			Collections.sort(md.getChilds());
		}
		
		return lmds;
	}

	@Override
	public List<TreeDto> tree() {
		String sql = "select id,name,pid from t_menu_res where display=1 order by order_num";
		List<TreeDto> tds = super.listBySql(sql, TreeDto.class, false);
		return tds;
	}

	@Override
	public List<MenuResources> listByParent(String pid) {
		String hql = null;
		if(pid==null||"".equals(pid))
			hql = getSelectHql()+" from MenuResources m where m.parent is null and m.display=1 order by m.orderNum";
		else
			hql = getSelectHql()+" from MenuResources m where m.display=1 and m.parent.id="+pid+" order by m.orderNum";
		return super.list(hql);
	}
	@Override
	public MenuResources loadBySn(String sn) {
		return (MenuResources)super.loadBySn(sn, MenuResources.class.getName());
	}
}
