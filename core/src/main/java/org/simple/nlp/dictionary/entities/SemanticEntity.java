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

import java.io.Serializable;
import java.util.UUID;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 18, 2014
 */
public abstract class SemanticEntity implements Serializable {

  /** . */
  private static final long serialVersionUID = 1L;

  /** . */
  protected String name;

  /** . */
  protected String[] variants, keywords;

  /** . */
  protected transient String SEPARATOR = ">>", BREAKER = ",";

  /** . */
  private String uuid;

  public String getUUID() {
    if (uuid == null) {
      uuid = UUID.nameUUIDFromBytes(toString().getBytes()).toString();
    }
    return uuid;
  }

  public abstract String getEntityType();

  /**
   * To add value of entity into Lucene document for indexing.
   * 
   * @param idoc the Lucene document.
   */
  public void doIndex(Document idoc) {
    idoc.add(new StringField("uuid", uuid, Store.YES));
    idoc.add(new TextField("name", name, Store.NO));

    if (variants != null) {
      for (String variant : variants) {
        idoc.add(new TextField("variants", variant, Store.NO));
      }
    }

    if (keywords != null) {
      for (String keyword : keywords) {
        idoc.add(new TextField("keywords", keyword, Store.NO));
      }
    }
  }

  /**
   * To convert a string which specify format to semantic entity. The format is "name1:value1>>name2:value2 ..." The last block
   * of string source represent name and variants of entity. In example: "country:vietnam >> city:ha noi, hanoi, HN", represent
   * {@link Product} entity named "ha noi", variants {"hanoi", "HN"}, city: "ha noi" and country "vietnam".
   * 
   * @param src The String source is formated
   * @throws SecurityException
   * @throws NoSuchFieldException
   * @throws Exception
   */
  public abstract void tranform(String src) throws Exception;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String[] getVariants() {
    return variants;
  }

  public void setVariants(String[] variants) {
    this.variants = variants;
  }

  public String[] getKeywords() {
    return keywords;
  }

  public void setKeywords(String[] keywords) {
    this.keywords = keywords;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName()).append(" name=").append(name).append(", entity-type=").append(getEntityType())
        .append(", variants=").append(variants).append(", keywords=").append(keywords);
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;
    else if (obj instanceof SemanticEntity) {
      SemanticEntity that = (SemanticEntity) obj;
      return that.hashCode() == this.hashCode();
    }
    return false;
  }
}
