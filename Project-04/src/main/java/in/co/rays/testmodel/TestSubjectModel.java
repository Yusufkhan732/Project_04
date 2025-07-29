package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.SubjectModel;

public class TestSubjectModel {

	public static void main(String[] args) throws Exception {
		// nextPk();

		// testadd();
		 testupadte();
		// testdelete();
		// testfindByPk();
		//testfindByName();
		// testsearch();
	}

	private static void nextPk() throws Exception {

		SubjectModel model = new SubjectModel();

		int i = model.nextPk();
		System.out.println("NextPk = " + i);

	}

	private static void testadd() throws Exception {

		SubjectBean bean = new SubjectBean();

		bean.setName("Mechanical");
		bean.setCourseId(2);
		bean.setDescription("Mechanical");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		SubjectModel model = new SubjectModel();
		model.add(bean);

	}

	private static void testupadte() throws Exception {
		SubjectBean bean = new SubjectBean();
		bean.setId(11);
		bean.setName("ITI");
		bean.setCourseId(2);
		bean.setDescription("ITI");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		SubjectModel model = new SubjectModel();
		model.update(bean);
	}

	private static void testdelete() throws ApplicationException {

		SubjectModel model = new SubjectModel();
		SubjectBean bean = new SubjectBean();

		model.delete(bean);
	}

	private static void testfindByPk() throws ApplicationException {

		SubjectModel model = new SubjectModel();
		SubjectBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {

			System.out.println("id not found");
		}
	}

	private static void testfindByName() throws ApplicationException {

		SubjectModel model = new SubjectModel();
		SubjectBean bean = model.findByName("Mechanical");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {

			System.out.println("id not found");
		}
	}

	private static void testsearch() throws ApplicationException {

		SubjectModel model = new SubjectModel();
		SubjectBean bean = new SubjectBean();

		bean.setName("ITI");
		List list = model.search(bean, 1, 4);

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (SubjectBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}
	}
}
