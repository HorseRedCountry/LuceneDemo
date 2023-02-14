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

    /**
     * 索引搜索对象，用于执行索引
     */
    private final IndexSearcher indexSearcher;
    /**
     * 查询解析器对象，用于解析并转化用户输入的查询数据
     */
    private final QueryParser queryParser;

    /**
     * 1.创建并初始化索引库目录对象
     * 2.初始化索引搜索对象
     * 3.初始化查询解析器对象
     * @param indexDirectoryPath 索引库路径
     * @throws IOException /
     */
    public Searcher(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open((new File(indexDirectoryPath)).toPath());
        //DirectoryReader：索引读取对象，用于读取索引
        indexSearcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
        //初始化参数：默认的搜索域  指定的分词器
        queryParser = new QueryParser(LuceneConstants.CONTENTS, new SmartChineseAnalyzer());
    }

    /**
     * 进行查询操作
     * @param queryStr 用户输入的查询串
     * @return 命中的结果
     * @throws IOException /
     * @throws ParseException /
     */
    public TopDocs search(String queryStr) throws IOException, ParseException {
        //实例化查询对象，用于执行查询
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
