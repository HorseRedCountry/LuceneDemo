package com.kmsoft.lucene.ik.demo;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>
 * 创建索引
 * </P>
 * 流程：
 * 1.获取原始数据，创建文档对象
 * 2.选择合适的分析器，对文档对象进行分词
 * 3.写入索引库
 *
 * @author Major Tom
 * @since 2023/2/13 11:24
 */
public class Indexer {
    /**
     * 索引写入对象，用于将文档对象写入索引库
     */
    private final IndexWriter writer;

    /**
     * 初始化
     * 1.创建并初始化索引库目录对象
     * 2.选择合适的配置（即适当的分词器），初始化索引写入对象
     *
     * @param indexDirectoryPath 索引库存储路径
     * @throws IOException /
     */
    public Indexer(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open((new File(indexDirectoryPath)).toPath());
        writer = new IndexWriter(indexDirectory, new IndexWriterConfig(new IKAnalyzer()));
    }


    /**
     * 关闭索引写入对象，释放资源
     *
     * @throws IOException /
     */
    public void close() throws IOException {
        writer.close();
    }

    /**
     * 创建文档对象，并把域添加至文档对象内
     *
     * @param file 原始数据文件
     * @return 创建好的文档对象
     * @throws IOException /
     */
    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        //把域添加至文档对象
        document.add(new TextField(LuceneConstants.CONTENTS, new FileReader(file)));
        document.add(new StringField(LuceneConstants.FILE_NAME, file.getName().split("\\.")[0], Field.Store.YES));
        document.add(new StringField(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES));
        return document;
    }

    /**
     * 创建文档对象，并将文档对象写入索引库
     *
     * @param file 原始数据文件
     * @throws IOException /
     */
    private void indexFile(File file) throws IOException {
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    /**
     * 创建索引库
     *
     * @param dataDirPath 原始数据路径
     * @param fileFilter  文件过滤器
     * @return 创建的document对象个数
     * @throws IOException /
     */
    public int createIndex(String dataDirPath, FileFilter fileFilter) throws IOException {
        File[] files = new File(dataDirPath).listFiles();
        if (null != files && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && fileFilter.accept(file)) {
                    indexFile(file);
                }
            }
        }
        return writer.numRamDocs();
    }
}
