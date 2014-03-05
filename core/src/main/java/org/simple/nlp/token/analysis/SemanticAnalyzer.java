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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.simple.nlp.dictionary.WordTree;
import org.simple.nlp.token.Token;
import org.simple.nlp.token.TokenType;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 27, 2014
 */
public class SemanticAnalyzer extends Analyzer {

  /** .*/
  private WordTree dictionary;
  
  /** .*/
  private int position;
  
  public SemanticAnalyzer(List<Token> source, WordTree wordTree) {
    super(source);
    this.dictionary = wordTree;
  }

  @Override
  public List<Token> analyze() {
    List<Token> holder = new ArrayList<Token>();
    for (position = 0; position < source.size(); position++) {
      Token current = source.get(position);
      if (current.getType() == TokenType.WORD) {
        int currentPosition = this.position;
        WordTree found = this.match(dictionary);
        if (found != null) {
          List<Token> sub = source.subList(currentPosition, position--);
          StringBuilder sb = new StringBuilder();
          for (Iterator<Token> i = sub.iterator(); i.hasNext();) {
            Token token = i.next();
            sb.append(token.getOriginal());
          }
          holder.add(new Token(sb.toString(), TokenType.SEMANTIC));
        }
      } else {
        holder.add(current);
      }
    }
    return holder;
  }
  
  private WordTree match(WordTree tree) {
    if (tree.getTreeMap() == null || position > source.size()) return null;
    Token current = source.get(position);
    if (current.getType() == TokenType.WORD) {
      WordTree found = tree.getTreeMap().get(current.getOriginal());
      if (found == null) return null;
      position++;
      WordTree sub = this.match(found);
      if (sub != null) return sub;
      if (found.hasEntities()) return found;
    } else if (current.getType() == TokenType.BLANK) {
      position++;
      WordTree sub = this.match(tree);
      if (sub != null) return sub;
      if (tree.hasEntities()) return tree;
    }
    return tree;
  }
}
