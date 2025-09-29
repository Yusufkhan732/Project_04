package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.RoleBean;
import in.co.rays.model.RoleModel;

public class TestRoleModel {

	public static void main(String[] args) throws Exception {
		// testadd();
		// testupdate();
		// testdelete();
		// testfindbypk();
	//	testFindByName();
		 testserch();
	}

	public static void testadd() throws Exception {

		RoleBean bean = new RoleBean();
		bean.setName("Subject");
		bean.setDescription("Subject");
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		RoleModel model = new RoleModel();

		model.add(bean);

	}

	public static void testupdate() throws Exception {

		RoleBean bean = new RoleBean();
		bean.setId(1);
		bean.setName("Admin");
		bean.setDescription("Admin");
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		RoleModel model = new RoleModel();

		model.update(bean);

	}

	public static void testdelete() throws Exception {

		RoleModel model = new RoleModel();
		RoleBean bean = new RoleBean();
		model.delete(bean);

	}

	public static void testfindbypk() throws Exception {

		RoleModel model = new RoleModel();

		RoleBean bean = model.findByPk(2);

		if (bean != null) {

			if (bean != null) {
				System.out.print(bean.getId());
				System.out.print("\t" + bean.getName());
				System.out.print("\t" + bean.getDescription());
				System.out.print("\t" + bean.getCreatedBy());
				System.out.print("\t" + bean.getModifiedBy());
				System.out.print("\t" + bean.getCreatedDatetime());
				System.out.println("\t" + bean.getModifiedDatetime());
			}

		}

	}

	public static void testFindByName() throws Exception {

		RoleModel model = new RoleModel();

		RoleBean bean = model.findByName("Admin");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		}
	}

	public static void testserch() throws Exception {
		RoleModel model = new RoleModel();

		RoleBean bean = new RoleBean();
	//	bean.setName("Student");
		List list = model.search(bean, 1, 4);

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (RoleBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}
	}
}