package com.tunnel.repository

import com.tunnel.domain.Project
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface ProjectCrudRepository extends CrudRepository<Project, Integer> {
    Project findByProjectNumber(String projectNumber)

}
