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

  private Environment env;

  private IndexEngine indexer;

  private String dbPath;

  public DictionaryDB(String path, boolean writeable) throws IOException {

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
    this.store = new StoredSortedMap<String, SemanticEntity>(db, new StringBinding(), new SemanticEntityBinding(), writeable);
    this.indexer = new IndexEngine(path + "/index");
  }

  public String getPath() {
    return dbPath;
  }

  public void save(SemanticEntity entity) throws IOException {
    store.put(entity.getUUID(), entity);
    indexer.index(entity, true);
  }

  public void update(String uuid, SemanticEntity entity) throws IOException {
    if (store.containsKey(uuid))
      store.replace(uuid, entity);
    else
      store.put(uuid, entity);

    //
    indexer.index(entity, false);
  }

  public SemanticEntity remove(String uuid) {
    return store.remove(uuid);
  }

  public void commit() throws IOException {
    db.sync();
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
    env.sync();
    env.close();
  }

  public long count() {
    return db.count();
  }

  public boolean containsKey(String uuid) {
    return store.containsKey(uuid);
  }

  public SemanticEntity search(String uuid) {
    return store.get(uuid);
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
