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
package org.simple.nlp.dictionary.db;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.simple.nlp.dictionary.entities.SemanticEntity;
import org.simple.nlp.dictionary.index.IndexEngine;

import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 20, 2014
 */
public class DictionaryDB {

  private Database db;
  
  private StoredSortedMap<String, SemanticEntity> store;
  
  private Database counterDB;
  
  private StoredSortedMap<String, Long> counter;

  private Environment env;

  private IndexEngine indexer;

  private String dbPath;

  public DictionaryDB(String path, boolean writable) throws IOException {

    this.dbPath = path;

    EnvironmentConfig envConfig = new EnvironmentConfig();
    envConfig.setTransactional(false);
    envConfig.setAllowCreate(true);
    envConfig.setCacheSize(5 * 1024 * 1024);

    File folder = new File(path + "/db");
    if (!folder.exists())
      folder.mkdirs();

    env = new Environment(folder, envConfig);

    DatabaseConfig dbConfig = new DatabaseConfig();
    dbConfig.setTransactional(false);
    dbConfig.setAllowCreate(true);
    dbConfig.setDeferredWrite(true);
    dbConfig.setSortedDuplicates(false);

    this.db = env.openDatabase(null, "dictionary", dbConfig);
    this.counterDB = env.openDatabase(null, "counter", dbConfig);
    this.store = new StoredSortedMap<String, SemanticEntity>(db, new StringBinding(), new SemanticEntityBinding(), writable);
    this.counter = new StoredSortedMap<String, Long>(counterDB, new StringBinding(), new LongBinding(), writable);
    this.indexer = new IndexEngine(path + "/index");
  }

  public String getPath() {
    return dbPath;
  }
  
  public void save(SemanticEntity entity) throws IOException {
    if (store.put(entity.getUUID(), entity) == null) {
      Long count = counter.get(entity.getClass().toString());
      count = count != null ? ++count : new Integer(1);
      counter.put(entity.getClass().toString(), count);
      indexer.index(entity, true);
    }
  }
  
  public void update(String uuid, SemanticEntity entity) throws IOException {
    if (store.containsKey(uuid)) {
      store.replace(uuid, entity);
      indexer.index(entity, false);
    }
    else
      this.save(entity);
  }

  public SemanticEntity remove(SemanticEntity entity) throws IOException {
    Long count = counter.get(entity.getClass().toString());
    if (count != null && count > 0) {
      count--;
    } else count = new Long(0);
    counter.put(entity.getClass().toString(), count);
    indexer.delete(entity.getUUID());
    return store.remove(entity.getUUID());
  }
  
  public SemanticEntity getEntity(String uuid) {
    return store.get(uuid);
  }
  
  public Iterator<SemanticEntity> getEntities(Class<? extends SemanticEntity> clazz) {
    return new SemanticIterator(store.values().iterator(), clazz);
  }

  public void commit() throws IOException {
    db.sync();
    counterDB.sync();
    indexer.commit();
  }

  public void compress() {
    db.getEnvironment().compress();
  }

  public Iterator<SemanticEntity> getSemanticEntities() {
    return store.values().iterator();
  }
  
  public void close() throws IOException {
    db.sync();
    db.close();
    //
    counterDB.sync();
    counterDB.close();
    //
    env.sync();
    env.close();
  }

  public long count() {
    return db.count();
  }
  
  public long count(Class<? extends SemanticEntity> clazz) {
    Long count = counter.get(clazz.toString());
    return count == null ? 0 : count;
  }

  public boolean containsKey(String uuid) {
    return store.containsKey(uuid);
  }

  public SemanticEntity search(String uuid) {
    return store.get(uuid);
  }
  
  private class SemanticIterator implements Iterator {

    private Iterator<SemanticEntity> source;
    
    private Class<?extends SemanticEntity> entityType;
    
    SemanticIterator(Iterator<SemanticEntity> source, Class<? extends SemanticEntity> clazz) {
      this.source = source;
      this.entityType = clazz;
    }
    
    public boolean hasNext() {
      throw new UnsupportedOperationException("Use next() != null to instead of");
    }

    public SemanticEntity next() {
      while (source.hasNext()) {
        SemanticEntity entity = source.next();
        if (entity == null) return null;
        else if (entity.getClass() == entityType) return entity;
      }
      return null;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private class SemanticEntityBinding extends TupleBinding<SemanticEntity> {

    @Override
    public SemanticEntity entryToObject(TupleInput input) {
      try {
        ObjectInputStream is = new ObjectInputStream(input);
        Object obj = is.readObject();
        return (SemanticEntity) obj;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public void objectToEntry(SemanticEntity object, TupleOutput output) {
      try {
        ObjectOutputStream os = new ObjectOutputStream(output);
        os.writeObject(object);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
