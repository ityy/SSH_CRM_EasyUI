package cn.yang.test;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

import cn.yang.dao.UserDao;
import cn.yang.domain.Customer;
import cn.yang.domain.User;
import cn.yang.service.CustomerService;
import cn.yang.service.UserService;
import cn.yang.utils.PageBean;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

//测试hibernate框架
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class HibernateTest {
			@Resource(name="sessionFactory")
	private SessionFactory sf ;
	
		@Test
		//单独测试hibernate
		public void fun1(){
			Configuration conf = new Configuration().configure();
			
			SessionFactory sf = conf.buildSessionFactory();
			
			Session session = sf.openSession();
			
			Transaction tx = session.beginTransaction();
			//-------------------------------------------------
			User u = new User();
			
			u.setUser_code("rose");
			u.setUser_name("肉丝");
			u.setUser_password("1234");
			
			session.save(u);
			
			//-------------------------------------------------
			tx.commit();
			
			session.close();
			
			sf.close();
			
		}
		
		@Test
		//测试spring管理sessionFactory
		public void fun2(){
			
			Session session = sf.openSession();
			
			Transaction tx = session.beginTransaction();
			//-------------------------------------------------
			User u = new User();
			
			u.setUser_code("jack");
			u.setUser_name("杰克");
			u.setUser_password("1234");
			
			session.save(u);
			
			//-------------------------------------------------
			tx.commit();
			
			session.close();
			
		}
		@Resource(name="userDao")
		private UserDao ud;
		@Test
		//测试Dao Hibernate模板
		public void fun3(){
			
			User u = ud.getByUserCode("tom");
			
			System.out.println(u);
		}
		@Resource(name="userService")
		private UserService us;
		@Test
		//测试aop事务
		public void fun4(){
			User u = new User();
			
			u.setUser_code("hqy");
			u.setUser_name("郝强勇");
			u.setUser_password("1234");
			
			
			us.saveUser(u);
		}
		@Resource(name="customerService")
		private CustomerService cs;
		@Test
		public void fun5(){
			DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
			
			PageBean pageBean = cs.getPageBean(dc, 1, 3);
			
			JsonConfig config = new JsonConfig();
			//设置生成json时不包含哪些字段
			config.setExcludes(new String[]{"customer"});
			
			String json = JSONArray.fromObject(pageBean.getList(),config).toString();
			
			System.out.println(json);
		}
		
		@Test
		public void fun6(){
			DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
			
			PageBean pageBean = cs.getPageBean(dc, 1, 3);
			
			
			String json = JSON.toJSONString(pageBean.getList());
			
			System.out.println(json);
		}
		
}
