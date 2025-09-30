package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.UserModel;

public class TestUserModel {

	public static void main(String[] args) throws Exception {
		 testnextPk();
		// testAdd();
		// testupdate();
		// testdelete();
		// testFindbyPk();
//		 testsearch();
	}

	public static void testnextPk() throws Exception {

		UserModel model = new UserModel();

		int i = model.nextPk();

		System.out.println("NextPk:" + i);
	}

	public static void testAdd() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		UserModel model = new UserModel();

		UserBean bean = new UserBean();

		bean.setFirstName("Anas");
		bean.setLastName("khan");
		bean.setLogin("anas@gmail.com");
		bean.setPassword("pass123");
		// bean.setConfirmPassword("Ak567");
		bean.setDob(sdf.parse("2025-07-07"));
		bean.setMobileNo("9145678990");
		bean.setRoleId(3);
		bean.setGender("male");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	public static void testupdate() throws ApplicationException, DuplicateRecordException {

		UserModel model = new UserModel();

		UserBean bean = new UserBean();
		bean.setId(11);
		bean.setFirstName("Mohit");
		bean.setLastName("meena");
		bean.setLogin("mohit@gmail.com");
		bean.setPassword("pass123");
		// bean.setConfirmPassword("MH7777");
		bean.setDob(new Date());
		bean.setMobileNo("9145678990");
		bean.setRoleId(2);
		bean.setGender("male");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.update(bean);

	}

	public static void testdelete() throws Exception {

		UserModel model = new UserModel();

		UserBean bean = new UserBean();

		model.delete(bean);

	}

	public static void testFindbyPk() throws Exception {

		UserModel model = new UserModel();

		UserBean bean = model.findByPk(2);

		if (bean != null) {

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getLogin());
			System.out.print("\t" + bean.getPassword());
			System.out.print("\t" + bean.getConfirmPassword());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getRoleId());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		}
	}

	public static void testFindByLogin() throws Exception {

		UserModel model = new UserModel();

		UserBean bean = model.authenticate("www@gmail.com", "yk567");

		if (bean != null) {

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getLogin());
			System.out.print("\t" + bean.getPassword());
			System.out.print("\t" + bean.getConfirmPassword());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getRoleId());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		}
	}

	public static void testsearch() throws Exception {

		UserBean bean = new UserBean();

		UserModel model = new UserModel();
		//bean.setLogin("riya.mehta@example.com");
		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (UserBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getLogin());
			System.out.print("\t" + bean.getPassword());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getRoleId());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		}

	}
}
