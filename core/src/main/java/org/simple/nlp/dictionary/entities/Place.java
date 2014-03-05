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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 19, 2014
 */
public class Place extends SemanticEntity {

  /** . */
  private static final long serialVersionUID = 1L;

  /** . */
  public static final String ENTITY_TYPE = "place";

  /** . */
  protected String[] type;

  /** . */
  protected String address, place, street, quarter, district, city, province, country, description;

  @Override
  public String getEntityType() {
    return ENTITY_TYPE;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(", address=").append(address).append(", place=").append(place).append(", street=").append(street)
        .append(", quarter=").append(quarter).append(", district=").append(district).append(", city=").append(city)
        .append(", province=").append(province).append(", country=").append(country).append(", type=").append(type)
        .append(", description=").append(description);
    return sb.toString();
  }

  @Override
  public void doIndex(Document idoc) {
    super.doIndex(idoc);
    if (country != null)
      idoc.add(new StringField("country@" + ENTITY_TYPE, country, Store.NO));
    if (province != null)
      idoc.add(new StringField("province@" + ENTITY_TYPE, province, Store.NO));
    if (city != null)
      idoc.add(new StringField("city@" + ENTITY_TYPE, city, Store.NO));
    if (district != null)
      idoc.add(new StringField("district@" + ENTITY_TYPE, district, Store.NO));
    if (quarter != null)
      idoc.add(new StringField("quarter@" + ENTITY_TYPE, quarter, Store.NO));
    if (street != null)
      idoc.add(new StringField("street@" + ENTITY_TYPE, street, Store.NO));
    if (place != null)
      idoc.add(new StringField("place@" + ENTITY_TYPE, place, Store.NO));
    if (address != null)
      idoc.add(new StringField("address@" + ENTITY_TYPE, address, Store.NO));
    if (type != null) {
      for (String s : type)
        idoc.add(new StringField("type@" + ENTITY_TYPE, s, Store.NO));
    }
  }

  public String[] getType() {
    return type;
  }

  public void setType(String[] type) {
    this.type = type;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getQuarter() {
    return quarter;
  }

  public void setQuarter(String quarter) {
    this.quarter = quarter;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * {@inheritDoc}
   * 
   * The example format is "country: việt nam >> city: cần thơ, tp.cần thơ, tp. cần thơ, tp cần thơ, tp . cần thơ" In this
   * example, The last block represent name and variants
   */
  @Override
  public void tranform(String src) throws Exception {
    List<String> list = Arrays.asList(src.split(SEPARATOR));
    for (Iterator<String> i = list.iterator(); i.hasNext();) {
      src = i.next();
      int colon = src.indexOf(':');

      String fieldName = src.substring(0, colon).trim();
      String fieldValue = src.substring(colon + 1).trim();

      fieldName = Normalizer.INSTANCE.resolve(fieldName);
      if (!i.hasNext()) {
        String[] values = fieldValue.split(BREAKER);
        this.name = values[0];
        try {
          Field field = getClass().getDeclaredField(fieldName);
          field.set(this, this.name);
        } catch (NoSuchFieldException e) {
          System.out.println(this);
        }
        
        if (values.length > 1) {
          this.variants = new String[values.length - 1];
          System.arraycopy(values, 1, variants, 0, variants.length);
        }
        return;
      }

      try {
        Field field = getClass().getDeclaredField(fieldName);
        field.set(this, fieldValue);
      } catch (NoSuchFieldException e) {
        System.out.println(this);
      }
    }
  }

  private static class Normalizer {

    private static Normalizer INSTANCE = new Normalizer();

    private Map<String, String> map;

    Normalizer() {
      Map<String, String> map = new HashMap<String, String>();
      map.put("thị xã", "city");
      map.put("quận", "district");
      map.put("huyện", "district");
      map.put("phường", "quarter");
      map.put("thị trấn", "quarter");
      map.put("xã", "quarter");
      map.put("xóm", "place");
      map.put("thôn", "place");
      this.map = map;
    }

    String resolve(String s) {
      return map.get(s) == null ? s : map.get(s);
    }
  }
}
