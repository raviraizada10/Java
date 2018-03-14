package main.java.org.java8.streams;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamsTest {

	static List<Dish> menu = Arrays.asList(
			new Dish("pork", false, 800, Dish.Type.MEAT),
			new Dish("beef", false, 700, Dish.Type.MEAT),
			new Dish("chicken", false, 400, Dish.Type.MEAT),
			new Dish("french fries", true, 530, Dish.Type.OTHER),
			new Dish("rice", true, 350, Dish.Type.OTHER),
			new Dish("season fruit", true, 120, Dish.Type.OTHER),
			new Dish("pizza", true, 550, Dish.Type.OTHER),
			new Dish("prawns", false, 300, Dish.Type.FISH),
			new Dish("salmon", false, 450, Dish.Type.FISH) );
			
	public static void main(String[] args) {
		
		List<String> threeHighCalorieDishes = 
				menu.stream()
					.filter(d->d.getCalories() > 300)
					.map(Dish::getName)
					.limit(3)
					.collect(toList());
		
		List<Dish> vegDishes = menu.stream()
									 .filter(Dish::isVegetarian)
									 .collect(toList());
		
		List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
		numbers.stream()
				.filter(i -> i % 2 == 0)
				.distinct()
				.forEach(System.out::println);

		
		List<Dish> dishes = menu.stream()
				.filter(d -> d.getCalories() > 300)
				.skip(2)
				.collect(toList());
		
		List<Dish> dishesLim =
				menu.stream()
				.filter(d -> d.getType() == Dish.Type.MEAT)
				.limit(2)
				.collect(toList());
		
		List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
		List<Integer> wordLengths = words.stream()
										 .map(String::length)
										 .collect(toList());
		
		List<Integer> dishNameLengths = menu.stream()
											.map(Dish::getName)
											.map(String::length)
											.collect(toList());
		
		String[] arrayOfWords = {"Goodbye", "World"};
		Stream<String> streamOfwords = Arrays.stream(arrayOfWords);
		
		List<String> uniqueCharacters = words.stream()
													.map(w -> w.split(""))
													.flatMap(Arrays::stream)
													.distinct()
													.collect(Collectors.toList());
		
		List<Integer> numbersSq = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> squares = numbersSq.stream()
									   .map(n -> n * n)
									   .collect(toList());
		
		System.out.println(squares);
		
		
		List<Integer> numbers1 = Arrays.asList(1, 2, 3);
		List<Integer> numbers2 = Arrays.asList(3, 4);
		List<int[]> pairs = numbers1.stream()
									.flatMap(i -> numbers2.stream().map(j -> new int[]{i,j}))
									.collect(toList());
		
		List<int[]> pairsDivisibleBy3 = numbers1.stream()
				.flatMap(i -> numbers2.stream()
						.filter(j -> (i+j) % 3 ==0)
						.map(j -> new int[] {i,j}))
				.collect(toList());
		
		for(int i =0 ; i< pairs.size() ; i++){
			System.out.print(pairs.get(i)[0] + ","+pairs.get(i)[1] +"\t" ); 
		}
		
		System.out.println();
		
		for(int i =0 ; i< pairsDivisibleBy3.size() ; i++){
			System.out.print(pairsDivisibleBy3.get(i)[0] + ","+pairsDivisibleBy3.get(i)[1] +"\t" ); 
		}
		
		if(menu.stream().anyMatch(Dish::isVegetarian)){
			System.out.println("The menu is (somewhat) vegetarian friendly!!");
		}
		
		boolean isHealthy = menu.stream()
				.allMatch(d -> d.getCalories() < 1000);
		
		boolean isHealthyN = menu.stream()
				.noneMatch(d -> d.getCalories() >= 1000);
		
		Optional<Dish> dish = menu.stream()
				.filter(Dish::isVegetarian)
				.findAny();
		
		List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
		Optional<Integer> firstSquareDivisibleByThree =
		someNumbers.stream()
				   .map(x -> x * x)
				   .filter(x -> (x % 3 == 0))
				   .findFirst();
		
		int sum = someNumbers.stream()
							 .reduce(0, (a, b) -> a + b);
		
		int sumM = someNumbers.stream()
							  .reduce(0, Integer::sum);
		
		Optional<Integer> sumOP = someNumbers.stream()
											 .reduce(Integer::sum);
		
		Optional<Integer> max = someNumbers.stream()
										   .reduce(Integer::max);
		
		//Specialized Stream
		
		int calories = menu.stream()
						   .mapToInt(Dish::getCalories)
						   .sum();
		
		//boxing to nonspecialized stream
		
		IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
		Stream<Integer> stream = intStream.boxed();
		
		OptionalInt maxCalories = menu.stream()
				.mapToInt(Dish::getCalories)
				.max();
		int maxCal = maxCalories.orElse(1);
		
		IntStream evenNumbers = IntStream.rangeClosed(1, 100)
										 .filter(d -> d % 2 == 0);
		System.out.println(evenNumbers.count());
		
		//fibonacci
		Stream.iterate(new int[] {0,1}, t ->  new int[]{t[1], t[0]+t[1]})
			  .limit(10)
			  .forEach(t -> System.out.println("(" + t[0] + "," + t[1] +")"));
		
		Stream.generate(Math::random)
			  .limit(5)
			  .forEach(System.out::println);
	
		IntStream intStream1 = new Random().ints(1,100000, 999999);
		intStream1.forEach(System.out::println);			
		  
		
	}

}
 