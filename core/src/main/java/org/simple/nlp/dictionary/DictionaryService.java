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
package org.simple.nlp.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.simple.nlp.dictionary.db.DictionaryDB;
import org.simple.nlp.dictionary.entities.Place;
import org.simple.nlp.dictionary.entities.Product;
import org.simple.nlp.dictionary.entities.SemanticEntity;
import org.simple.nlp.dictionary.entities.SemanticQuery;
import org.simple.nlp.dictionary.entities.SemanticWord;
import org.simple.nlp.dictionary.entities.Synset;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 27, 2014
 */
public final class DictionaryService {
  
  private static DictionaryService instance;
  
  private DictionaryDB db;
  
  private DictionaryService() {
    InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
    Properties prop = new Properties();
    try {
      prop.load(is);
    } catch (Exception e) {
      System.out.println("[WARNING]: Can't load config.properties file");
      prop.setProperty("db.path", "target/db");
      prop.setProperty("db.init.path", "src/main/resources");
      prop.setProperty("db.writable", "true");
    }
    
    String dbPath = prop.getProperty("db.path");
    String initPath = prop.getProperty("db.init.path");
    boolean writable = Boolean.parseBoolean(prop.getProperty("db.writable"));
    try {
      db = new DictionaryDB(dbPath, writable);
      if (db.count() == 0) {
        this.init(initPath);
        db.commit();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private void init(String initPath) throws Exception {
    File dir = new File(initPath);
    for (File file : dir.listFiles()) {
      String name = file.getName();
      if (!name.endsWith(".data")) continue;

      String type = name.substring(0, name.indexOf('.'));
      //
      this.transform(file, type);
    }
  }
  
  private void transform(File file, String type) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    while ((line = reader.readLine()) != null) {
      SemanticEntity entity = null;

      if (SemanticWord.ENTITY_TYPE.equals(type)) {
        entity = new SemanticWord();
      } else if (Place.ENTITY_TYPE.equals(type)) {
        entity = new Place();
      } else if (Product.ENTITY_TYPE.equals(type)) {
        entity = new Product();
      } else if (Synset.ENTITY_TYPE.equals(type)) {
        entity = new Synset();
      } else if (SemanticQuery.ENTITY_TYPE.equals(type)) {
        entity = new SemanticQuery();
      } else throw new UnsupportedOperationException("Unsupported the type: " + type);
      
      entity.tranform(line);
      db.save(entity);
    }
    reader.close();
  }
  
  public DictionaryDB getDatabase() {
    return db;
  }
  
  public static DictionaryService getInstance() {
    if (instance == null) instance = new DictionaryService();
    return instance;
  }
  
  public static DictionaryService reload() throws IOException {
    if (instance != null) instance.getDatabase().close();
    return instance = new DictionaryService();
  }
}
