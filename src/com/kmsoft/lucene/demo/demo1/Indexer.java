package com.kmsoft.lucene.demo.demo1;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;

/**
 * <p>
 * 创建索引
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/13 11:24
 */
public class Indexer {
    private final IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open((new File(indexDirectoryPath)).toPath());
        writer = new IndexWriter(indexDirectory, new IndexWriterConfig());
    }


    public void close() throws IOException {
        writer.close();
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        document.add(new TextField(LuceneConstants.CONTENTS, new FileReader(file)));
        document.add(new StringField(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES));
        document.add(new StringField(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES));
        return document;
    }

    private void indexFile(File file) throws IOException {
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

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
