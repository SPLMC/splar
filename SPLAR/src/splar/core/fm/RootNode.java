package splar.core.fm;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class RootNode extends FeatureTreeNode {

	public RootNode(  String id, String name, IFNodeRenderer nodeRenderer ) {		
		super(id,name,nodeRenderer);
//		value = FeatureTreeNode.SELECTED;
//		super.setImmutable(true);
	}
	
	public String toString() {
		return ":r " + super.toString();
	}
	
	
	public Element createFeatureIdeElement(Document doc) {
		Element e = null; 
		String isAbstract = (String) this.getProperty("abstract"); 
		
		int numOfChildren = this.getChildCount();
		
		//Creating the DOM element for the root node
		if (numOfChildren == 0) {
			e = doc.createElement("feature"); 
		} else {
			e = doc.createElement("and"); 
			//Creating the DOM element for each child of root node
			for (int i=0; i<numOfChildren; i++) {
				FeatureTreeNode tn = (FeatureTreeNode) this.getChildAt(i); 
				Element c = tn.createFeatureIdeElement(doc); 
				e.appendChild(c);
			}
		}
		
		e.setAttribute("name", getName());
		e.setAttribute("mandatory", "true");
		e.setAttribute("abstract", isAbstract);
		
		
		return e;
	}
}
