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
package org.simple.nlp.dictionary.utils;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 25, 2014
 */
public class CharacterUtil {

  public static boolean isAlphaDigit (char c) {
    if(c >= 'A' && c <= 'Z') return true ;
    if(c >= 'a' && c <= 'z') return true ;
    if(c >= '0' && c <= '9') return true ;
    if(Character.isLetter(c)) return true ;
    return false  ;
  }
  
  public static boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  public static boolean isBlank(char c) {
    return c == ' ' || c == '\t' || c == 160;
  }

  public static boolean isNewLine(char c) {
    return c == '\n' || c == '\f' || c == '\r' || c == '\0' || c == 13;
  }

  public static boolean isPunctuation(char c) {
    //apostrophe
    if(c == '\'' || c == '’') return true ;
    //Brackets
    if(c == '(' || c == ')' || c == '[' || c == ']' || 
        c == '{' || c == '}' || c == '<' || c == '>') return true ;
    //colon
    if(c == ':') return true ;
    //comma
    if(c == ',') return true ;
    //dashes
    if( c == '‒' || c == '–' || c =='—' || c == '―') return true ;
    //exclamation mark  
    if(c == '!' ) return true ;
    //full stop (period)  
    if(c == '.') return true;
    //guillemets  
    if(c == '«' || c == '»' ) return true ;
    //hyphen  
    if( c == '-' || c == '‐' ) return true ;
    //question mark   
    if(c == '?') return true ;
    //quotation marks   
    if(c == '"' || c == '\'' || c == '‘' || c ==  '’' || c == '“' || c == '”') return true ;
    //semicolon   
    if(c == ';') return true ;
    //slash/stroke  
    if(c ==  '/' ) return true ;
    //solidus   
    if(c == '⁄') return true ;
    //percent
    if(c == '%') return true ;
    return false ;
  }
}
