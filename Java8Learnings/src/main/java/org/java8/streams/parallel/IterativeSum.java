package main.java.org.java8.streams.parallel;

import java.util.stream.Stream;

public class IterativeSum {

	public static long sequentialSum(int n) {
		 long startTime = System.currentTimeMillis();
		 long sum = Stream.iterate(1L, i -> i+1)
					 .limit(n)
					 .reduce(0L, Long::sum);
		long endTime = System.currentTimeMillis() - startTime;
		System.out.println(endTime);
		return sum;
	}
	
	public static long parallelSum(int n) {
		long startTime = System.currentTimeMillis();
		long sum = Stream.iterate(1L, i -> i+1)
					 .limit(n)
					 .parallel()
					 .reduce(0L, Long::sum);
		
		long endTime = System.currentTimeMillis() - startTime;
		System.out.println(endTime);
		return sum;
	}
	
	public static void main(String[] args) {
		System.out.println(IterativeSum.sequentialSum(11122215));
		System.out.println(IterativeSum.parallelSum(11122215));
	}
}
