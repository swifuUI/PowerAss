package com.cclinux.projects.actcampus.mapper;

import com.cclinux.framework.core.mapper.ProjectBaseMapper;
import com.cclinux.projects.actcampus.model.FavModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository("ActCampusFavMapper")
@Mapper
public interface FavMapper extends ProjectBaseMapper<FavModel> {
}
