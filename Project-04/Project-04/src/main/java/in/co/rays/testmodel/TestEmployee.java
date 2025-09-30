package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.EmployeeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.EmployeeModel;

public class TestEmployee {
	public static void main(String[] args) throws Exception {
	//	TestSearch();

		TestAdd();
	}

	private static void TestAdd() throws ApplicationException {
		EmployeeBean bean = new EmployeeBean();

		bean.setEmployeeName("Farhan");
		bean.setLastName("khan");
		bean.setDob(new Date());
		bean.setDepartment("Finanace");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		EmployeeModel model = new EmployeeModel();

		model.add(bean);
	}

	private static void TestSearch() throws Exception {
		EmployeeModel model = new EmployeeModel();
		EmployeeBean bean = new EmployeeBean();

		bean.setDepartment("IT");
		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (EmployeeBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getEmployeeName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDepartment());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		}

	}

}
