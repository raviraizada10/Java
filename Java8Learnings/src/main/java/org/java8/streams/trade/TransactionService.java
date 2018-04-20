package main.java.org.java8.streams.trade;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TransactionService
{

	//Find all transactions in 2011 and sort by value (small to	high)
	public static List<Transaction> transactinsByYear(List<Transaction> transactions){
		
		List<Transaction> tr2011 = transactions.stream()
											   .filter(transaction -> transaction.getYear() == 2011)
											   .sorted(comparing(Transaction::getValue))
											   .limit(1)
											   .collect(toList());
		return tr2011;
	} 
	
	//What are all the unique cities where the traders work?
	public static List<String> uniqueCities(List<Transaction> transactions){
		List<String> cities = transactions.stream()
										  .map(transaction -> transaction.getTrader().getCity())
										  .distinct()
										  .collect(toList());
		return cities;
	}
	
	//Find all traders from Cambridge and sort them by name
	public static List<Trader> cityTraderSorted(List<Transaction> transactions){
		List<Trader> traders = transactions.stream()
										   .map(Transaction::getTrader)
										   .filter(trader -> trader.getCity().equals("Cambridge"))
										   .distinct()
										   .sorted(comparing(Trader::getName))
										   .collect(toList());
		return traders;
	}
	
	//Return a string of all traders’ names sorted alphabetically
	public static String traderSorted(List<Transaction> transactions){
		String sortedTraders = transactions.stream()
											     .map(transaction -> transaction.getTrader().getName())
											     .distinct()
											     .sorted()
											     .reduce("", (n1,n2) -> n1 + " " + n2);
		return sortedTraders;
												 
	}
	
	public static String traderSortedCollect(List<Transaction> transactions){
	String traderStr =
			transactions.stream()
			.map(transaction -> transaction.getTrader().getName())
			.distinct()
			.sorted()
			.collect(joining());
	
		return traderStr;
	}
	
	//Are any traders based in Milan?
	public static Boolean cityUsers(List<Transaction> transactions) {
		Boolean milanTraders = transactions.stream()
										   .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));
		return milanTraders;
	} 
	
	//Print all transactions’ values from the traders living in Cambridge
	public static void cambridgeTrancsactions(List<Transaction> transactions){
		transactions.stream()
					.filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
					.map(Transaction::getValue)
					.forEach(System.out::println);
	}
	
	//What’s the highest value of all the transactions?
	public static Optional<Integer> getHighestTransaction(List<Transaction> transactions){
		Optional<Integer> highestInteger = transactions.stream()
													   .map(Transaction::getValue)
													   .reduce(Integer::max);
		return highestInteger;
	}
	
	//Find the transaction with the smallest value
	public static Optional<Integer> getSmallestTransaction(List<Transaction> transactions){
		Optional<Integer> smallestInteger = transactions.stream()
													   .map(Transaction::getValue)
													   .reduce(Integer::min);
		return smallestInteger;
	}
	
	public static void main(String[] args)
	{
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario","Milan");
		Trader alan = new Trader("Alan","Cambridge");
		Trader brian = new Trader("Brian","Cambridge");
		List<Transaction> transactions = Arrays.asList(
		new Transaction(brian, 2011, 300),
		new Transaction(raoul, 2012, 1000),
		new Transaction(raoul, 2011, 400),
		new Transaction(mario, 2012, 710),
		new Transaction(mario, 2012, 700),
		new Transaction(alan, 2012, 950)
		);
		
		System.out.println(TransactionService.transactinsByYear(transactions));
		
		/*System.out.println(TransactionService.uniqueCities(transactions));
		
		System.out.println(TransactionService.cityTraderSorted(transactions));
		
		System.out.println(TransactionService.traderSorted(transactions));
		
		System.out.println(TransactionService.traderSortedCollect(transactions));
		
		System.out.println(TransactionService.cityUsers(transactions));
		
		TransactionService.cambridgeTrancsactions(transactions);
		
		System.out.println(TransactionService.getHighestTransaction(transactions));
		
		System.out.println(TransactionService.getSmallestTransaction(transactions));*/
	}

}
