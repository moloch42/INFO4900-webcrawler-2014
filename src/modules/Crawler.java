package modules;

import java.net.URL;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import dataModel.NodeObject;
import dataModel.NodeObject_Attribute;
import dataModel.Seller;
import dataModel.SiteFormat;
import dataModel.SiteFormat_NodeObject;

import modules.Logger;

public class Crawler {
	public static Vector<NodeObject> getNodeObjects(Connection pConn, int pintSiteFormatID) {
		Vector<NodeObject> no_vecNew = new Vector<NodeObject>();
		try {
			SiteFormat sf = new SiteFormat(pConn, pintSiteFormatID, true);
			Logger.debug("SiteFormat {" + sf.toString() + "} Created");
			SiteFormat_NodeObject sfpMain = sf.getSiteFormat_nodeObject();
			Logger.debug("SiteFormat_NodeObject {" + sfpMain.toString() + "} Created");

			HtmlCleaner hc = new HtmlCleaner();
			TagNode tnWebDoc;
			TagNode[] tn_arrNodeObject;
			for (int intPageCount = 1; intPageCount <= 2; intPageCount++) {
				String myUrl = sf.getURL() + sf.getUrl_page_id() + intPageCount;
				Logger.debug("getting and cleaning web document from '" + myUrl + "'");
				tnWebDoc = hc.clean(new URL(myUrl));
				Logger.debug(tagNodeToString(tnWebDoc, 0));
				Logger.debug("Searching for " + sfpMain.getElement_attribute_name() + "==" + sfpMain.getElement_attribute_value());
				tn_arrNodeObject = tnWebDoc.getElementsByAttValue(sfpMain.getElement_attribute_name(), sfpMain.getElement_attribute_value(), true, true);

				for (int intNodeObjectCount = 0; intNodeObjectCount < tn_arrNodeObject.length; intNodeObjectCount++) {
					no_vecNew.add(getNodeObject(tn_arrNodeObject[intNodeObjectCount], sfpMain, sf.getSeller()));
				}
			}
		} catch (Exception e) {
			Logger.error("An error occured getting node objects", e);
		}
		return no_vecNew;
	}

	private static NodeObject getNodeObject(TagNode ptnNodeObject, SiteFormat_NodeObject psfpNodeObject, Seller pSeller) {
		NodeObject noNew = null;
		try {
			noNew = new NodeObject();
			noNew.setActive_flag(true);
			noNew.setSeller(pSeller);

			noNew.getNodeObject_attributes().addAll(getrNodeObject_Attributes(noNew, ptnNodeObject, psfpNodeObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noNew;
	}

	private static Vector<NodeObject_Attribute> getrNodeObject_Attributes(NodeObject pnoOwner, TagNode ptnAttribute, SiteFormat_NodeObject psfpNodeObject) {
		Vector<NodeObject_Attribute> pa_vecNew = new Vector<NodeObject_Attribute>();
		try {
			String strAttributeValue, strContent;
			TagNode[] tn_arrElements;
			SiteFormat_NodeObject sfpAttribute;
			NodeObject_Attribute paNew;
			boolean blnFound;

			Iterator<SiteFormat_NodeObject> itChild_siteFormat_nodeObjects = psfpNodeObject.getChild_siteFormat_nodeObjects().iterator();
			while (itChild_siteFormat_nodeObjects.hasNext()) {
				sfpAttribute = itChild_siteFormat_nodeObjects.next();
				blnFound = false;

				tn_arrElements = ptnAttribute.getElementsByName(sfpAttribute.getElement_name(), true);
				for (int i = 0; i < tn_arrElements.length; i++) {
					if (sfpAttribute.getElement_attribute_name() != null) {
						strAttributeValue = tn_arrElements[i].getAttributeByName(sfpAttribute.getElement_attribute_name());
						if (strAttributeValue != null) {
							if (strAttributeValue.equals(sfpAttribute.getElement_attribute_value())) {
								if (sfpAttribute.getCode_nodeObject_attribute_id() >= 1000) {
									if (sfpAttribute.getElement_content_attribute_name() != null) {
										strContent = tn_arrElements[i].getAttributeByName(sfpAttribute.getElement_content_attribute_name());
									} else {
										strContent = tn_arrElements[i].getText().toString();
									}
									paNew = new NodeObject_Attribute(pnoOwner, sfpAttribute.getCode_nodeObject_attribute(), ContentFilter.cleanInput(strContent));
									pa_vecNew.add(paNew);
									blnFound = true;
								}
								pa_vecNew.addAll(getrNodeObject_Attributes(pnoOwner, tn_arrElements[i], sfpAttribute));
								if (blnFound) {
									break;
								}
							}
						}
					} else {
						if (sfpAttribute.getCode_nodeObject_attribute_id() >= 1000) {
							if (sfpAttribute.getElement_content_attribute_name() != null) {
								strContent = tn_arrElements[i].getAttributeByName(sfpAttribute.getElement_content_attribute_name());
							} else {
								strContent = tn_arrElements[i].getText().toString();
							}
							paNew = new NodeObject_Attribute(pnoOwner, sfpAttribute.getCode_nodeObject_attribute(), ContentFilter.cleanInput(strContent));
							pa_vecNew.add(paNew);
							blnFound = true;
						}
						pa_vecNew.addAll(getrNodeObject_Attributes(pnoOwner, tn_arrElements[i], sfpAttribute));
						if (blnFound) {
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pa_vecNew;
	}
	
	
	public static String tagNodeToString(TagNode node, int indent) {
		
		StringBuffer rv = new StringBuffer();
		
		for (int i=0; i < indent; i++) {
			rv.append("\t");
		}
		rv.append(node.toString() + ": ");
		Map<String, String> attributes = node.getAttributes();
		for (String key: attributes.keySet()) {
			rv.append(key + "==" + attributes.get(key) + " ");
		}
		
		
		rv.append("\n");
		
		for (TagNode t: node.getChildTags()) {
			rv.append(tagNodeToString(t, indent+1));
		}

		return rv.toString();
	}
}
