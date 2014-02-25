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

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.simple.nlp.dictionary.utils.CharacterUtil;
import org.simple.nlp.dictionary.utils.VietnameseUtil;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 25, 2014
 */
public class TokenParser {
  
  public List<Token> parse(Reader reader) throws IOException {
    char[] data = new char[4912];
    int available = -1;
    CharArrayWriter w = new CharArrayWriter(4912);
    while ((available = reader.read(data)) > -1) {
      w.write(data, 0, available);
    }
    return this.parse(w.toCharArray());
  }
  
  public List<Token> parse(String s) {
    return this.parse(s.toCharArray());
  }

  public List<Token> parse(char[] buffer) {
    Tokenizer tokenizer = new Tokenizer(buffer);
    List<Token> holder = new ArrayList<Token>();
    
    Token currentToken = null;
    while (tokenizer.next()) {
      currentToken = new Token(tokenizer.getCurrentToken(), tokenizer.type);
      holder.add(currentToken);
    }
    //
    return holder;
  }
  
  private static class Tokenizer {
    
    /** . */
    private TokenType type;

    /** .*/
    private char[] chars;
    
    /** .*/
    private int pos = 0, start = 0, end = 0;
    
    Tokenizer(char[] chars) {
      this.chars = chars;
    }
    
    String getCurrentToken() {
      return new String(chars, start, end - start );
    }
    
    boolean next() {
      if (this.pos < this.chars.length) {
        char c = this.chars[this.pos];
        if (c == '<' && this.pos + 2 < this.chars.length) { // Detect open XML tag, include " <! ", " </ ", " <? "
          int end = -1;
          this.start = pos;
          this.pos ++;
          
          //
          if (CharacterUtil.isAlphaDigit(chars[pos]) || chars[pos] == '!' || chars[pos] == '/' || chars[pos] == '?') {
            while (++pos < chars.length) {
              if (chars[pos] == '>') {
                end = ++pos; //Detect close tag
                break;
              }
            }
            //
            if (end > 0) {
              this.end = end;
              this.type = TokenType.XML_TAG;
              return true;
            }
          } else {
            this.end = start + 1;
            this.type = TokenType.PUNCTUATION;
            return true;
          }
        } else if (CharacterUtil.isDigit(c)) {
          this.start = pos++;
          while (pos < chars.length && CharacterUtil.isDigit(chars[pos])) pos++;
          this.end = pos;
          this.type = TokenType.DIGIT;
        } else if (CharacterUtil.isBlank(c)) {
          this.start = pos++;
          while (pos < chars.length && CharacterUtil.isBlank(chars[pos])) pos++;
          this.end = pos;
          this.type = TokenType.BLANK;
        } else if (CharacterUtil.isNewLine(c)) {
          this.start = pos++;
          while (pos < chars.length && CharacterUtil.isNewLine(chars[pos])) pos++;
          this.end = pos;
          this.type = TokenType.NEW_LINE;
        } else if (CharacterUtil.isPunctuation(c)) {
          this.start = pos++;
          this.end = pos;
          this.type = TokenType.PUNCTUATION;
        } else if (CharacterUtil.isAlphaDigit(c)) {
          this.start = pos++;
          while (pos < chars.length && CharacterUtil.isAlphaDigit(chars[pos])) pos++;
          this.end = pos;
          this.type = TokenType.WORD;
        } else {
          this.start = pos++;
          this.end = pos;
          this.type = TokenType.UNKNOW;
        }
        return true;
      }
      return false;
    }
  }
  
  private static class DefaultCharacterComparator implements Comparator<Character> {

    public int compare(Character o1, Character o2) {
      return o1.compareTo(o2);
    }
  };
  
  private static class NoVietnameseAccentCharacterComparator implements Comparator<Character> {

    public int compare(Character o1, Character o2) {
      int c1 = Character.toLowerCase(VietnameseUtil.removeVietnameseAccent(o1));
      int c2 = Character.toLowerCase(VietnameseUtil.removeVietnameseAccent(o2));
      return c1 - c2;
    }
  }
}
