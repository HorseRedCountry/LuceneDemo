package com.kmsoft.lucene.database.demo.search;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;

/**
 * <p>
 *
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/14 13:17
 */
public class DemoMain {
    public static void main(String[] args) {
        String indexDir = "D:/Lucene/databasedemo";
        try {
            Indexer.createIndex(indexDir);
            Searcher.search("who",indexDir);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            throw new RuntimeException(e);
        }
    }
}
