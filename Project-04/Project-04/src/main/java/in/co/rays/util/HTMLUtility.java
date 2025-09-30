package in.co.rays.util;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import in.co.rays.bean.DropdownListBean;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;

/**
 * Utility class to generate HTML select dropdowns from HashMap or List data.
 * Provides methods to create formatted select elements with selected value.
 * Also contains test methods to verify dropdown generation.
 * 
 * Author: Yusuf Khan Version: 1.0
 */
public class HTMLUtility {

	/**
	 * Generates an HTML select element from a HashMap.
	 *
	 * @param name        name attribute of the select element
	 * @param selectedVal value to be selected by default
	 * @param map         HashMap containing key-value pairs for options
	 * @return HTML select element as a String
	 */
	public static String getList(String name, String selectedVal, HashMap<String, String> map) {

		StringBuffer sb = new StringBuffer(
				"<select style=\"width: 170px;text-align-last: center;\"; class='form-control' name='" + name + "'>");

		sb.append("\n<option selected value=''>-------------Select-------------</option>");

		Set<String> keys = map.keySet();
		String val = null;

		for (String key : keys) {
			val = map.get(key);
			if (key.trim().equals(selectedVal)) {
				sb.append("\n<option selected value='" + key + "'>" + val + "</option>");
			} else {
				sb.append("\n<option value='" + key + "'>" + val + "</option>");
			}
		}
		sb.append("\n</select>");
		return sb.toString();
	}

	/**
	 * Generates an HTML select element from a List of DropdownListBean objects.
	 *
	 * @param name        name attribute of the select element
	 * @param selectedVal value to be selected by default
	 * @param list        List of DropdownListBean objects containing key-value
	 *                    pairs
	 * @return HTML select element as a String
	 */
	public static String getList(String name, String selectedVal, List list) {

		StringBuffer sb = new StringBuffer("<select style=\"width: 170px;text-align-last: center;\"; "
				+ "class='form-control' name='" + name + "'>");

		sb.append("\n<option selected value=''>-------------Select-------------</option>");

		if (list != null && !list.isEmpty()) {
			List<DropdownListBean> dd = (List<DropdownListBean>) list;

			String key = null;
			String val = null;

			for (DropdownListBean obj : dd) {
				key = obj.getKey();
				val = obj.getValue();

				if (key != null && key.trim().equals(selectedVal)) {
					sb.append("\n<option selected value='" + key + "'>" + val + "</option>");
				} else {
					sb.append("\n<option value='" + key + "'>" + val + "</option>");
				}
			}
		} else {
			sb.append("\n<option value=''>-- No Data Available --</option>");
		}

		sb.append("\n</select>");
		return sb.toString();
	}

	/**
	 * Test method to generate dropdown using a HashMap.
	 */
	public static void testGetListByMap() {

		HashMap<String, String> map = new HashMap<>();
		map.put("male", "male");
		map.put("female", "female");

		String selectedValue = "male";
		String htmlSelectFromMap = HTMLUtility.getList("gender", selectedValue, map);

		System.out.println(htmlSelectFromMap);
	}

	/**
	 * Test method to generate dropdown using a List of DropdownListBean.
	 *
	 * @throws Exception if an error occurs during test
	 */
	public static void testGetListByList() throws Exception {

		UserModel model = new UserModel();

		List<DropdownListBean> list = null;

		String selectedValue = null;

		String htmlSelectFromList = HTMLUtility.getList("name", selectedValue, list);

		System.out.println(htmlSelectFromList);
	}

	/**
	 * Main method to test dropdown generation methods.
	 *
	 * @param args command line arguments
	 * @throws Exception if an error occurs during test
	 */
	public static void main(String[] args) throws Exception {

		// testGetListByMap();
		testGetListByList();
	}
}
