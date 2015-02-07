package modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO update this javadoc
/**
* @author Unknown
*/
public class ContentFilter {
    
    /** this method cleans a given HTML string in preparation for parsing into a TagNode.
     * @param pstrInput The HTML string to be cleaned
     * @return The cleaned String
     */
    public static String cleanInput(String pstrInput) {
    	String rv = pstrInput;
        try {
        	rv = replaceAllHashChars(rv);
        	//rv = removeAllDollarSigns(rv);
        } catch (Exception e) {
            Logger.error("An error occured while cleaning input", e);
        }
        return rv;
    }

    /** This method replaces all hash characters in a given string
     * @param pstrInput The string to process
     * @return the processed string
     */
    public static String replaceAllHashChars(String pstrInput) {
    	String rv = pstrInput;
        try {
            Pattern pattern = Pattern.compile("&#(\\d+);");
            Matcher matcher = pattern.matcher(rv);

            while (matcher.find()) {
                rv = rv.replace(matcher.group(), (char) Integer.parseInt(matcher.group(1)) + "");
            }
           
        } catch (Exception e) {
        	 Logger.error("An error occured while replacing hash characters", e);
        }
        return rv;
    }

    /** This method removes all dollar signs from a given string
     * @param pstrInput The string to process
     * @return The processed string
     */
    public static String removeAllDollarSigns(String pstrInput) {
    	String rv = pstrInput;
        try {
            rv = rv.replaceAll("\\$", "");
        } catch (Exception e) {
        	Logger.error("An error occured while removing dollar signs", e);
        }
        return rv;
    }
    
    /** This method removes various characters
     * @param input The string to process
     * @return The processed string
     * */
    public static String formatString(String input) {
    	String rv = input;
    	rv = rv.replaceAll("[\\n\\t\\r]", "").replaceAll("\\s\\s", " ").replaceAll("'", "''").replaceAll(",", "").trim();
    	return rv;
    }
}
