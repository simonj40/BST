/* Copyright (c) 2015, Frédéric Fauberteau
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.fauberteau.rbtree;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	private final static int MAX_THREADS = 10000;
	private  final static ExecutorService service = Executors
			.newFixedThreadPool(MAX_THREADS);

	private static int THREADS_NUM = 2000;
	private static int NODE_INSERTED = 1000000;
	private static int ADD_PER_THREADS = NODE_INSERTED/THREADS_NUM;

	public static BinarySearchTree<Integer> rbtree = new BinarySearchTree<>();

	public static final void main(String[] args) throws IOException {
		
		
		double AVG_ADDING_TIME = 0;
		double AVG_TIME_THREAD = 0;
		ArrayList<Future<Long>> futureList = new ArrayList<>();
		
		long start = System.currentTimeMillis();

		
		RandomGenerator generator = new RandomGenerator(ADD_PER_THREADS);
		
		for (int i = 0; i < THREADS_NUM; i++) {
			Future<Long> future = service.submit(generator);
			futureList.add(future);
			
		}
		
		
		for (Future<Long> future : futureList) {
			try {
				AVG_TIME_THREAD += future.get().longValue();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		service.shutdownNow();
		
		long time = System.currentTimeMillis() - start;
		
		AVG_TIME_THREAD = AVG_TIME_THREAD/THREADS_NUM;
		
		
		
		AVG_TIME_THREAD = AVG_TIME_THREAD/THREADS_NUM;
		
		AVG_ADDING_TIME = rbtree.getTime();
		
		System.out.println();
		System.out.println("EXECUTION TIME : "+time);
		System.out.println("NUMBER OF THREADS : " + THREADS_NUM);
		System.out.println("ADD PER THREADS : " + ADD_PER_THREADS);
		System.out.println("AVERAGE ADDING TIME : " + AVG_ADDING_TIME);
		System.out.println("AVERAGE TIME PER THREADS : " + AVG_TIME_THREAD);
		
		/*
		String name = "rbtree";
		PrintWriter writer = new PrintWriter(name + ".dot");
		writer.println(rbtree.toDOT(name));
		writer.close();
		ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/dot", "-Tpdf", "-o", name + ".pdf ", name + ".dot");
		builder.start();
		*/
		
		
	}

	public static class RandomGenerator implements Callable<Long> {

		private int count;

		public RandomGenerator(int count) {
			this.count = count;
		}

		
		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Long call(){
			long start = System.currentTimeMillis();
			
			Random rand = new Random();

			for (int i = 0; i < count; i++) {
				rbtree.add(rand.nextInt());
			}
			
			return System.currentTimeMillis() - start;
		}

	}

}
