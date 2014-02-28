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

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * 
 *         Feb 20, 2014
 */
public class SearchEngine {

  private IndexSearcher searcher;

  public SearchEngine(String indexDir) throws IOException {
    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDir + "/index")));
    searcher = new IndexSearcher(reader);
  }

  public TopDocsCollector<ScoreDoc> query(String query, int limit) throws ParseException, IOException {
    TopScoreDocCollector collector = TopScoreDocCollector.create(limit, false);
    QueryParser parser = new QueryParser(Version.LUCENE_46, "name", new CustomAnalyzer());
    Query lquery = parser.parse(query);
    searcher.search(lquery, collector);
    return collector;
  }

  public void update() throws IOException {
    DirectoryReader reader = (DirectoryReader) searcher.getIndexReader();
    reader = DirectoryReader.openIfChanged(reader);
    Similarity similarity = searcher.getSimilarity();
    searcher = new IndexSearcher(reader);
    searcher.setSimilarity(similarity);
  }

  public void close() throws IOException {
    searcher.getIndexReader().close();
  }
}
