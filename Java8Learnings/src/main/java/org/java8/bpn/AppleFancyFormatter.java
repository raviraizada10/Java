package main.java.org.java8.bpn;

public class AppleFancyFormatter implements AppleFormatter{

	@Override
	public String accept(Apple apple) {
		String characteristic =  apple.getWeight() > 150 ? "heavy" : "light";
		return "A " + characteristic + " " + apple.getColour() + " Apple";
	}

}
