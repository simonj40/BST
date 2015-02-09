/**
 * 
 */
package application;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;
import java.util.stream.LongStream;

import org.fauberteau.rbtree.BinarySearchTree;
import org.fauberteau.rbtree.RandomWordGenerator;

/**
 * @author Simon
 *
 */
public class BSTAdder implements Callable<Duration> {

	private BinarySearchTree<String> tree;
	private RandomWordGenerator generator;

	public BSTAdder(RandomWordGenerator generator, BinarySearchTree<String> tree) {

		this.tree = tree;
		this.generator = generator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Duration call() throws Exception {

		
		List<Long> times = new ArrayList<Long>();

		for (String s : generator) {
			
			long start = System.nanoTime();
			tree.add(s);
			long time = System.nanoTime() - start;
			times.add(time);
		}
	
		OptionalDouble avg = times.stream().mapToLong(b -> b.longValue())
				.average();
		return Duration.ofNanos((long) avg.getAsDouble());
	}

}
