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
package org.simple.nlp.dictionary.entities;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Mar 3, 2014
 */
public class PlaceTestCase {

  @Test
  public void testTransform() throws Exception {
    String s1 = "country: việt nam, vietnam\n" +
                    "country: việt nam >> city: cần thơ, tp.cần thơ, tp. cần thơ, tp cần thơ, tp . cần thơ";
    List<Place> list = new ArrayList<Place>();
    for (String s : s1.split("\n")) {
      Place p = new Place();
      p.tranform(s);
      list.add(p);
    }
    Assert.assertEquals(2, list.size());
    Assert.assertEquals("việt nam", list.get(0).getName());
    Assert.assertEquals("việt nam", list.get(0).getCountry());
    Assert.assertEquals("cần thơ", list.get(1).getName());
    Assert.assertEquals("cần thơ", list.get(1).getCity());
  }
}
