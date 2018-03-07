package main.java.org.java8.bpn;

import java.util.ArrayList;
import java.util.List;

public class ApplePrint {
	
	static List<Apple> appleList = new ArrayList<>();
	
	public void prettyPrintApple(List<Apple> inventory, AppleFormatter formatter) {
		
		for(Apple apple:inventory) {
			String output = formatter.accept(apple);
			System.out.println(output);
		}
	}

	public static void main(String[] args) {
		
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
		
		ap.prettyPrintApple(appleList, new AppleSimpleFormatter());

	}

}
