package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.CourseBean;
import in.co.rays.model.CourseModel;

public class TestCourseModel {

	public static void main(String[] args) throws Exception {

		// testNextPK();
		// testAdd();
		// testDelete();
		// testfindByPk();
		// testfindByName();
		// testsearch();
	}

	private static void testNextPK() throws Exception {

		CourseModel model = new CourseModel();

		int i = model.nextPk();
		System.out.println("NextPk:" + i);

	}

	private static void testAdd() throws Exception {

		CourseBean bean = new CourseBean();

		CourseModel model = new CourseModel();

		bean.setName("CA");
		bean.setDuration("2 year");
		bean.setDescription("Economic");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.add(bean);

	}

	private static void testDelete() throws Exception {

		CourseModel model = new CourseModel();
		CourseBean bean = new CourseBean();
		model.delete(bean);

	}

	private static void testfindByPk() throws Exception {
		CourseModel model = new CourseModel();

		CourseBean bean = model.findByPk(103);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getDuration());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("id not found");
		}
	}

	private static void testfindByName() throws Exception {
		CourseModel model = new CourseModel();

		CourseBean bean = model.findByName("BBA");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getDuration());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		} else {
			System.out.println("name not found");
		}
	}

	private static void testsearch() throws Exception {

		CourseBean bean = new CourseBean();

		CourseModel model = new CourseModel();

		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (CourseBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getDuration());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}
	}
}
