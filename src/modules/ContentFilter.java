package modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO update this javadoc
/**
* @author
*/
public class ContentFilter {
    
    /** this method cleans a given HTML string in preparation for parsing into a TagNode.
     * @param pstrInput The HTML string to be cleaned
     * @return The cleaned String
     */
    public static String cleanInput(String pstrInput) {
        try {
            pstrInput = replaceAllHashChars(pstrInput);
            pstrInput = removeAllDollarSigns(pstrInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pstrInput;
    }

    /** This method replaces all hash characters in a given string
     * @param pstrInput The string to process
     * @return the processed string
     */
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

    /** This method removes all dollar signs from a given string
     * @param pstrInput The string to process
     * @return The processed string
     */
    public static String removeAllDollarSigns(String pstrInput) {
        try {
            pstrInput = pstrInput.replaceAll("\\$", "").replaceAll(",", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pstrInput;
    }
}
