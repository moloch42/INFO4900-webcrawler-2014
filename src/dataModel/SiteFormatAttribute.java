package dataModel;

import java.util.LinkedList;
import java.util.List;

import org.htmlcleaner.TagNode;


/** This class represents a single attribute that is part of a site format.
 * It is associated with one attribute name.
 * It is functionally represented by an ordered list of AttributePatterns. These
 * patterns represent an ordered list of the HTML tags that must be traversed to find
 * the desired item attribute.
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

//    /**
//     * @param attributeName
//     */
//    public void setAttributeName(AttributeName attributeName) {
//        this.attributeName = attributeName;
//    }

    /**
     * @return The AttributeName object that identifies this attribute
     */
    public AttributeName getAttributeName() {
        return attributeName;
    }

//    /**
//     * @param attributePattern
//     */
//    public void setAttributePattern(List<AttributePattern> attributePattern) {
//        this.attributePattern = attributePattern;
//    }

    /**
     * @return Tha attributePatterns that are used to find this Attribute
     */
    public List<AttributePattern> getAttributePattern() {
        return attributePattern;
    }

    /** Add a newpattern to the set of AttributePatterns.
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

        //TODO implement this method
        List<ItemAttribute> attributes = new LinkedList<ItemAttribute>();
        return attributes;
    }
}
