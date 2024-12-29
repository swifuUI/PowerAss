package com.cclinux.projects.actcampus.mapper;

import com.cclinux.framework.core.mapper.ProjectBaseMapper;
import com.cclinux.projects.actcampus.model.ActivityJoinModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository("ActCampusActivityJoinMapper")
@Mapper
public interface ActivityJoinMapper extends ProjectBaseMapper<ActivityJoinModel> {

}
