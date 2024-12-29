package com.cclinux.projects.actcampus.service.cust;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.helper.FakerHelper;
import com.cclinux.projects.actcampus.mapper.ActivityJoinMapper;
import com.cclinux.projects.actcampus.mapper.ActivityMapper;
import com.cclinux.projects.actcampus.mapper.UserMapper;
import com.cclinux.projects.actcampus.model.ActivityJoinModel;
import com.cclinux.projects.actcampus.model.ActivityModel;
import com.cclinux.projects.actcampus.model.UserModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Notes: 活动模块业务逻辑
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/7 5:41
 * @Ver: ccminicloud-framework 3.2.1
 */

@Service("ActCampusTestService")
public class TestService extends BaseMyCustService {

    @Resource(name = "ActCampusActivityMapper")
    private ActivityMapper activityMapper;

    @Resource(name = "ActCampusActivityJoinMapper")
    private ActivityJoinMapper activityJoinMapper;

    @Resource(name = "ActCampusUserMapper")
    private UserMapper userMapper;


    public void test() {



    }


}
