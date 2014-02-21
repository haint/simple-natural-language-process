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

import java.lang.reflect.Field;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 19, 2014
 */
public class Product extends SemanticEntity {

  /** . */
  private static final long serialVersionUID = 1L;

  /** . */
  public static final String ENTITY_TYPE = "product";

  /** . */
  protected String[] type;

  /** . */
  protected String model, maker, classifier;

  @Override
  public String getEntityType() {
    return ENTITY_TYPE;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(", maker=").append(maker).append(", model=").append(model).append(", classifier=").append(classifier)
        .append(", type=").append(type);
    return sb.toString();
  }

  @Override
  public void doIndex(Document idoc) {
    super.doIndex(idoc);
    if (maker != null)
      idoc.add(new StringField("maker@" + ENTITY_TYPE, maker, Store.NO));
    if (model != null)
      idoc.add(new StringField("model@" + ENTITY_TYPE, model, Store.NO));
    if (classifier != null)
      idoc.add(new StringField("classifier@" + ENTITY_TYPE, classifier, Store.NO));
    if (type != null) {
      for (String s : type)
        idoc.add(new StringField("type@" + ENTITY_TYPE, s, Store.NO));
    }
  }

  /**
   * {@inheritDoc}
   * 
   * The example format is "maker: apple >> model: iphone 3G >> variant: iphone 3G, apple iphone 3G"
   * 
   * @throws SecurityException
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   */
  @Override
  public void tranform(String src) throws Exception {
    String[] blocks = src.split(SEPARATOR);
    for (String block : blocks) {
      int colon = block.indexOf(':');
      String name = block.substring(0, colon).trim();
      String value = block.substring(colon + 1).trim();
      if ("variant".equals(name)) {
        this.variants = value.split(BREAKER);
      } else {
        Field field = this.getClass().getDeclaredField(name);
        field.set(this, value);
      }
    }
    if (this.name == null)
      this.name = this.model;
  }
}
