package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.PatientBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.PatientModel;

public class TestPatientModel {
	public static void main(String[] args)
			throws DatabaseException, ParseException, ApplicationException, DuplicateRecordException {

		// testnextPk();
		// testAdd();
		// testUpdate();
		// testDelete();
		// testfindByPk();
		// testfindByEmail();
		testsearch();
	}

	private static void testnextPk() throws DatabaseException {
		PatientModel model = new PatientModel();

		int i = model.nextPk();

		System.out.println("Nextpk" + i);

	}

	private static void testAdd() throws ParseException, ApplicationException, DuplicateRecordException {
		PatientBean bean = new PatientBean();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setFirstName("yusuf");
		bean.setLastName("khan");
		bean.setGender("male");
		bean.setDob(sdf.parse("2025-08-13"));
		bean.setContactNumber("9189907868");
		bean.setEmail("yusufkhan12@gmail.com");
		bean.setAddress("Indore,india");
		bean.setBloodGroup("A+");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		PatientModel model = new PatientModel();
		model.add(bean);

	}

	private static void testUpdate() throws ParseException, ApplicationException, DuplicateRecordException {
		PatientBean bean = new PatientBean();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		PatientModel model = new PatientModel();

		bean.setId(10);
		bean.setFirstName("Ilyas");
		bean.setLastName("mansuri");
		bean.setGender("male");
		bean.setDob(sdf.parse("2025-08-13"));
		bean.setContactNumber("9189907868");
		bean.setEmail("ilyaskhan12@gmail.com");
		bean.setAddress("Indore,india");
		bean.setBloodGroup("A+");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.update(bean);
	}

	private static void testDelete() throws ApplicationException {
		PatientBean bean = new PatientBean();
		PatientModel model = new PatientModel();
		model.delete(bean);

	}

	private static void testfindByPk() throws ApplicationException {
		PatientModel model = new PatientModel();
		PatientBean bean = model.findByPk(10);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getContactNumber());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getBloodGroup());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Id not found");
		}
	}

	private static void testfindByEmail() throws ApplicationException {
		PatientModel model = new PatientModel();
		PatientBean bean = model.findByEmail("pooja.mishra@example.com");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getContactNumber());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getBloodGroup());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Email Id not found");
		}
	}

	public static void testsearch() throws ApplicationException {

		PatientModel model = new PatientModel();

		PatientBean bean = new PatientBean();

		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (PatientBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getContactNumber());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getBloodGroup());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}
	}
}
