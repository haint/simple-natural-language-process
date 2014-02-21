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
package org.simple.nlp.dictionary.index;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 20, 2014
 */
final class CustomAnalyzer extends Analyzer {

  @Override
  protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
    final WordTokenizer src = new WordTokenizer(Version.LUCENE_46, reader);
    return new TokenStreamComponents(src);
  }

  class WordTokenizer extends CharTokenizer {

    public WordTokenizer(Version matchVersion, Reader input) {
      super(matchVersion, input);
    }

    @Override
    protected boolean isTokenChar(int c) {
      if (Character.isLetter(c))
        return true;
      if (c >= '0' && c <= '9')
        return true;
      if (c == '.' || c == '-' || c == '_' || c == '/' || c == ':' || c == '@')
        return true;
      return false;
    }

    @Override
    protected int normalize(int c) {
      return Character.toLowerCase(c);
    }
  }
}
