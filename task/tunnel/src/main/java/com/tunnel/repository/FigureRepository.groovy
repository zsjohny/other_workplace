package com.tunnel.repository

import com.tunnel.domain.Figure
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = Figure.class, idClass = Long.class)
interface FigureRepository {

    @Query(value = "select  * from figure where tunnelNumber =:tunnelNumber  limit :page ,:pageSize  ", nativeQuery = true)
    List<Figure> findFigureALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


    @Query(value = "select  count(id) from figure  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
    Integer findFigureCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


    @Modifying
    @Query("update Figure set photosPicture=:photosPath where id=:id")
    void updateFigurePhotosPictureByPhotos(@Param("photosPath") String photosPath, @Param("id") Long id)


}
