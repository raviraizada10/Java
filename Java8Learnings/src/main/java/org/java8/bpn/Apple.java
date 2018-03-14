package main.java.org.java8.bpn;

public class Apple {

	public Apple(int weight, String colour) {
		super();
		this.weight = weight;
		this.colour = colour;
	}
	
	public Apple() {}
	
	private int weight;
	private String colour;
	
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	
}
