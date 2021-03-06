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
import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simple.nlp.dictionary.db.DictionaryDB;
import org.simple.nlp.dictionary.entities.Place;
import org.simple.nlp.dictionary.entities.Product;
import org.simple.nlp.dictionary.entities.SemanticEntity;
import org.simple.nlp.dictionary.entities.SemanticWord;
import org.simple.nlp.dictionary.index.SearchEngine;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 21, 2014
 */
public class DictionaryDBTestCase extends TestCase {

  private DictionaryDB db;

  @Before
  public void setUp() throws IOException {
    db = new DictionaryDB("target/db-" + System.currentTimeMillis(), true);
  }

  @After
  public void tearDow() throws IOException {
    db.close();
  }
  
  @Test
  public void testGet() throws Exception {
    String s = "noun: bay\n" + "noun: bay bướm\n" + "noun: bay nhảy";
    for (String src : s.split("\n")) {
      SemanticWord word = new SemanticWord();
      word.tranform(src);
      db.save(word);
    }

    db.commit();
    assertEquals(3, db.count(SemanticWord.class));
  }
  
  @Test
  public void testUpdate() throws Exception {
    String s1 = "maker:nokia >> model: n97\n" + "maker:samsung >> model: onima hd";

    for (String src : s1.split("\n")) {
      Product prd = new Product();
      prd.tranform(src);
      db.update(prd.getUUID(), prd);
    }
    db.commit();
    
    assertEquals(2, db.count());
    
    Iterator<SemanticEntity> iterator = db.getSemanticEntities();
    
    Product p1 = (Product) iterator.next();
    assertEquals("n97", p1.getName());
    assertEquals("n97", p1.getModel());
    assertEquals("nokia", p1.getMaker());
    
    Product p2 = (Product) iterator.next();
    assertEquals("onima hd", p2.getName());
    assertEquals("onima hd", p2.getModel());
    assertEquals("samsung", p2.getMaker());
    
    p2.setModel("onima hd 2");
    db.update(p2.getUUID(), p2);
    db.commit();

    assertEquals(2, db.count());
    p2 = (Product) db.getEntity(p2.getUUID());
    assertEquals("onima hd 2", p2.getModel());
  }
  
  @Test
  public void testDelete() throws Exception {
    String s1 = "maker:nokia >> model: n97\n" + "maker:samsung >> model: onima hd";
    for (String src : s1.split("\n")) {
      Product prd = new Product();
      prd.tranform(src);
      db.update(prd.getUUID(), prd);
    }
    assertEquals(2, db.count());

    Iterator<SemanticEntity> iterator = db.getSemanticEntities();
    Product nokia = (Product) iterator.next();
    db.remove(nokia);
    db.commit();
    
    assertEquals(1, db.count());
    assertEquals(1, db.count(Product.class));
    SearchEngine searcher = new SearchEngine(db.getPath());
    assertEquals(0, searcher.query("maker@product: nokia", 100).getTotalHits());
    assertEquals(1, searcher.query("maker@product: samsung", 100).getTotalHits());
  }
  
  @Test
  public void testCount() throws Exception {
    String s1 = "maker:nokia >> model: n97\n" + "maker:samsung >> model: onima hd\n" + "maker:iphone >> model: 3gs\n"
        + "maker:blackberry >> model: bold";
    for (String src : s1.split("\n")) {
      Product prd = new Product();
      prd.tranform(src);
      db.save(prd);
    }
    assertEquals(4, db.count(Product.class));

    //
    String s2 = "country: vietnam >> city: hanoi >> district: haibatrung >> street:truong dinh, truongdinh\n"
        + "country: vietnam >> city: hanoi >> place: quoctugiam\n";

    for (String src : s2.split("\n")) {
      Place place = new Place();
      place.tranform(src);
      db.save(place);
    }
    assertEquals(2, db.count(Place.class));
    
    //
    String s3 = "noun: bay\n" + "noun: bay bướm\n" + "noun: bay nhảy";
    
    for (String src : s3.split("\n")) {
      SemanticWord word = new SemanticWord();
      word.tranform(src);
      db.save(word);
    }
    assertEquals(3, db.count(SemanticWord.class));
    
    //
    assertEquals(9, db.count());
  }

  @Test
  public void testIndexAndSearch() throws Exception {
    //
    String s2 = "country: vietnam >> city: hanoi >> district: haibatrung >> street:truong dinh, truongdinh\n"
        + "country: vietnam >> city: hanoi >> place: quoctugiam\n";

    for (String src : s2.split("\n")) {
      Place place = new Place();
      place.tranform(src);
      db.save(place);
    }
    assertEquals(2, db.count(Place.class));
    
    //
    String s3 = "noun: bay\n" + "noun: bay bướm\n" + "noun: bay nhảy";
    
    for (String src : s3.split("\n")) {
      SemanticWord word = new SemanticWord();
      word.tranform(src);
      db.save(word);
    }
    assertEquals(3, db.count(SemanticWord.class));
    
    db.commit();

    SearchEngine searcher = new SearchEngine(db.getPath());
    assertEquals(1, searcher.query("name: quoctugiam", 100).getTotalHits());
    assertEquals(1, searcher.query("name: bay", 100).getTotalHits());
    assertEquals(3, searcher.query("bay*", 100).getTotalHits());
    assertEquals(2, searcher.query("city@place: hanoi", 100).getTotalHits());
  }
}
