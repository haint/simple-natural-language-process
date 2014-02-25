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

import java.util.Comparator;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 25, 2014
 */
public class HeapTreeTestCase extends TestCase {
  
  public Comparator<Integer> comparator = new Comparator<Integer>() {
    public int compare(Integer o1, Integer o2) {
      return o1.compareTo(o2);
    }
  };
  
  @Test
  public void testInsert() {
    HeapTree<Integer> tree = new HeapTree<Integer>(3, comparator);
    assertNull(tree.insert(10)); assertEquals(1, tree.size());
    assertNull(tree.insert(5)); assertEquals(2, tree.size());
    assertNull(tree.insert(8)); assertEquals(3, tree.size());
    
    assertEquals(1, tree.insert(1).intValue());
    assertEquals(5, tree.insert(6).intValue());
    assertEquals(6, tree.peek().intValue());
    
    assertEquals(3, tree.size());
    assertTrue(tree.isFull());
  }
  
  @Test
  public void testPopAndPeek() {
    HeapTree<Integer> tree = new HeapTree<Integer>(3, comparator);
    assertNull(tree.insert(10));
    assertNull(tree.insert(5));
    assertNull(tree.insert(8));
    
    assertEquals(5, tree.peek().intValue());
    assertEquals(5, tree.pop().intValue());
    assertEquals(8, tree.pop().intValue());
    assertEquals(10, tree.pop().intValue());
  }
}
