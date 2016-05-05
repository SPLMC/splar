package splar.core.fm;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GroupedFeature extends FeatureTreeNode {

	private boolean isOptional = false;
	
	public GroupedFeature(  String id, String name, IFNodeRenderer nodeRenderer ) {		
		super(id,name,nodeRenderer);
	}
	
	public FeatureGroup getGroup() {
		return (FeatureGroup)getParent();
	}
	
	public String toString() {
		return ": " + super.toString();
	}
	
	
	/**
	 * This method creates the DOM element for this feature and its children features
	 * @param doc - The DOM document where the element will be created
	 * @return e - The DOM element containing all the information of the feature and 
	 * its children
	 * 
	 *  @author Andre Luiz Peron Martins Lanna
	 */
	public Element createFeatureIdeElement(Document doc) {
		Element e = null;
		String isAbstract = (String) getProperty("abstract"); 
		
		int numChildren = this.getChildCount();
		
		//Choosing which kind of not for representing the feature
		if (numChildren == 0) {
			//Creating the DOM element of the feature
			e = doc.createElement("feature"); 
		} else {
			e = doc.createElement("and"); 
			//appending the DOM elements for each one of its children
			for (int i=0; i<numChildren; i++) {
				FeatureTreeNode tn = (FeatureTreeNode) this.getChildAt(i); 
				Element c = tn.createFeatureIdeElement(doc); 
				e.appendChild(c);
			}		
		}
		
		//setting the element's attributes
		e.setAttribute("name", this.getName());
//		if (!isOptional())
//			e.setAttribute("mandatory", "true");
		if (isAbstract != null && isAbstract.equals("true"))
			e.setAttribute("abstract", "true");			
		
		//return the element created
		return e;
	}
}