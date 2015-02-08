package dataModel;

import java.util.LinkedList;
import java.util.List;

import org.htmlcleaner.TagNode;

import dataModel.exceptions.AttributePatternException;

/**
* This class contains the information needed to identify one HTML tag in the chain
* of tags that defines how an ItemAttribute is to be parsed from an HTML document.
* Given a TagNode it can locate any of that node's children that match the criteria
* stored in this class.
*/
public class AttributePattern {

	private String element_name;
	private String element_attribute_name;
	private String element_attribute_value;


    /**
     * @param elementName The tag type that will be matched by this pattern. Mandatory.
     * @param elementAttributeName The tag attribute name that will be matched by this pattern. Mandatory if there
     * is an elementAttributeValue in this pattern.
     * @param elementAttributeValue That value of the tag attribute that will be matched by this pattern. Optional.
     * @throws AttributePatternException If an unusable pattern is provided.
     */
    public AttributePattern(String elementName, String elementAttributeName,
                            String elementAttributeValue) throws AttributePatternException {
        if (elementName == null) {
            throw new AttributePatternException("element_name can not be null");
        }
        if (elementAttributeName == null && elementAttributeValue != null) {
            throw new AttributePatternException("You can not have an element_attribute_value without an element_attribute_name");
        }

        this.element_name = elementName;
        this.element_attribute_name = elementAttributeName;
        this.element_attribute_value = elementAttributeValue;
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

            List<TagNode> matchingNodes = new LinkedList<TagNode>();

            //get all direct child nodes that match the tag type
            List<TagNode> nodes = node.getElementListByName(element_name, false);

            
            
            for (TagNode t: nodes) {
            	//skip this node if the attribute name doesn't match
            	if (element_attribute_name != null && !t.hasAttribute(element_attribute_name)) {
            		continue;
            	}
            	//skip this node if the attribute value doesn't match
            	if (element_attribute_value != null && !t.getAttributeByName(element_attribute_name).equals(element_attribute_value)) {
            		continue;
            	}
            	
            	//the node matches, add it to the list
            	matchingNodes.add(t);
            }

            return matchingNodes;
        }
}
