package in.co.rays.testmodel;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.TimetableModel;

public class TestTimetableModel {

	public static void main(String[] args) throws Exception {
		// testAdd();
		// testUpdate();
		// testDelete();
		// testFindByPk();
		// testSearch();
		// testCoursetName();
		// testSubjectName();
		// testexamTime();
		testSemester();
	}

	public static void testAdd() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddd");
		TimetableBean bean = new TimetableBean();
		bean.setSemester("BCA");
		bean.setDescription("first");
		bean.setExamDate(sdf.parse("2025-07-08"));
		bean.setExamTime("9:00AM - 12:00PM");
		bean.setCourseId(2);
		bean.setSubjectId(1);
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		TimetableModel model = new TimetableModel();
		model.add(bean);
	}

	public static void testUpdate() throws Exception {

		TimetableModel model = new TimetableModel();

		TimetableBean bean = model.findByPk(2);
		bean.setSemester("4rd");
		bean.setDescription("4rd");
		bean.setExamDate(new Date());
		bean.setExamTime("11:00AM - 12:00PM");
		bean.setCourseId(1);
		bean.setSubjectId(1);

		model.update(bean);
	}

	public static void testDelete() throws Exception {
		TimetableModel model = new TimetableModel();
		TimetableBean bean = new TimetableBean();
		model.delete(bean);
	}

	public static void testFindByPk() throws Exception {

		TimetableModel model = new TimetableModel();

		TimetableBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
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

	public static void testCoursetName() throws ApplicationException, ParseException {

		TimetableModel model = new TimetableModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TimetableBean bean = model.checkByCourseName(1l, sdf.parse("2025-08-01 00:00:00"));

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Course name not found");

		}
	}

	public static void testSubjectName() throws ApplicationException, ParseException {

		TimetableModel model = new TimetableModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TimetableBean bean = model.checkBySubjectName(4l, 4l, sdf.parse("2025-08-04 00:00:00"));

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("subject  name not found");

		}
	}

	public static void testSemester() throws ApplicationException, ParseException {

		TimetableModel model = new TimetableModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TimetableBean bean = model.checkBySemester(5l, 5l, "1st", sdf.parse("2025-08-05 00:00:00"));

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("semester name not found");

		}
	}

	public static void testexamTime() throws ApplicationException, ParseException {

		TimetableModel model = new TimetableModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TimetableBean bean = model.checkByExamTime(3l, 3l, "3rd", sdf.parse("2025-08-03 00:00:00"), "09:00 AM",
				"Internal Test");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("exam time  not found");

		}
	}

	public static void testSearch() throws Exception {

		TimetableBean bean = new TimetableBean();
		bean.setSemester("test");

		TimetableModel model = new TimetableModel();

		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (TimetableBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
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
