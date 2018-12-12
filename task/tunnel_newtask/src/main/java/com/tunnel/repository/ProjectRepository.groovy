package com.tunnel.repository

import com.tunnel.domain.Project
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/11.
 */
interface ProjectRepository extends Repository<Project, Integer> {


    @Query(value = "select  * from project  limit :page ,:pageSize  ", nativeQuery = true)
    List<Project> findProjectALl(@Param("page") Integer page, @Param("pageSize") Integer pageSize);


    @Modifying
    @Query("update Project  set projectNumber=:projectNumber , projectName=:projectName ,examinePersion=:examinePersion,contactInfo=:contactInfo,examineDate=:examineDate where id=:id ")
    void updateProjectInfoByProject(
            @Param("projectNumber") String projectNumber,
            @Param("projectName") String projectName,
            @Param("examinePersion") String examinePersion, @Param("contactInfo") String contactInfo,
            @Param("examineDate") String examineDate, @Param("id") Integer id);


    @Query(value = "select  * from project  where projectNumber like %:projectNumber% limit :page ,:pageSize  ", nativeQuery = true)
    List<Project> findProjectALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("projectNumber") String projectNumber);

    @Query(value = "select  count(id) from project  where projectNumber like %:projectNumber%   ", nativeQuery = true)
    Integer findProjectCountALlByNumberAndPage(@Param("projectNumber") String projectNumber);


}
