package main.java.org.java8.streams.parallel.fjf;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SplitIteratorDemo {

	private static int countWords(Stream<Character> stream) {
		WordCounter wordCounter = stream.reduce(new WordCounter(0, true), WordCounter::accumulate,
				WordCounter::combine);
		return wordCounter.getCounter();
	}

	public static void main(String[] args) {

		final String SENTENCE = " Nel mezzo del cammin di nostra vita " + "mi ritrovai in una selva oscura"
				+ " ché la dritta via era smarrita ";

		Spliterator<Character> spliterator = new WordCounterSplitIterator(SENTENCE);
		Stream<Character> stream = StreamSupport.stream(spliterator, true);
		
		System.out.println("Found " + countWords(stream) + " words");

		//System.out.println("Found " + countWords(stream.parallel()) + " words");
		
		Stream<Character> streams = IntStream.range(0, SENTENCE.length())
				.mapToObj(SENTENCE::charAt);
				System.out.println("Found " + countWords(streams) + " words");
	}

}
