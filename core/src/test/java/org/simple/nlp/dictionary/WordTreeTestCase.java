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

import org.junit.Test;
import org.simple.nlp.dictionary.entities.SemanticEntity;
import org.simple.nlp.dictionary.entities.SemanticWord;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 18, 2014
 */
public class WordTreeTestCase extends TestCase {

    @Test
    public void testMatch() {
        SemanticEntity e1 = new SemanticWord();
        e1.setName("cong hoa");
        SemanticEntity e2 = new SemanticWord();
        e2.setName("xa hoi");
        SemanticEntity e3 = new SemanticWord();
        e3.setName("cong hoa xa hoi");
        SemanticEntity e4 = new SemanticWord();
        e4.setName("cong hoa xa hoi chu nghia Viet Nam");
        WordTree root = new WordTree(null);
        root.addEntity(new String[] {"cong", "hoa"}, e1);
        root.addEntity(new String[] {"xa", "hoi"}, e2);
        root.addEntity(new String[] {"cong", "hoa", "xa", "hoi"}, e3);
        root.addEntity(new String[] {"cong", "hoa", "xa", "hoi", "chu", "nghia"}, e4);
        root.dump(System.out, "  ");

        assertEquals(e1, root.match(new String[] { "cong", "hoa" }).getEntities()[0]);
        assertEquals(e2, root.match(new String[] {"xa", "hoi"}).getEntities()[0]);
        assertEquals(e3, root.match(new String[] {"cong", "hoa", "xa", "hoi"}).getEntities()[0]);
        assertEquals(e4, root.match(new String[] {"cong", "hoa", "xa", "hoi", "chu", "nghia"}).getEntities()[0]);
    }
}
