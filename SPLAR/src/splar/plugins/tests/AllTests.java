package splar.plugins.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import splar.plugins.configuration.tests.bdd.javabdd.BDDConfigurationEngineTest;
import splar.plugins.configuration.tests.sat.sat4j.SATConfigurationEngineTest;
import splar.plugins.reasoners.tests.bdd.javabdd.ReasoningWithBDDTest;
import splar.plugins.reasoners.tests.sat.sat4j.ReasoningWithSATTest;

public class AllTests extends TestSuite{
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for tests");
		//$JUnit-BEGIN$
		suite.addTest(ReasoningWithSATTest.class);
		suite.addTest(ReasoningWithBDDTest.class);
		suite.addTest(SATConfigurationEngineTest.class);
		suite.addTest(BDDConfigurationEngineTest.class);
		//$JUnit-END$
		return suite;
	}

}
