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
package org.simple.nlp.dictionary;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simple.nlp.dictionary.db.DictionaryDB;
import org.simple.nlp.dictionary.entities.Place;
import org.simple.nlp.dictionary.entities.Product;
import org.simple.nlp.dictionary.entities.SemanticWord;
import org.simple.nlp.dictionary.index.SearchEngine;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 21, 2014
 */
public class DictionaryDBTestCase extends TestCase {

  private DictionaryDB db;
  
  @Before
  public void setUp() throws IOException {
    db = new DictionaryDB("target/db-" + System.currentTimeMillis() , true);
  }
  
  @After
  public void tearDow() throws IOException {
    db.close();
  }
  
  @Test
  public void test() throws Exception {
    String s1 = "maker:nokia >> model: n97\n" +
        "maker:samsung >> model: onima hd\n" +
        "maker:iphone >> model: 3gs\n" +
        "maker:blackberry >> model: bold";

    for (String src : s1.split("\n")) {
      Product prd = new Product();
      prd.tranform(src);
      db.save(prd);
    }
    
    String s2 = 
        "country: vietnam >> city: hanoi >> district: haibatrung >> street:truong dinh, truongdinh\n" +
        "country: vietnam >> city: hanoi >> place: quoctugiam\n";
    
    for (String src : s2.split("\n")) {
      Place place = new Place();
      place.tranform(src);
      db.save(place);
    }
    
    String s3 = 
        "noun: bay\n" +
        "noun: bay bướm\n" +
        "noun: bay nhảy";
    
    for (String src : s3.split("\n")) {
      SemanticWord word = new SemanticWord();
      word.tranform(src);
      db.save(word);
    }
    
    db.commit();
    
    SearchEngine searcher = new SearchEngine(db.getPath() + "/index");
    assertEquals(1, searcher.query("name: quoctugiam", 100).getTotalHits());
    assertEquals(3, searcher.query("name: bay", 100).getTotalHits());
    assertEquals(2, searcher.query("city@place: hanoi", 100).getTotalHits());
    assertEquals(9, db.count());
  }
}
