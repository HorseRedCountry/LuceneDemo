package com.kmsoft.lucene.database.demo.search;

import com.kmsoft.lucene.database.demo.dao.FilmDao;
import com.kmsoft.lucene.database.demo.entity.FilmEntity;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 创建索引库
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/14 11:55
 */
public class Indexer {
    /**
     * 创建索引库
     *
     * @param indexPath 索引存储路径
     */
    public static void createIndex(String indexPath) throws IOException {
        //1.查询，获取所有原始数据
        FilmDao filmDao = new FilmDao();
        List<FilmEntity> allFilms = filmDao.getAllFilms();
        if (allFilms.isEmpty()) {
            return;
        }
        //2.创建文档对象，将所需的域添加至文本对象内
        List<Document> documentList = new ArrayList<>();
        for (FilmEntity film : allFilms) {
            Document document = new Document();
            document.add(new StringField("filmId", film.getFilmId() + "", Field.Store.YES));
            document.add(new TextField("title", film.getTitle(), Field.Store.YES));
            document.add(new StringField("description", film.getDescription(), Field.Store.YES));
            document.add(new StringField("releaseYear", film.getReleaseYear(), Field.Store.YES));
            documentList.add(document);
        }
        //3.创建索引写入对象，并指定分词器，写入索引
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open((new File(indexPath)).toPath()), new IndexWriterConfig(new IKAnalyzer()));
        System.out.println("一共创建了 " + documentList.size() + " 个索引文档");
        indexWriter.addDocuments(documentList);
        //4.释放资源
        indexWriter.close();
    }
}
