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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simple.nlp.dictionary.entities.SemanticEntity;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 18, 2014
 */
public class WordTree {

  private Map<String, WordTree> tree;

  private SemanticEntity[] entities;

  private String[] path;

  public WordTree(String[] path) {
    this.path = path;
  }

  public String[] getPath() {
    return this.path;
  }

  public Map<String, WordTree> getTreeMap() {
    return this.tree;
  }

  public SemanticEntity[] getEntities() {
    return this.entities;
  }

  public String getPathAsString() {
    if (path.length == 1)
      return path[0];
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.length; i++) {
      if (i > 0)
        sb.append(' ');
      sb.append(path[i]);
    }
    return sb.toString().trim();
  }

  public boolean hasEntities() {
    return this.entities != null;
  }

  public boolean hasEntity(Class<? extends SemanticEntity> type) {
    if (entities == null)
      return false;
    for (int i = 0; i < entities.length; i++) {
      if (entities[i].getClass() == type)
        return true;
    }
    return false;
  }

  public void addEntity(SemanticEntity newEntity) {
    if (entities == null)
      entities = new SemanticEntity[] { newEntity };
    else {
      for (int i = 0; i < entities.length; i++) {
        if (entities[i] == newEntity)
          return;
      }
      SemanticEntity[] tmp = new SemanticEntity[entities.length + 1];
      System.arraycopy(entities, 0, tmp, 0, entities.length);
      tmp[tmp.length - 1] = newEntity;
      this.entities = tmp;
    }
  }

  public void addEntity(String[] path, SemanticEntity newEntity) {
    this.addEntity(path, 0, newEntity);
  }

  private void addEntity(String[] path, int currentDeep, SemanticEntity newEntity) {
    if (currentDeep == path.length) {
      addEntity(newEntity);
      return;
    }

    if (tree == null)
      tree = new HashMap<String, WordTree>();
    WordTree child = tree.get(path[currentDeep]);
    if (child == null) {
      String[] treePath = new String[currentDeep + 1];
      System.arraycopy(path, 0, treePath, 0, treePath.length);
      child = new WordTree(treePath);
      this.tree.put(path[currentDeep], child);
    }
    child.addEntity(path, ++currentDeep, newEntity);
  }

  public WordTree match(String[] path) {
    return this.match(path, 0);
  }

  private WordTree match(String[] path, int pos) {
    if (tree == null || path == null || pos == path.length)
      return null;
    String token = path[pos];
    WordTree foundTree = tree.get(token);
    if (pos + 1 == path.length)
      return foundTree;
    return foundTree.match(path, ++pos);
  }

  public void dump(PrintStream out, String indent) {
    if (tree == null)
      return;
    List<String> keys = new ArrayList<String>(tree.keySet());
    Collections.sort(keys, new Comparator<String>() {
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    });
    for (String wordUnit : keys) {
      WordTree child = tree.get(wordUnit);
      out.print(indent);
      out.print("+ ");
      out.print(wordUnit);
      out.print("[");
      if (child.hasEntities()) {
        for (SemanticEntity entity : child.getEntities()) {
          out.print(entity.getName());
          out.print(",");
        }
      }
      out.print("]");
      out.println();
      child.dump(out, indent + "  ");
    }
  }
}
