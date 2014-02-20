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

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.simple.nlp.dictionary.entities.SemanticEntity;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Feb 20, 2014
 */
public class IndexEngine {
    
    public static final String UUID = "uuid";

    private Directory dir;
    
    private IndexWriter writer;
    
    public IndexEngine(String dirPath) throws IOException {
        File file = new File(dirPath);
        if (!file.exists()) file.mkdirs();
        this.dir = FSDirectory.open(file);
        
        if (IndexWriter.isLocked(dir)) 
            IndexWriter.unlock(dir);
        
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46, new CustomAnalyzer());
        conf.setRAMBufferSizeMB(256);
        this.writer = new IndexWriter(dir, conf);
    }
    
    public void index (SemanticEntity entity, boolean isNew) throws IOException {
        Document idoc = new Document();
        entity.doIndex(idoc);
        if (isNew)
            writer.addDocument(idoc);
        else {
            Term term = new Term(UUID, entity.getUUID());
            writer.updateDocument(term, idoc);
        }
    }
    
    public void commit() throws IOException {
        writer.commit();
    }
    
    public void close() throws IOException {
        writer.commit();
        writer.close();
        dir.close();
    }
}
