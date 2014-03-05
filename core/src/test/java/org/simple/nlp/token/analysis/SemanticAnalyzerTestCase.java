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
package org.simple.nlp.token.analysis;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.simple.nlp.dictionary.WordTree;
import org.simple.nlp.dictionary.entities.SemanticWord;
import org.simple.nlp.token.Token;
import org.simple.nlp.token.TokenParser;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Mar 3, 2014
 */
public class SemanticAnalyzerTestCase {

  @Test
  public void test() throws Exception {
    WordTree root = new WordTree(new String[] {});
    
    String noun = "noun:Đất nước\n" +
    		"noun:tôi\n" +
    		"noun:thon thả\n" +
    		"noun:giọt đàn bầu";
    for (String n : noun.split("\n")) {
      SemanticWord w =new SemanticWord();
      w.tranform(n);
      root.addEntity(w);
    }
    
    String s = "Đất nước<i> tôi thon thả</i> giọt đàn bầu";
    TokenParser parser = new TokenParser();
    List<Token> tokens = parser.parse(s);
    Assert.assertEquals(17, tokens.size());
    SemanticAnalyzer analyzer = new SemanticAnalyzer(tokens, root);
    tokens = analyzer.analyze();
    Assert.assertEquals(9, tokens.size());
    Assert.assertEquals("Đất nước", tokens.get(0).getOriginal());
    Assert.assertEquals("<i>", tokens.get(1).getOriginal());
    Assert.assertEquals(" ", tokens.get(2).getOriginal());
    Assert.assertEquals("tôi", tokens.get(3).getOriginal());
    Assert.assertEquals(" ", tokens.get(4).getOriginal());
    Assert.assertEquals("thon thả", tokens.get(5).getOriginal());
    Assert.assertEquals("</i>", tokens.get(6).getOriginal());
    Assert.assertEquals(" ", tokens.get(7).getOriginal());
    Assert.assertEquals("giọt đàn bầu", tokens.get(8).getOriginal());
  }
}
