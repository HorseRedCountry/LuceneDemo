package com.kmsoft.lucene.database.demo.entity;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 电影实体
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/14 11:12
 */
@Data
@Builder
public class FilmEntity {

    private Long filmId;
    private String title;
    private String description;
    private String releaseYear;

}
