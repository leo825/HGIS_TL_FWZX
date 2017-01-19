package cn.geobeans.auth.dao.impl;

import cn.geobeans.auth.dao.ControllerResDao;
import cn.geobeans.auth.model.ControllerOper;
import cn.geobeans.auth.model.ControllerResources;
import cn.geobeans.common.dao.impl.BaseDaoImpl;
import cn.geobeans.common.model.SysException;
import cn.geobeans.dto.TreeDto;
import cn.geobeans.kit.BasicSysKit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("controllerResDao")
public class ControllerResDaoImpl extends BaseDaoImpl<ControllerResources> implements ControllerResDao {
	
	private String getSelectHql() {
//		int id, String name, String sn, String psn,
//		String className, int orderNum
		String hql = "select new ControllerResources(cr.id,cr.name,cr.sn,cr.psn,cr.className,cr.orderNum)";
		return hql;
	}

	@Override
	public void addResources(ControllerResources cr, String psn) {
		ControllerResources p = null;
		if(!BasicSysKit.isEmpty(psn)) {
			p = (ControllerResources)super.loadBySn(psn, "ControllerResources");
			if(p==null) throw new SysException("Controller资源对象的父节点不存在！");
			cr.setPsn(psn);
			cr.setParent(p);
		}
		ControllerResources self = (ControllerResources)super.loadBySn(cr.getSn(), "ControllerResources");
		if(self==null) {
			if(cr.getOrderNum()<=0) {
				if(p==null)
					cr.setOrderNum(super.getMaxOrder(null, ControllerResources.class.getSimpleName())+10);
				else
					cr.setOrderNum(super.getMaxOrder(p.getId(), ControllerResources.class.getSimpleName())+10);
			}
			self = cr;
		} else {
			self.setClassName(cr.getClassName());
			self.setName(cr.getName());
			if(cr.getOrderNum()>0)
				self.setOrderNum(cr.getOrderNum());
			self.setSn(cr.getSn());
			self.setPsn(cr.getPsn());
			self.setParent(cr.getParent());
		}
		super.getSession().saveOrUpdate(self);
	}

	@Override
	public void addOper(ControllerOper oper, String rsn) {
		ControllerResources p = (ControllerResources)super.loadBySn(rsn, "ControllerResources");
		if(p==null) throw new SysException("Controller资源对象不存在！");
		ControllerOper co = this.loadOperBySn(rsn, oper.getSn());
		if(co==null) {
			co = oper;
		} else {
			co.setIndexPos(oper.getIndexPos());
			co.setMethodName(oper.getMethodName());
			co.setName(oper.getName());
			co.setSn(oper.getSn());
		}
		co.setRid(p.getId());
		co.setRsn(p.getSn());
		super.getSession().saveOrUpdate(co);
	}


	@Override
	public void updateOper(ControllerOper oper) {
		super.updateEntity(oper);
	}

	@Override
	public void deleteOper(String operId) {
		super.deleteEntity(this.loadOperById(operId));
	}

	@Override
	public List<ControllerResources> listResByParent(String pid) {
		String hql = null;
		if(pid==null||"".equals(pid)) {
			hql = getSelectHql()+" from ControllerResources cr where cr.parent is null order by cr.orderNum";
		} else {
			hql = getSelectHql()+" from ControllerResources cr where cr.parent.id="+pid+" order by cr.orderNum";
		}
		return super.list(hql);
	}

	@Override
	public List<ControllerOper> listOperByRes(String rid) {
		String hql = "selet co from ControllerOper co where co.rid=? ";
		return super.listObj(hql, rid);
	}

	@Override
	public ControllerOper loadOperById(String operId) {
		return (ControllerOper)super.loadEntity(operId, ControllerOper.class);
	}

	@Override
	public ControllerOper loadOperBySn(String rsn, String sn) {
		String hql = "select co from ControllerOper co where co.rsn=? and co.sn=?";
		return (ControllerOper)super.queryObject(hql, rsn,sn);
	}

	@Override
	public List<TreeDto> tree() {
		String sql = "select id,name,pid from t_controller_res order by order_num";
		List<TreeDto> tds = super.listBySql(sql, TreeDto.class, false);
		return tds;
	}

	@Override
	public ControllerResources loadBySn(String sn) {
		return (ControllerResources)super.loadBySn(sn, ControllerResources.class.getName());
	}


}
