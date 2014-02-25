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
package org.simple.nlp.token;

import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 26, 2014
 */
public class TokenParserTestCase extends TestCase{

  @Test
  public void testXmlTag() {
    TokenParser parser = new TokenParser();
    List<Token> tokens = parser.parse("<a></a><?xml>");
    
    assertEquals(3, tokens.size());
    
    assertEquals("<a>", tokens.get(0).getOriginal());
    assertEquals(TokenType.XML_TAG, tokens.get(0).getType());
    
    assertEquals("</a>", tokens.get(1).getOriginal());
    assertEquals(TokenType.XML_TAG, tokens.get(1).getType());
    
    assertEquals("<?xml>", tokens.get(2).getOriginal());
    assertEquals(TokenType.XML_TAG, tokens.get(2).getType());
  }
  
  @Test
  public void testPunctuation() {
    TokenParser parser = new TokenParser();
    List<Token> tokens = parser.parse("<<a>>");
    
    assertEquals(3, tokens.size());
    
    assertEquals("<", tokens.get(0).getOriginal());
    assertEquals(TokenType.PUNCTUATION, tokens.get(0).getType());
    
    assertEquals("<a>", tokens.get(1).getOriginal());
    assertEquals(TokenType.XML_TAG, tokens.get(1).getType());
    
    assertEquals(">", tokens.get(2).getOriginal());
    assertEquals(TokenType.PUNCTUATION, tokens.get(2).getType());
  }
  
  @Test
  public void testNewLine() {
    TokenParser parser = new TokenParser();
    List<Token> tokens = parser.parse("\n\f\r");
    assertEquals(1, tokens.size());
    assertEquals("\n\f\r", tokens.get(0).getOriginal());
  }
  
  @Test
  public void testBlank() {
    TokenParser parser = new TokenParser();
    List<Token> tokens = parser.parse(" \t ");
    assertEquals(1, tokens.size());
    assertEquals(" \t ", tokens.get(0).getOriginal());
  }
  
  @Test
  public void testSemantic() {
    TokenParser parser = new TokenParser();
    List<Token> tokens = parser.parse("Vietnam\t 7554");
    
    assertEquals(3, tokens.size());
    
    assertEquals("Vietnam", tokens.get(0).getOriginal());
    assertEquals(TokenType.WORD, tokens.get(0).getType());
    
    assertEquals("\t ", tokens.get(1).getOriginal());
    assertEquals(TokenType.BLANK, tokens.get(1).getType());
    
    assertEquals("7554", tokens.get(2).getOriginal());
    assertEquals(TokenType.DIGIT, tokens.get(2).getType());
  }
}
