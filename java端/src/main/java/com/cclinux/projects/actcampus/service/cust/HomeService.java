package com.cclinux.projects.actcampus.service.cust;

import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.actcampus.mapper.ActivityMapper;
import com.cclinux.projects.actcampus.mapper.NewsMapper;
import com.cclinux.projects.actcampus.model.ActivityModel;
import com.cclinux.projects.actcampus.model.NewsModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Notes: 首页
 * @Author: cclinux0730 (weixin)
 * @Ver: ccminicloud-framework 3.2.1
 */

@Service("ActCampusHomeService")
public class HomeService extends BaseMyCustService {

    @Resource(name = "ActCampusNewsMapper")
    private NewsMapper newsMapper;

    @Resource(name = "ActCampusActivityMapper")
    private ActivityMapper activityMapper;

    /**
     * 首页列表
     */
    public Map<String, Object> getHomeList() {
        Map<String, Object> ret = new HashMap<>();

        Where<NewsModel> whereNews = new Where<>();
        whereNews.eq("NEWS_VOUCH",1);
        whereNews.orderByAsc("NEWS_ORDER");
        whereNews.orderByDesc("NEWS_ID");
        List<Map<String, Object>> newsList = newsMapper.getAllListMap(whereNews, "NEWS_TITLE,NEWS_OBJ,NEWS_ID," +
                "NEWS_CATE_NAME,ADD_TIME");

        for (Map<String, Object> record : newsList) {
            FormHelper.fmtDBObj(record, "newsObj", "cover");
            TimeHelper.fmtDBTime(record,"addTime","yyyy-MM-dd");
        }

        ret.put("newsList", newsList);

        Where<ActivityModel> whereActivity = new Where<>();
        whereActivity.eq("ACTIVITY_VOUCH",1);
        whereActivity.orderByAsc("ACTIVITY_ORDER");
        whereActivity.orderByDesc("ACTIVITY_ID");
        List<Map<String, Object>> activityList = activityMapper.getAllListMap(whereActivity, "ACTIVITY_TITLE," +
                "ACTIVITY_JOIN_CNT," +
                "ACTIVITY_OBJ," +
                "ACTIVITY_ID," +
                "ACTIVITY_CATE_NAME, ADD_TIME");

        for (Map<String, Object> record : activityList) {
            FormHelper.fmtDBObj(record, "activityObj", "cover");
        }

        ret.put("activityList", activityList);

        return ret;
    }

}
