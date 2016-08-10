package splar.core.fm.personalization;

import splar.core.fm.FeatureTreeNode;
import splar.core.fm.RootNode;

public interface IPersonalFeatureModel {

	public void loadFMfromFeatureIDEXML(String path);

	public RootNode createRootNode(String name, String id);
	public FeatureTreeNode createOptionalFeature(String name, String id, FeatureTreeNode parent);
	public FeatureTreeNode createMandatoryFeature(String name, String id, FeatureTreeNode parent);
	public FeatureTreeNode createExclusiveOrFeature(String name, String id, FeatureTreeNode parent);
	public FeatureTreeNode createInclusiveOrFeature(String name, String id, FeatureTreeNode parent);
	public FeatureTreeNode createGroupedFeature(String name, String id, FeatureTreeNode parent);
}
