package main.java.org.java8.streams.trade;

public class Trader
{
	private final String name;
	private final String city;

	public Trader(String n, String c)
	{
		this.name = n;
		this.city = c;
	}

	public String getName()
	{
		return this.name;
	}

	public String getCity()
	{
		return this.city;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trader other = (Trader) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String toString()
	{
		return "Trader:" + this.name + " in " + this.city;
	}
}
