package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.FacultyBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.FacultyModel;

public class TestFacultyModel {

	public static void main(String[] args) throws Exception {

		// testadd();
		// testupdate();
		// testdelete();
		// testFindByPk();
		// testFindByEmail();
		// testsearch();
	}

	private static void testadd() throws Exception {
		FacultyBean bean = new FacultyBean();

		bean.setFirstName("Azam");
		bean.setLastName("khan");
		bean.setDob(new Date());
		bean.setGender("male");
		bean.setMobileNo("9109686959");
		bean.setEmail("azam@gmail.com");
		bean.setCollegeId(5);
		bean.setCourseId(101);
		bean.setSubjectId(202);
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		FacultyModel model = new FacultyModel();
		model.add(bean);

	}

	private static void testupdate() throws Exception {

		FacultyModel model = new FacultyModel();
		FacultyBean bean = model.findByPk(1);

		bean.setFirstName("Shad");
		bean.setLastName("khan");
		bean.setEmail("shad@gmail.com");

		model.update(bean);
	}

	private static void testdelete() throws ApplicationException {

		FacultyModel model = new FacultyModel();
		FacultyBean bean = new FacultyBean();
		model.delete(bean);
	}

	private static void testFindByPk() throws ApplicationException {

		FacultyModel model = new FacultyModel();

		FacultyBean bean = model.findByPk(10);

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
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("id not found");

		}

	}

	private static void testFindByEmail() throws ApplicationException {

		FacultyModel model = new FacultyModel();

		FacultyBean bean = model.findByEmail("neha.singh@college.com");

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
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("id not found");

		}
	}

	private static void testsearch() throws ApplicationException {

		FacultyModel model = new FacultyModel();

		FacultyBean bean = new FacultyBean();

		List list = model.search(bean, 1, 2);

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (FacultyBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getCollegeId());
			System.out.print("\t" + bean.getCollegeName());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}
	}
}
