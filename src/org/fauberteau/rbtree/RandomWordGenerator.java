package org.fauberteau.rbtree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomWordGenerator implements Iterable<String> {
	private int count;
	private int number_words;
	Random rand = new Random();
	int MAX = 10;
	int MIN = 1;
	private int cursor;

	private final List<String> String_list = new ArrayList<String>();

	public RandomWordGenerator(int number_words) {
		this.number_words = number_words;
	}

	public String next() {
		System.out.println("getRandomWord() : " + getRandomWord());

		return getRandomWord();
	}

	private String getRandomWord() {

		int size = rand.nextInt(MAX - MIN + 1) + MIN;
		char[] word = new char[size];
		for (int i = 0; i < size; i++) {
			int c = Math.abs(rand.nextInt()) % 26 + 'a';
			word[i] = Character.toChars(c)[0];
		}

		return new String(word);
	}

	// private String ;

	@Override
	public Iterator<String> iterator() {

		return new Iterator<String>() {

			private int counter = 0;

			@Override
			public boolean hasNext() {
				return counter < number_words ? true : false;
			}

			@Override
			public String next() {
				if (counter < number_words) {
					counter++;
					return getRandomWord();

				} else {
					return null;
				}
			}

		};
	}
}
