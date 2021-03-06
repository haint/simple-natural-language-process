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

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 19, 2014
 */
public class SemanticWord extends SemanticEntity {

  /** . */
  private static final long serialVersionUID = 1L;

  /** . */
  public static final String ENTITY_TYPE = "word";

  /** . */
  protected String[] type, synonym, antonym;

  @Override
  public String getEntityType() {
    return ENTITY_TYPE;
  }

  public String[] getSynonym() {
    return synonym;
  }

  public void setSynonym(String[] synonym) {
    this.synonym = synonym;
  }

  public String[] getAntonym() {
    return antonym;
  }

  public void setAntonym(String[] antonym) {
    this.antonym = antonym;
  }

  public void setType(String[] type) {
    this.type = type;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(", type=").append(type).append(", synonym=").append(synonym).append(", antonym=").append(antonym);
    return sb.toString();
  }

  /**
   * {@inheritDoc}
   * 
   * The example format is "noun: hoa binh"
   */
  @Override
  public void tranform(String src) {
    String name = src.substring("noun:".length()).trim();
    this.name = name;
  }
}
