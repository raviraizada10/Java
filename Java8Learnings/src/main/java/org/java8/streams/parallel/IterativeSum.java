package main.java.org.java8.streams.parallel;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import main.java.org.java8.streams.parallel.fjf.ForkJoinSumCalculator;

public class IterativeSum {

	public static long sequentialSum(long n) {
		long sum = Stream.iterate(1L, i -> i + 1).limit(n).reduce(0L, Long::sum);
		return sum;
	}

	public static long parallelSum(long n) {
		long sum = Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(0L, Long::sum);
		return sum;
	}

	public static long measureSumPerf(Function<Long, Long> adder, long n) {
		long fastest = Long.MAX_VALUE;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			long sum = adder.apply(n);
			long duration = (System.nanoTime() - start) / 1_000_000;
			System.out.println("Result: " + sum);
			if (duration < fastest)
				fastest = duration;
		}
		return fastest;
	}
	
	public static long rangedSum(long n) {
		return LongStream
				.rangeClosed(1, n)
				.reduce(0L, Long::sum);
	}
	
	public static long parallelRangedSum(long n) {
		return LongStream.rangeClosed(1, n)
		.parallel()
		.reduce(0L, Long::sum);
		}
	
	public static long sideEffectSum(long n) {
		Accumulator accumulator = new Accumulator();
								LongStream
								.rangeClosed(1, n)
								.forEach(accumulator::add);
		return accumulator.total;
	}
	
	public static long sideEffectParallelSum(long n) {
		Accumulator accumulator = new Accumulator();
								  LongStream
								  .rangeClosed(1, n)
								  .parallel()
								  .forEach(accumulator::add);
		return accumulator.total;
		}

	public static void main(String[] args) {
		/*System.out.println("Sequential sum done in: " +
				measureSumPerf(IterativeSum::sequentialSum, 10_000_000) + " msecs");
		
		System.out.println("Parallel sum done in: " +
				measureSumPerf(IterativeSum::parallelSum, 10_000_000) + " msecs" );*/
		
		System.out.println("Ranged sum done in: " +
				measureSumPerf(IterativeSum::rangedSum, 10_000_000) + " msecs" );
		
		System.out.println("Parallel Ranged sum done in: " +
				measureSumPerf(IterativeSum::parallelRangedSum, 10_000_000) + " msecs" );
		
		System.out.println("ForkJoin sum done in: " + measureSumPerf(
				ForkJoinSumCalculator::forkJoinSum, 10_000_000) + " msecs" );
	}
}
