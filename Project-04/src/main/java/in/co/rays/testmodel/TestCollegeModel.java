package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CollegeModel;

public class TestCollegeModel {

	public static void main(String[] args) throws DatabaseException, ApplicationException, DuplicateRecordException {

		// nextPk();
		// testadd();
		// testupadte();
		// testdelete();
		// testfindByPk();
		// testfindByName();
		testsearch();

	}

	private static void nextPk() throws DatabaseException {

		CollegeModel model = new CollegeModel();
		int i = model.nextPk();

		System.out.println("NextPk =:" + i);
	}

	private static void testadd() throws ApplicationException, DuplicateRecordException {

		CollegeBean bean = new CollegeBean();

		bean.setName("Makhanlal College");
		bean.setAddress("Sehore Road");
		bean.setCity("Bhopal");
		bean.setState("MP");
		bean.setPhoneNo("8769767794");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		CollegeModel model = new CollegeModel();
		model.add(bean);
	}

	private static void testupadte() throws ApplicationException, DuplicateRecordException {
		CollegeBean bean = new CollegeBean();
		bean.setId(11);
		bean.setName(" College");
		bean.setAddress("Khandwa Road");
		bean.setCity("Indore");
		bean.setState("MP");
		bean.setPhoneNo("8769767794");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		CollegeModel model = new CollegeModel();
		model.update(bean);
	}

	private static void testdelete() throws ApplicationException {
		CollegeModel model = new CollegeModel();
		CollegeBean bean = new CollegeBean();
		model.delete(bean);
	}

	private static void testfindByPk() throws ApplicationException {
		CollegeModel Model = new CollegeModel();
		CollegeBean bean = Model.findByPk(10);
		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getCity());
			System.out.print("\t" + bean.getState());
			System.out.print("\t" + bean.getPhoneNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("id not found");
		}
	}

	private static void testfindByName() throws ApplicationException {

		CollegeModel Model = new CollegeModel();
		CollegeBean bean = Model.findByName("LNCT");
		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getCity());
			System.out.print("\t" + bean.getState());
			System.out.print("\t" + bean.getPhoneNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("name not found");
		}
	}

	private static void testsearch() throws ApplicationException {

		CollegeModel model = new CollegeModel();
		CollegeBean bean = new CollegeBean();

		//bean.setName("MITS Gwalior");
		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (CollegeBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getCity());
			System.out.print("\t" + bean.getState());
			System.out.print("\t" + bean.getPhoneNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}
	}
}
