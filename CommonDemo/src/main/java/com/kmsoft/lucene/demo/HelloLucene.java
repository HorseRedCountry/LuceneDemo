package com.kmsoft.lucene.demo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;

import java.io.IOException;

/**
 * <p>
 * Lucene Demo
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/13 9:24
 */
public class HelloLucene {
    public static void main(String[] args) throws IOException, ParseException {
        //1.指定词法分析器
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //2.创建索引
        ByteBuffersDirectory index = new ByteBuffersDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(index, config);
        addDoc(indexWriter, "Lucene in Action", "193398817");
        addDoc(indexWriter, "Lucene for Dummies", "55320055Z");
        addDoc(indexWriter, "Managing Gigabytes", "55063554A");
        addDoc(indexWriter, "The Art of Computer Science", "9900333X");
        indexWriter.close();
        //3.进行查询
        String queryStr = args.length > 0 ? args[0] : "Lucene";
        Query query = new QueryParser("title", analyzer).parse(queryStr);
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(query, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;
        //4.显示搜索结果
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document doc = searcher.doc(docId);
            System.out.println((i + 1) + ". " + doc.get("isbn") + "\t" + doc.get("title"));
        }
        reader.close();
    }

    /**
     * 将指定字段加入索引中
     *
     * @param indexWriter /
     * @param title       /
     * @param isbn        /
     * @throws IOException /
     */
    private static void addDoc(IndexWriter indexWriter, String title, String isbn) throws IOException {
        Document doc = new Document();
        //用TextField将 title 加入至索引字段
        doc.add(new TextField("title", title, Field.Store.YES));
        //用StringField,isbn 字段不加入索引
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        indexWriter.addDocument(doc);
    }

}
