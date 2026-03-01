package com.cclinux.projects.actcampus.mapper;

import com.cclinux.framework.core.mapper.ProjectBaseMapper;
import com.cclinux.projects.actcampus.model.ActivityModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository("ActCampusActivityMapper")
@Mapper
public interface ActivityMapper extends ProjectBaseMapper<ActivityModel> {
}
