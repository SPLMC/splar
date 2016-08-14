package splar.core.fm;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class FeatureGroup extends FeatureTreeNode {

	private boolean isOptional = false;
	private int min=1, max=1;
	
	public FeatureGroup( String id, String name, int min, int max, IFNodeRenderer nodeRenderer ) {		
		super(id,name,nodeRenderer);
		this.min = min;
		this.max = max;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}

	public String getDescription() {
		String name = "";
		if ( !getName().startsWith("_")) {
			name = getName();
		}
		return name + " [" + getMin() + "," + (getMax()==-1?"*":getMax()) + "] ";
	}

	public String toString() {
		return ":g "+ super.toString() + " [" + getMin() + "," + (getMax()==-1?"*":getMax()) + "] ";
	}
	
	
	/**
	 * This method creates the DOM element a grouping feature and its children features
	 * @param doc - The DOM document where the element will be created
	 * @return e - The DOM element containing all the information of the feature and 
	 * its children
	 * 
	 *  @author Andre Luiz Peron Martins Lanna
	 */
	public Element createFeatureIdeElement(Document doc) {
		//Choosing each type of DOM element for representing the feature
		String groupType = (getMin()==1 && getMax()==1 ? "alt" : "or");
		String isAbstract = (String) getProperty("abstract");
		Object objIsMandatory = getProperty("mandatory");
		boolean isMandatory = false;
		if (objIsMandatory != null)
			isMandatory = (boolean) getProperty("mandatory");
		

		//
		String name = getName().replaceFirst("_", "");
		//setting the element's attributes
		Element e = doc.createElement(groupType);
		e.setAttribute("mandatory", "true");
		e.setAttribute("name", name);
		if (isAbstract != null && isAbstract.equals("true"))
			e.setAttribute("abstract", "true");
		if (isMandatory)
			e.setAttribute("mandatory", "true");
		else
			e.setAttribute("mandatory", "false");
		
		int numChildren = this.getChildCount(); 
		for (int i=0; i<numChildren; i++) {
			FeatureTreeNode tn = (FeatureTreeNode) getChildAt(i);
			Element c = tn.createFeatureIdeElement(doc); 
			e.appendChild(c);
		}
		
		return e;
	}
}