package splar.core.fm.personalization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import splar.core.constraints.BooleanVariableInterface;
import splar.core.constraints.CNFClause;
import splar.core.constraints.CNFGenerator;
import splar.core.fm.FeatureGroup;
import splar.core.fm.FeatureModel;
import splar.core.fm.FeatureTreeNode;
import splar.core.fm.GroupedFeature;
import splar.core.fm.RootNode;
import splar.core.fm.SolitaireFeature;
import splar.core.fm.TreeNodeRendererFactory;

public class PersonalFeatureModel extends FeatureModel implements
		IPersonalFeatureModel {

	private static final int UPPER = 1;
	private static final int LOWER = Integer.MAX_VALUE;
	List<FeatureTreeNode> fmNodes;
	List<BooleanVariableInterface> vars;
	List<CNFClause> clauses; 

	public PersonalFeatureModel() {
		super();
		fmNodes = new ArrayList<FeatureTreeNode>();
		vars = new LinkedList<BooleanVariableInterface>();
		clauses = new LinkedList<CNFClause>();
	}

	public CNFClause createClause(String expression) {
		return null;
	}
	
	@Override
	protected FeatureTreeNode createNodes() {
		return null;
	}

	@Override
	protected void saveNodes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFMfromFeatureIDEXML(String path) {
		File xmlFile = new File(path); 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder dbBuilder;
		try {
			dbBuilder = dbFactory.newDocumentBuilder();
			Document document = dbBuilder.parse(xmlFile);
			
			NodeList fmStructure = document.getElementsByTagName("struct"); 
			Node rootElement = getRootElement(fmStructure);
			String rootName = rootElement.getAttributes().getNamedItem("name").getNodeValue();
			FeatureTreeNode f = createRootNode(rootName, rootName);
			
			List<Node> rootElementChildren = getElementNodes(rootElement.getChildNodes());
			for(Node n : rootElementChildren) {
				FeatureTreeNode child = createChildFeature(n, f);
				f.add(child);
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private FeatureTreeNode createChildFeature(Node node, FeatureTreeNode parent) {
		FeatureTreeNode answer = null;
		String nodeName; 
		String nodeType;
		String isMandatory;
		
		nodeType = node.getNodeName();
		nodeName = node.getAttributes().getNamedItem("name").getNodeValue(); 
		isMandatory = node.getAttributes().getNamedItem("mandatory").getNodeValue();
		
		FeatureTreeNode feat = null; 
		switch (nodeType) {
		case "and":
			if (isMandatory.equalsIgnoreCase("true"))
				feat = createMandatoryFeature(nodeName, nodeName, parent);
			else 
				feat = createOptionalFeature(nodeName, nodeName, parent);
			break;
		
		case "or":
			feat = createInclusiveOrFeature(nodeName, nodeName, parent);
			if (isMandatory.equalsIgnoreCase("true"))
				feat.setProperty("mandatory", true);
			break;

		case "alt": 
			feat = createExclusiveOrFeature(nodeName, nodeName, parent); 
			if (isMandatory.equalsIgnoreCase("true"))
				feat.setProperty("mandatory", true);
			break;
			
		case "feature":
			if (parent instanceof FeatureGroup) 
				feat = createGroupedFeature(nodeName, nodeName, parent);
			else
				feat = createOptionalFeature(nodeName, nodeName, parent);
			
			if (isMandatory.equalsIgnoreCase("true"))
				feat.setProperty("mandatory", "true");
			break;
		default:
			break;
		}
		
		List<Node> nodeElementChildren = getElementNodes(node.getChildNodes());
		for (Node n : nodeElementChildren) {
			FeatureTreeNode child = createChildFeature(n, feat); 
			feat.add(child);
		}
		
		answer = feat;
		return answer;
	}

	private List<Node> getElementNodes(NodeList childNodes) {
		List<Node> answer = new LinkedList<Node>();
		for (int i=0; i<childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE &&
				!childNodes.item(i).getNodeName().equalsIgnoreCase("description")){
				answer.add(childNodes.item(i));
			}
		}
		return answer;
	}

	private Node getRootElement(NodeList fmStructure) {
		Node answer = null;
//		System.out.println("|fmStructure = " + fmStructure);
		Node strucElement = fmStructure.item(0); 
		for (int i=0; i < strucElement.getChildNodes().getLength(); i++) {
			Node n = strucElement.getChildNodes().item(i); 
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				answer = n; 
			}
		}
		return answer;
	}

	@Override
	public RootNode createRootNode(String id, String name) {
		root = new RootNode(id, name,
				TreeNodeRendererFactory.createRootRenderer());
		vars.add(root);  //add the feature in the list of variables
		boolean isAdded = fmNodes.add(root);
		RootNode answer = (isAdded ? (RootNode)root : null);
		return answer;
	}

	@Override
	public FeatureTreeNode createOptionalFeature(String id, String name,
			FeatureTreeNode parent) {
		FeatureTreeNode temp = new SolitaireFeature(true, id, name,
				TreeNodeRendererFactory.createOptionalRenderer());

		if (parent != null) {
			parent.add(temp);
		}

		vars.add(temp); //add the feature in the list of variables
		boolean isAdded = fmNodes.add(temp);
		FeatureTreeNode answer = (isAdded ? temp : null);
		return answer;
	}

	@Override
	public FeatureTreeNode createMandatoryFeature(String id, String name,
			FeatureTreeNode parent) {
		FeatureTreeNode temp = new SolitaireFeature(false, id, name,
				TreeNodeRendererFactory.createOptionalRenderer());

		if (parent != null) {
			parent.add(temp);
		}

		vars.add(temp); //add the feature in the list of variables
		boolean isAdded = fmNodes.add(temp);
		FeatureTreeNode answer = (isAdded ? temp : null);
		return answer;
	}

	@Override
	public FeatureTreeNode createExclusiveOrFeature(String id, String name,
			FeatureTreeNode parent) {
		FeatureTreeNode temp = new FeatureGroup(id, name, 1, 1,
				TreeNodeRendererFactory.createFeatureGroupRenderer());

		if (parent != null) {
			parent.add(temp);
		}

		vars.add(temp); //add the feature in the list of variables
		boolean isAdded = fmNodes.add(temp);
		FeatureTreeNode answer = (isAdded ? temp : null);
		return answer;
	}

	@Override
	public FeatureTreeNode createInclusiveOrFeature(String id, String name,
			FeatureTreeNode parent) {
		FeatureTreeNode temp = new FeatureGroup(id, name, LOWER, UPPER,
				TreeNodeRendererFactory.createFeatureGroupRenderer());

		if (parent != null) {
			parent.add(temp);
		}

		vars.add(temp); //add the feature in the list of variables
		boolean isAdded = fmNodes.add(temp);
		FeatureTreeNode answer = (isAdded ? temp : null);
		return answer;
	}

	@Override
	public FeatureTreeNode createGroupedFeature(String id, String name,
			FeatureTreeNode parent) {
		FeatureTreeNode temp = new GroupedFeature(id, name,
				TreeNodeRendererFactory.createGroupedRenderer());

		if (parent != null) {
			parent.add(temp);
		}

		vars.add(temp); //add the feature in the list of variables
		boolean isAdded = fmNodes.add(temp);
		FeatureTreeNode answer = (isAdded ? temp : null);
		return answer;
	}
	
	public RootNode getRoot() {
		return (RootNode)root;
	}

}
