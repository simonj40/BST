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

import java.util.ArrayDeque;

/**
 * Implement a Binary Search Tree (BST) data structure. It is not thread-safe
 * 
 * @author Frédéric Fauberteau
 *
 */
public class BinarySearchTree<E extends Comparable<E>> {
  
  private BinarySearchNode<E> root;
  
  /**
   * Add an element in the tree if it is not already present.
   * 
   * @param e
   *          the element to add in the tree
   * @return true if the element has been correctly added and false if it is
   *         already present
   */
  public synchronized boolean add(E e) {
    BinarySearchNode<E> y = null;
    BinarySearchNode<E> x = getRoot();
    while (x != null) {
      y = x;
      if (x.getKey().compareTo(e) == 0) {
        return false;
      } else if (x.getKey().compareTo(e) > 0) {
        x = x.getRightChild();
      } else {
        x = x.getLeftChild();
      }
    }
    
    BinarySearchNode<E> z = new BinarySearchNode<>(e);
    
    if (y == null) {
      setRoot(z);
    } else {
      if (y.getKey().compareTo(e) == 0) {
        return false;
      } else if (y.getKey().compareTo(e) > 0) {
        y.setRightChild(z);
      } else {
        y.setLeftChild(z);
      }
    }
    return true;
  }
  
  public void randomlyAdd(int count){
	  
	  for(int i=0; i<count;i++){
		  
		  
		  
		  
		  
		  
	  }
	  
	  
	  
  }
  
  
  private BinarySearchNode<E> getRoot() {
    return root;
  }
  
  private void setRoot(BinarySearchNode<E> node) {
    root = node;
  }
  
  
  public String toDOT(String name) {
    ArrayDeque<BinarySearchNode<E>> queue = new ArrayDeque<>();
    StringBuilder sb = new StringBuilder();
    if (getRoot() != null) {
      sb.append("graph ").append(name).append(" {\n");
      queue.offerFirst(getRoot());
      while (!queue.isEmpty()) {
        BinarySearchNode<E> node = queue.pollLast();
        if (node.hasRightChild()) {
          BinarySearchNode<E> right = node.getRightChild();
          sb.append(node).append(" -- ").append(right).append("\n");
          queue.offerFirst(right);
        }
        if (node.hasLeftChild()) {
          BinarySearchNode<E> left = node.getLeftChild();
          sb.append(node).append(" -- ").append(left).append(";\n");
          queue.offerFirst(left);
        }
      }
      sb.append('}');
    }
    return sb.toString();
  }
  
  private static class BinarySearchNode<E extends Comparable<E>> {
    
    private final E key;
 
    private BinarySearchNode<E> left;
    
    private BinarySearchNode<E> right;
    
    private BinarySearchNode(E key) {
      this.key = key;
    }
    
    private E getKey() {
      return key;
    }
    
    private BinarySearchNode<E> getLeftChild() {
      return left;
    }
    
    private BinarySearchNode<E> getRightChild() {
      return right;
    }
    
    private boolean hasLeftChild() {
      return getLeftChild() == null ? false : true;
    }
    
    private boolean hasRightChild() {
      return getRightChild() == null ? false : true;
    }
   
    private void setLeftChild(BinarySearchNode<E> node) {
      left = node;
    }
    
    private void setRightChild(BinarySearchNode<E> node) {
      right = node;
    }
    
    @Override
    public String toString() {
      return getKey().toString();
    }
    
  }

}
