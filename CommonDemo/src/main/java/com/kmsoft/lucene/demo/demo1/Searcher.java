package com.kmsoft.lucene.demo.demo1;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 搜索类
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/13 13:55
 */
public class Searcher {

    private final IndexSearcher indexSearcher;
    private final QueryParser queryParser;

    public Searcher(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open((new File(indexDirectoryPath)).toPath());
        indexSearcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
        queryParser = new QueryParser(LuceneConstants.CONTENTS, new SmartChineseAnalyzer());
    }

    public TopDocs search(String queryStr) throws IOException, ParseException {
        Query query = queryParser.parse(queryStr);
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    public Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public void close() throws IOException {
        indexSearcher.getIndexReader().close();
    }

}
