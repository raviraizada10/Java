package main.java.org.java8.streams.collectors;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import main.java.org.java8.streams.Dish;

public class CollectorTest {

	static List<Dish> menu = Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
			new Dish("beef", false, 700, Dish.Type.MEAT), new Dish("chicken", false, 400, Dish.Type.MEAT),
			new Dish("french fries", true, 530, Dish.Type.OTHER), new Dish("rice", true, 350, Dish.Type.OTHER),
			new Dish("season fruit", true, 120, Dish.Type.OTHER), new Dish("pizza", true, 550, Dish.Type.OTHER),
			new Dish("prawns", false, 300, Dish.Type.FISH), new Dish("salmon", false, 450, Dish.Type.FISH));

	public static void main(String[] args) {

		long howManyDishes = menu.stream().collect(Collectors.counting());
		long howManyDishesCount = menu.stream().count();

		System.out.println(howManyDishes);

		Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);

		Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));

		int totalCaloriesReduced = menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));

		int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));

		double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));

		IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));

		System.out.println(menuStatistics);

		String shortMenu = menu.stream().map(Dish::getName).collect(joining());

		String shortMenuCol = menu.stream().map(Dish::getName).collect(joining(", "));

		System.out.println(shortMenu);
		System.out.println(shortMenuCol);

		// Grouping

		Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(groupingBy(dish -> {
			if (dish.getCalories() <= 400)
				return CaloricLevel.DIET;
			else if (dish.getCalories() <= 700)
				return CaloricLevel.NORMAL;
			else
				return CaloricLevel.FAT;
		}));

		System.out.println(dishesByCaloricLevel);

		Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream()
				.collect(groupingBy(Dish::getType, groupingBy(dish -> {
					if (dish.getCalories() <= 400)
						return CaloricLevel.DIET;
					else if (dish.getCalories() <= 700)
						return CaloricLevel.NORMAL;
					else
						return CaloricLevel.FAT;
				})));
		System.out.println(dishesByTypeCaloricLevel);

		Map<Dish.Type, Long> typesCount = menu.stream().collect(groupingBy(Dish::getType, counting()));

		System.out.println(typesCount);

		Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream()
				.collect(groupingBy(Dish::getType, maxBy(Comparator.comparingInt(Dish::getCalories))));
		
		System.out.println(mostCaloricByType);
		
		Map<Dish.Type, Dish> mostCaloricByTypeWoOp = menu.stream()
				.collect(groupingBy(Dish::getType, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
		
		System.out.println(mostCaloricByTypeWoOp);
		
	}

}

enum CaloricLevel {
	DIET, NORMAL, FAT
};
