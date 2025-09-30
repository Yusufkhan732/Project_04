package in.co.rays.util;

import java.util.ResourceBundle;

/**
 * Utility class to read property values from a ResourceBundle. Supports single
 * and multiple parameter replacements in property messages.
 * 
 * Author: Yusuf Khan Version: 1.0
 */
public class PropertyReader {

	/** ResourceBundle to read properties from */
	private static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.bundle.system");

	/**
	 * Returns the value of a property key.
	 * 
	 * @param key the property key
	 * @return value of the key or the key itself if not found
	 */
	public static String getValue(String key) {
		String val = null;
		try {
			val = rb.getString(key); // {0} is required
		} catch (Exception e) {
			val = key;
		}
		return val;
	}

	/**
	 * Returns the value of a property key with a single parameter replacement.
	 * Replaces "{0}" in the property value with the provided parameter.
	 * 
	 * @param key   the property key
	 * @param param parameter to replace {0} in the property value
	 * @return formatted property value
	 */
	public static String getValue(String key, String param) {
		String msg = getValue(key); // {0} is required
		msg = msg.replace("{0}", param);
		return msg;
	}

	/**
	 * Returns the value of a property key with multiple parameter replacements.
	 * Replaces "{0}", "{1}", etc. in the property value with provided parameters.
	 * 
	 * @param key    the property key
	 * @param params array of parameters to replace placeholders in the property
	 *               value
	 * @return formatted property value
	 */
	public static String getValue(String key, String[] params) {
		String msg = getValue(key);
		for (int i = 0; i < params.length; i++) {
			msg = msg.replace("{" + i + "}", params[i]);
		}
		return msg;
	}

	/**
	 * Main method to demonstrate reading property values and parameter replacement.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		System.out.println("Single key example:");
		System.out.println(PropertyReader.getValue("error.require"));

		System.out.println("\nSingle parameter replacement example:");
		System.out.println(PropertyReader.getValue("error.require", "loginId"));

		System.out.println("\nMultiple parameter replacement example:");
		String[] params = { "Roll No", "Student Name" };
		System.out.println(PropertyReader.getValue("error.multipleFields", params));
	}
}
