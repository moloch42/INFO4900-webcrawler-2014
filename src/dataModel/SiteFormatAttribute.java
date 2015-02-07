package dataModel;

import java.util.LinkedList;
import java.util.List;

import modules.ContentFilter;

import org.htmlcleaner.TagNode;


/** This class represents a single attribute that is part of a site format.
 * It is associated with one attribute name.
 * It is functionally represented by an ordered list of AttributePatterns. These
 * patterns represent an ordered list of the HTML tags that must be traversed to find
 * the desired item attribute.
 * @author David Tickner
 * */
public class SiteFormatAttribute {
    private AttributeName attributeName;

    /**
     * @associates <{dataModel.AttributePattern}>
     */
    private List<AttributePattern> attributePattern;

    private String finalAttribute;

    /**
     * @param name The AttributeName of this Attribute
     * @param patterns The AttributePatterns to identify this Attribute
     * @param finalAttribute if the value of this attribute is read from an HTML attribute rather that the body
     * of an HTML tag then set this to the name of the attribute. Otherwise set it to null.
     */
    public SiteFormatAttribute(AttributeName name, List<AttributePattern> patterns, String finalAttribute) {
        this.attributePattern = patterns;
        this.attributeName = name;
        this.finalAttribute = finalAttribute;
    }

    /**
     * @return The AttributeName object that identifies this attribute
     */
    public AttributeName getAttributeName() {
        return attributeName;
    }

    /**
     * @return The attributePatterns that are used to find this Attribute
     */
    public List<AttributePattern> getAttributePattern() {
        return attributePattern;
    }
    
    /**
     * @return if the text for this SiteFormatAttribute is to be parsed from a tag attribute rather than the tag body
     * then this method returns the name of the attribute to use
     */
    public String getFinalAttribute() {
		return finalAttribute;
	}


    /** Add a new pattern to the set of AttributePatterns.
     * @param pattern The new pattern to add
     */
    public void addAttributePattern(AttributePattern pattern) {
        this.attributePattern.add(pattern);
    }

    /** Parse any attributes identified by this object from a given HTML fragment.
     * @param item The Item that the parsed attributes will be associated with
     * @param rootItem The HTML fragment that the attributes will be parsed from
     * @return A list of the attributes that were found. This will often be of length one.
     */
    public List<ItemAttribute> parseItemAttribute(Item item, TagNode rootItem) {

        List<ItemAttribute> attributes = new LinkedList<ItemAttribute>();

        //Start with the root Node
        List<TagNode> currentNodes = new LinkedList<TagNode>();
        currentNodes.add(rootItem);

        List<TagNode> newNodes = new LinkedList<TagNode>();

        //for each pattern go through all of the current nodes and get all of the matching children
        //they will be the nodes for the next pass
        for (AttributePattern pattern : attributePattern) {

            for (TagNode node : currentNodes) {
                newNodes.addAll(pattern.patternMatch(node));
            }
            currentNodes = newNodes;
            newNodes = new LinkedList<TagNode>();

        }

        //for each node that we found make a new ItemAttribute
        //if the value is in an attribute, get it from there
        //otherwise get it from the tag text
        for (TagNode node : currentNodes) {
            String value;
            if (finalAttribute != null) {
                value = ContentFilter.cleanInput( node.getAttributeByName(finalAttribute) );
            } else {
                value = ContentFilter.cleanInput( node.getText().toString() );
            }
            attributes.add(new ItemAttribute(item, attributeName, value));
        }

        //extract the value from the TagNode
        return attributes;
    }
}
