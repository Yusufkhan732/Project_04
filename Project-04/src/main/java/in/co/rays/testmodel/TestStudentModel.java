package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.StudentModel;

public class TestStudentModel {

	public static void main(String[] args) throws Exception {

		// testNextPk();

		//testadd();

		 testupdate();

		// testfindByPk();

		// testfinfemail();
		// testsearch();
	}

	private static void testNextPk() throws Exception {

		StudentModel model = new StudentModel();

		int i = model.nextPk();
		System.out.println("NextPk:= " + i);
	}

	private static void testadd() throws Exception, ApplicationException, DuplicateRecordException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StudentBean bean = new StudentBean();
		bean.setFirstName("krishna");
		bean.setLastName("Meena");
		bean.setDob(sdf.parse("2025-07-07"));
		bean.setGender("male");
		bean.setMobileNo("7648880088");
		bean.setEmail("krishna@gmail.com");
		bean.setCollegeId(3);
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		StudentModel model = new StudentModel();
		model.add(bean);

	}

	private static void testupdate() throws Exception, ApplicationException, DuplicateRecordException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StudentBean bean = new StudentBean();
		bean.setId(13);
		bean.setFirstName("Nusad");
		bean.setLastName("khan");
		bean.setDob(sdf.parse("2025-07-07"));
		bean.setGender("male");
		bean.setMobileNo("7648880088");
		bean.setEmail("anas@gmail.com");
		bean.setCollegeId(3);
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		StudentModel model = new StudentModel();
		model.update(bean);

	}

	private static void testfindByPk() throws ApplicationException {

		StudentModel model = new StudentModel();

		StudentBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getCollegeId());
			System.out.print("\t" + bean.getCollegeName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("id not found");
		}
	}

	private static void testfinfemail() throws ApplicationException {

		StudentModel model = new StudentModel();
		StudentBean bean = model.findByEmail("rohit@gmail.com");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getCollegeId());
			System.out.print("\t" + bean.getCollegeName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("email not found");

		}
	}

	private static void testsearch() throws ApplicationException {
		StudentModel model = new StudentModel();

		StudentBean bean = new StudentBean();

		// bean.setFirstName("Azam");

		List list = model.search(bean, 0, 4);

		// List list = model.list();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (StudentBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getCollegeId());
			System.out.print("\t" + bean.getCollegeName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}
	}
}
