/*
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.simple.nlp.dictionary.utils;

import java.io.PrintStream;
import java.util.Comparator;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 21, 2014
 * 
 * Represent a Min-Heap tree which allow to insert an T object greater than root object of this tree
 */
public class HeapTree<T> {

  private int size;
  
  private int maxSize;
  
  private T[] nodes;
  
  private Comparator<T> comparator;
  
  public HeapTree(int maxSize, Comparator<T> comparator) {
    this.maxSize = maxSize;
    this.comparator = comparator;
    this.clear();
  }
  
  /**
   * Insert an object into this tree. If tree's size smaller than tree's max size then inserting into tree.
   * Otherwise, insert only when new object greater than root node of this tree. 
   * @param newNode
   * @return null if inserted into tree
   * @return an object this is inserted node  if it is not inserted. Or root of this tree if it is inserted.
   */
  public T insert(T newNode) {
    if (size < maxSize) {
      nodes[size] = newNode;
      shiftUp(size);
      size++;
      return null;
    } else {
      if (comparator.compare(newNode, nodes[0]) < 0) return newNode;
      T replaceNode = nodes[0];
      nodes[0] = newNode;
      shiftDown(0);
      return replaceNode;
    }
  }
  
  /**
   * Looks at the object at the root of this tree without removing it from the tree. 
   * @return the object at the root of this tree
   */
  public T peek() {
    return size == 0 ? null : nodes[0];
  }
  /**
   * Removes the object at the root of this tree and returns that object as the value of this function
   * @return The object at the root of this tree.
   */
  public T pop() {
    if (size == 0) return null;
    T tmp = nodes[0];
    //Move largest node to root of this tree.
    nodes[0] = nodes[--size];
    //Shift down new root node.
    shiftDown(0);
    return tmp;
  }
  
  public int size() {
    return size;
  }
  
  public int maxSize() {
    return maxSize;
  }

  /**
   * Find the index of left child
   * @param pos The index of node
   * @return The index of left child
   */
  public int leftChild(int pos) {
    return 2 * pos + 1;
  }
  
  /**
   * Find the index of right child
   * @param pos The index of node
   * @return The index of right child
   */
  public int rightChild(int pos) {
    return 2 * pos + 2;
  }
  
  /**
   * Find the index of parent's node
   * @param pos The index of node
   * @return The index of parent's node
   */
  public int parent(int pos) {
    return (pos - 1) / 2;
  }
  
  /**
   * To check whether a node is a leaf or internal node
   * @param pos The index of node
   * @return true is leaf
   */
  public boolean isLeaf(int pos) {
    return (pos >= size / 2) && (pos < size);
  }
  
  public boolean isFull() {
    return size == maxSize;
  }
  
  private void swap(int pos1, int pos2) {
    T temp= nodes[pos1];
    nodes[pos1] = nodes[pos2];
    nodes[pos2] = temp;
  }
  
  /**
   * This function will shift down a node until it is a leaf node or it is smaller than its both right and left child
   * @param pos
   */
  private void shiftDown(int pos) {
    if (pos < 0 || pos >= size) return;
    while (!this.isLeaf(pos)) {
      int j = leftChild(pos);
      //compare left child and right child
      if (j < size - 1 && comparator.compare(nodes[j], nodes[j + 1]) > 0) j++;
      //if node smaller than its child then doing nothing
      if (comparator.compare(nodes[pos], nodes[j]) < 0) return;
      //else swap node with its child
      this.swap(pos, j);
      //recursive
      pos = j;
    }
  }
  
  /**
   * This function will shift up a node until it is greater than its parent
   * @param pos
   */
  private void shiftUp(int pos) {
    while (pos > 0) {
      int parent = this.parent(pos);
      //if node greater than its parent then doing nothing
      if (comparator.compare(nodes[pos], nodes[parent]) > 0) return;
      //else swap this node with its parent
      swap(pos, parent);
      //recursive
      pos = parent;
    }
  }
  
  public T[] toArray(T[] array) {
    System.arraycopy(nodes, 0, array, 0, size);
    return array;
  }
  
  /**
   * Cleanup and renew the tree
   */
  public void clear() {
    nodes = (T[]) new Object[maxSize];
    size = 0;
  }
  
  public void printNodes(PrintStream out) {
    for (T node : nodes) {
      out.print(node);
      out.print(" ");
    }
  }
}
