package test.java.org.java8.bpn;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.org.java8.bpn.Apple;
import main.java.org.java8.bpn.ApplePrint;

public class ApplePrintTest {

	static List<Apple> appleList = new ArrayList<>();
	
	@BeforeClass
	private void LoadData() {
		ApplePrint ap = new ApplePrint();
		
		Apple a1 = new Apple();
		a1.setColour("red");
		a1.setWeight(45);
		
		Apple a2 = new Apple();
		a2.setColour("green");
		a2.setWeight(58);
		
		Apple a3 = new Apple();
		a3.setColour("Red");
		a3.setWeight(205);
		
		appleList.add(a1);
		appleList.add(a2);
		appleList.add(a3);
	}

	@Test
	void prettyPrintAppleTest(){
		
		//assertEquals(expected, actual);
	}
}
