package com.aptina.test.unit.miscellaneous;

import junit.framework.TestCase;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.fitting.CurveFitter;
import org.apache.commons.math3.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer;

import com.aptina.miscellaneous.GestureCurve;

public class GestureCurveTests extends TestCase {

	private static final CurveFitter rawCurve = new CurveFitter(new LevenbergMarquardtOptimizer());
	private static final CurveFitter origCurve = new CurveFitter(new LevenbergMarquardtOptimizer());

	static {

		for(int i = 0; i < 11; i++){
			rawCurve.addObservedPoint(new WeightedObservedPoint(1.0, i , i));
		}
		for(int i = 0; i < 11 ; i++){
			origCurve.addObservedPoint(new WeightedObservedPoint(1.0, i -5 , i - 5));
		}

	}
	public GestureCurveTests() {
		// TODO Auto-generated constructor stub
	}

	public GestureCurveTests(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void testTranslateToOrigin(){
		
		CurveFitter actualCurve = GestureCurve.translateToOrigin(rawCurve);
		WeightedObservedPoint[] actualPoints = actualCurve.getObservations();
		WeightedObservedPoint[] expectedPoints = origCurve.getObservations();
		assertEquals( expectedPoints.length, actualPoints.length);
		for(int i = 0; i < actualPoints.length; i++){
			assertEquals( expectedPoints[i].getX(), actualPoints[i].getX());
			assertEquals( expectedPoints[i].getY(), actualPoints[i].getY());
		}
		

	}
	
	public void testGetFunction(){
		double[] a1 = {2};
		double[] a2 = {2,1};
		double[] a3 = {2,1,2};
		UnivariateFunction f1 = GestureCurve.getFunction(a1);
		UnivariateFunction f2 = GestureCurve.getFunction(a2);
		UnivariateFunction f3 = GestureCurve.getFunction(a3);
		
		//x = 0
		assertEquals((double)2, f1.value(0));
		assertEquals((double)2, f2.value(0));
		assertEquals((double)2, f3.value(0));
		
		//x = -5
		assertEquals((double)2, f1.value(-5));
		assertEquals((double)-3, f2.value(-5));
		assertEquals((double)47, f3.value(-5));
		
		//x = 5
		assertEquals((double)2, f1.value(5));
		assertEquals((double)7, f2.value(5));
		assertEquals((double)57, f3.value(5));
	}
	
	public void testGetFirstOrder(){
		UnivariateFunction f1 = GestureCurve.getFirstOrder(rawCurve);
		assertEquals((double)50, f1.value(50));
		assertEquals((double)-50, f1.value(-50));
	}

}
