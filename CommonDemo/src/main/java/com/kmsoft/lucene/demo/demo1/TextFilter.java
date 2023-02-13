package com.kmsoft.lucene.demo.demo1;

import java.io.File;
import java.io.FileFilter;

/**
 * <p>
 * .txt文件过滤器
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/13 11:22
 */
public class TextFilter implements FileFilter {

    @Override
    public boolean accept(File pathName) {
        return pathName.getName().toLowerCase().endsWith(".txt");
    }
}
