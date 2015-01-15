package modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentFilter {
	public static String cleanInput(String pstrInput) {
		try {
			pstrInput = replaceAllHashChars(pstrInput);
			pstrInput = removeAllDollarSigns(pstrInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pstrInput;
	}

	public static String replaceAllHashChars(String pstrInput) {
		try {
			Pattern pattern = Pattern.compile("&#(\\d+);");
			Matcher matcher = pattern.matcher(pstrInput);

			while (matcher.find()) {
				pstrInput = pstrInput.replace(matcher.group(), (char) Integer.parseInt(matcher.group(1)) + "");
			}
			pstrInput = pstrInput.replaceAll("[\\n\\t\\r]", "").replaceAll("\\s\\s", "").replaceAll("'", "''").trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pstrInput;
	}

	public static String removeAllDollarSigns(String pstrInput) {
		try {
			pstrInput = pstrInput.replaceAll("\\$", "").replaceAll(",", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pstrInput;
	}
}
