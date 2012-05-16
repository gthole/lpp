package lpp.test;


import static org.junit.Assert.*;
import lpp.LPP;
import lpp.LPPExamples;
import lpp.LPPSolution;

import org.junit.Test;

public class LPPTest {

	// TODO: Fill in meta information beyond ObjectiveFunctionValue (constraint sensitivity, etc.)
	
	@Test
	public void testSolveTransshipmentExample() {
		try {
			LPP lpp = LPPExamples.transshipment();
			LPPSolution solved = lpp.solve();
			assertEquals("ObjectiveFunctionValue", 10043, solved.getObjectiveFunctionValue(), 0.0001);
		} catch (Exception e) {
			fail("Exception thrown in LPP constructor");
		}
	}
	
	@Test
	public void testSolveMinimizeExample() {
		try {
			LPP lpp = LPPExamples.minimizeExample();
			LPPSolution solved = lpp.solve();
			assertEquals("ObjectiveFunctionValue", 2.625, solved.getObjectiveFunctionValue(), 0.0001);
		} catch (Exception e) {
			fail("Exception thrown in LPP constructor");
		}
	}
	
	@Test
	public void testSolveSmallMinimizeExample() {
		try {
			LPP lpp = LPPExamples.smallMinimizeExample();
			LPPSolution solved = lpp.solve();
			assertEquals("ObjectiveFunctionValue", 57, solved.getObjectiveFunctionValue(), 0.0001);
		} catch (Exception e) {
			fail("Exception thrown in LPP constructor");
		}
	}
	
	@Test
	public void testSolveMaximizeExample() {
		try {
			LPP lpp = LPPExamples.maximizeExample();
			LPPSolution solved = lpp.solve();
			assertEquals("ObjectiveFunctionValue", 70, solved.getObjectiveFunctionValue(), 0.0001);
		} catch (Exception e) {
			fail("Exception thrown in LPP constructor");
		}
	}
	
	@Test
	public void testSolveStrictEqualityExample() {
		try {
			LPP lpp = LPPExamples.strictEquality();
			LPPSolution solved = lpp.solve();
			assertEquals("ObjectiveFunctionValue", 55, solved.getObjectiveFunctionValue(), 0.0001);
		} catch (Exception e) {
			fail("Exception thrown in LPP constructor");
		}
	}
}
