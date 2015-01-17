package dataModel;

import java.util.LinkedList;
import java.util.List;

import org.htmlcleaner.TagNode;

import dataModel.exceptions.AttributePatternException;

//TODO update this javadoc
/**
* @author
*/
public class AttributePattern {

	private String element_name;
	private String element_attribute_name;
	private String element_attribute_value;
	//private String element_content_attribute_name;


    /**
     * @param elementName The tag type that will be matched by this pattern. Manditory.
     * @param elementAttributeName The tag attribute name that will be matched by this pattern. Manditory if there
     * is an elementAttributeValue in this pattern.
     * @param elementAttributeValue That value of the tag attribute that will be matched by this pattern. Optional.
     * @throws AttributePatternException If an unusable pattern is provided.
     */
    public AttributePattern(String elementName, String elementAttributeName,
                            String elementAttributeValue) throws AttributePatternException {
            this.element_name = elementName;
            this.element_attribute_name = elementAttributeName;
            this.element_attribute_value = elementAttributeValue;
            //this.element_content_attribute_name = elementContentAttributeName;
        }


    /**
     * @return the tag type that will be matched by this pattern.
     */
    public String getElement_name() {
        return element_name;
    }


    /**
     * @return The tag attribute name that will be matched by this pattern.
     */
    public String getElement_attribute_name() {
        return element_attribute_name;
    }


    /**
     * @return The tag attribute value that will be matched by this pattern.
     */
    public String getElement_attribute_value() {
        return element_attribute_value;
    }


    /**
     * @param node a TagNode representation of an HTML tag.
     * @return all subtags of the given tag that match this pattern
     */ 
        public List<TagNode> patternMatch(TagNode node) {
            //TODO Implement This method
            //given a TagNode, return the child node(s?) that matche this pattern or empty list if nothing matches
            return new LinkedList<TagNode>();
        }


}
