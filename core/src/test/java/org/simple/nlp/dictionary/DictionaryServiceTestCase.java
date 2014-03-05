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

import junit.framework.Assert;

import org.junit.Test;
import org.simple.nlp.dictionary.db.DictionaryDB;
import org.simple.nlp.dictionary.entities.Place;
import org.simple.nlp.dictionary.entities.Product;
import org.simple.nlp.dictionary.entities.SemanticWord;
import org.simple.nlp.dictionary.index.SearchEngine;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 28, 2014
 */
public class DictionaryServiceTestCase {

  @Test
  public void testWord() throws Exception {
    DictionaryDB db = DictionaryService.getInstance().getDatabase();
    Assert.assertEquals(49630, db.count());
    Assert.assertEquals(29594, db.count(SemanticWord.class));
    Assert.assertEquals(18318, db.count(Place.class));
    Assert.assertEquals(1718, db.count(Product.class));
    
    SearchEngine searcher = new SearchEngine("target/db");
    Assert.assertEquals(18318, searcher.query("country@place:\"việt nam\"", 100).getTotalHits());
  }
}
