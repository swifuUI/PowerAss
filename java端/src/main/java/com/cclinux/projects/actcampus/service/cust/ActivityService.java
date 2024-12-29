package com.cclinux.projects.actcampus.service.cust;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.core.mapper.WhereJoin;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.actcampus.mapper.ActivityJoinMapper;
import com.cclinux.projects.actcampus.mapper.ActivityMapper;
import com.cclinux.projects.actcampus.model.ActivityJoinModel;
import com.cclinux.projects.actcampus.model.ActivityModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Notes: 活动模块业务逻辑
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/7 5:41
 * @Ver: ccminicloud-framework 3.2.1
 */

@Service("ActCampusActivityService")
public class ActivityService extends BaseMyCustService {

    @Resource(name = "ActCampusActivityMapper")
    private ActivityMapper activityMapper;

    @Resource(name = "ActCampusActivityJoinMapper")
    private ActivityJoinMapper activityJoinMapper;


    /**
     * 计算活动状态
     */
    public String getActivityStatusDesc(long id) {
        ActivityModel activity = activityMapper.getOne(id);
        if (ObjectUtil.isNull((activity))) return "";

        return getActivityStatusDesc(activity.getActivityStatus(), activity.getActivityEnd(), activity.getActivityStop()
                , activity.getActivityJoinCnt(), activity.getActivityMaxCnt());
    }


    /**
     * 计算活动状态
     */
    public String getActivityStatusDesc(Map<String, Object> map) {
        if (ObjectUtil.isNull((map))) return "";

        int status = MapUtil.getInt(map, "activityStatus");
        long end = MapUtil.getLong(map, "activityEnd");
        long stop = MapUtil.getLong(map, "activityStop");
        long joinCnt = MapUtil.getLong(map, "activityJoinCnt");
        long maxCnt = MapUtil.getLong(map, "activityMaxCnt");

        return getActivityStatusDesc(status, end, stop, joinCnt, maxCnt);
    }

    /**
     * 计算活动状态
     */
    public String getActivityStatusDesc(int status, long end, long stop, long joinCnt, long maxCnt) {


        long time = TimeHelper.timestamp();

        if (status == ActivityModel.STATUS.STOP) return "活动停止";
        else if (NumberUtil.compare(end, time) < 0) return "活动结束";
        else if (NumberUtil.compare(stop, time) < 0) return "报名结束";
        else if (joinCnt >= maxCnt) return "报名已满";
        else return "报名中";
    }

    /**
     * 活动列表
     */
    @LoginIgnore
    public PageResult getActivityList(PageParams pageRequest) {

        Where<ActivityModel> where = new Where<>();
        where.eq("ACTIVITY_STATUS", ActivityModel.STATUS.NORMAL);

        long cateId = pageRequest.getParamLong("cateId");
        if (NumberUtil.compare(cateId, 0) > 0)
            where.eq("ACTIVITY_CATE_ID", cateId);

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(wrapper -> {
                wrapper.or().like("ACTIVITY_TITLE", search);
            });
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "cateId": {
                    where.eq("ACTIVITY_CATE_ID", Convert.toLong(sortVal));
                    break;
                }
                case "today": {
                    long start = TimeHelper.getDayFirstTimestamp();
                    long end = start + 86400 * 1000 - 1;
                    where.between("ACTIVITY_START", start, end);
                    break;
                }
                case "tomorrow": {
                    long start = TimeHelper.getDayFirstTimestamp() + 86400 * 1000;
                    long end = start + 86400 * 1000 - 1;
                    where.between("ACTIVITY_START", start, end);
                    break;
                }
                case "month": {
                    long start = TimeHelper.getMonthFirstTimestamp();
                    long end = TimeHelper.getMonthLastTimestamp();
                    where.between("ACTIVITY_START", start, end);
                    break;
                }
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByAsc("ACTIVITY_ORDER");
        where.orderByDesc("ACTIVITY_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return activityMapper.getPageList(page, where, "ACTIVITY_STATUS,ACTIVITY_ID,ACTIVITY_TITLE,ACTIVITY_OBJ," +
                "ACTIVITY_START," +
                "ACTIVITY_END,ACTIVITY_STOP,ACTIVITY_CATE_NAME,ACTIVITY_MAX_CNT,ACTIVITY_JOIN_CNT");
    }

    /**
     * 取得我的报名详情
     */
    public Map<String, Object> getMyActivityJoinDetail(long userId, long activityJoinId) {
        Where<ActivityJoinModel> where = new Where<>();
        where.eq("ACTIVITY_JOIN_ID", activityJoinId);
        where.eq("ACTIVITY_JOIN_USER_ID", userId);
        Map<String, Object> ret = activityJoinMapper.getOneMap(where);
        if (ObjectUtil.isEmpty(ret)) return ret;

        long activityId = MapUtil.getLong(ret, "activityJoinActivityId");
        ret.put("activity", activityMapper.getOneMap(activityId, "ACTIVITY_TITLE,ACTIVITY_START,ACTIVITY_END"));

        return ret;
    }


    /**
     * 我的活动报名列表
     */
    @LoginIgnore
    public PageResult getMyActivityJoinList(long userId, PageParams pageRequest) {

        WhereJoin<ActivityJoinModel> where = new WhereJoin<>();
        where.leftJoin(ActivityModel.class, ActivityModel::getActivityId, ActivityJoinModel::getActivityJoinActivityId);

        where.eq("t.ACTIVITY_JOIN_USER_ID", userId);

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(wrapper -> {
                wrapper.or().like("t.ACTIVITY_JOIN_CODE", search);
                wrapper.or().like("t1.ACTIVITY_TITLE", search);
            });
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByDesc("ACTIVITY_JOIN_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return activityJoinMapper.getPageJoinList(page, where, "t.*,t1.ACTIVITY_TITLE,t1.ACTIVITY_END,t1.ACTIVITY_START");
    }


    /**
     * 单个浏览
     */
    @LoginIgnore
    public Map<String, Object> view(long id, long userId) {

        // PV
        UpdateWhere<ActivityModel> uw = new UpdateWhere<>();
        uw.eq("ACTIVITY_ID", id);
        activityMapper.inc(uw, "ACTIVITY_VIEW_CNT");

        Where<ActivityModel> where = new Where<>();
        where.eq("ACTIVITY_ID", id);
        where.eq("ACTIVITY_STATUS", ActivityModel.STATUS.NORMAL);

        Map<String, Object> ret = activityMapper.getOneMap(where);
        if (ObjectUtil.isNull((ret))) return null;

        if (NumberUtil.equals(userId, 0)) {
            ret.put("myActivityJoinId", "");
            ret.put("myActivityJoinTag", "");

            return ret;
        }

        // 判断是否有报名
        Where<ActivityJoinModel> whereJoin = new Where<>();
        whereJoin.eq("ACTIVITY_JOIN_USER_ID", userId);
        whereJoin.eq("ACTIVITY_JOIN_ACTIVITY_ID", id);
        whereJoin.eq("ACTIVITY_JOIN_STATUS", ActivityModel.STATUS.NORMAL);
        ActivityJoinModel activityJoin = activityJoinMapper.getOne(whereJoin);
        if (ObjectUtil.isNotNull(activityJoin)) {
            // 已报名
            ret.put("myActivityJoinId", activityJoin.getActivityJoinId());
            ret.put("myActivityJoinTag", "已报名");
        }


        return ret;
    }

    /**
     * 报名前获取关键信息
     */
    public Map<String, Object> detailForActivityJoin(long userId, long activityId) {
        Where<ActivityModel> where = new Where<>();
        where.eq("ACTIVITY_ID", activityId);
        where.eq("ACTIVITY_STATUS", ActivityModel.STATUS.NORMAL);
        Map<String, Object> ret = activityMapper.getOneMap(where, "ACTIVITY_TITLE");
        if (ObjectUtil.isEmpty(ret)) throw new AppException("该活动不存在");
        logger.info(ret.toString());

        // 取出本人最近一次的填写表单
        Where<ActivityJoinModel> whereJoin = new Where<>();
        whereJoin.eq("ACTIVITY_JOIN_USER_ID", userId);
        whereJoin.orderByDesc("ACTIVITY_JOIN_ID");
        Map<String, Object> retJoin = activityJoinMapper.getOneMap(whereJoin, "ACTIVITY_JOIN_FORMS");

        if (ObjectUtil.isEmpty(retJoin)) {
            ret.put("myForms", null);
        } else {
            ret.put("myForms", retJoin.get("activityJoinForms"));

        }

        return ret;
    }

    /**
     * 活动报名
     */
    public long activityJoin(long userId, long activityId, String forms, String obj) {


        return 0;
    }

    /**
     * 取消我的报名,取消即为删除记录
     */
    public void cancelMyActivityJoin(long userId, long activityJoinId) {
        Where<ActivityJoinModel> whereJoin = new Where<>();
        whereJoin.eq("ACTIVITY_JOIN_ID", activityJoinId);
        whereJoin.eq("ACTIVITY_JOIN_STATUS", ActivityJoinModel.STATUS.NORMAL);
        ActivityJoinModel activityJoin = activityJoinMapper.getOne(whereJoin);

        if (ObjectUtil.isEmpty(activityJoin))
            throw new AppException("未找到可取消的报名记录");

        if (NumberUtil.equals(activityJoin.getActivityJoinIsCheck(), 1))
            throw new AppException("该活动已经签到，无法取消");

        ActivityModel activity = activityMapper.getOne(activityJoin.getActivityJoinActivityId());
        if (ObjectUtil.isEmpty(activity))
            throw new AppException("该活动不存在，无法取消");

        if (NumberUtil.compare(activity.getActivityEnd(), TimeHelper.timestamp()) < 0)
            throw new AppException("该活动已经结束，无法取消");

        activityJoinMapper.delete(activityJoinId);

        // 统计
        statActivityJoin(activityJoin.getActivityJoinActivityId());
    }


    /**
     * 统计数量
     */
    public void statActivityJoin(long id) {
        Where<ActivityJoinModel> whereJoin = new Where<>();
        whereJoin.eq("ACTIVITY_JOIN_ACTIVITY_ID", id);
        long cnt = activityJoinMapper.count(whereJoin);

        UpdateWhere<ActivityModel> uw = new UpdateWhere<>();
        uw.eq("ACTIVITY_ID", id);
        uw.set("ACTIVITY_JOIN_CNT", cnt);
        activityMapper.edit(uw);
    }

    /**
     * 按天获取报名项目
     */
    public List<Map<String, Object>> getActivityListByDay(String day) {
        long start = TimeHelper.time2Timestamp(day + " 00:00:00");
        long end = TimeHelper.time2Timestamp(day + " 23:59:59");

        Where<ActivityModel> where = new Where<>();
        where.eq("ACTIVITY_STATUS", ActivityModel.STATUS.NORMAL);
        where.between("ACTIVITY_START", start, end);

        where.orderByAsc("ACTIVITY_ORDER");
        where.orderByDesc("ACTIVITY_ID");

        String fields = "ACTIVITY_ID,ACTIVITY_TITLE,ACTIVITY_START,ACTIVITY_OBJ";

        List<Map<String, Object>> list = activityMapper.getAllListMap(where, fields);

        for (Map<String, Object> record : list) {
            FormHelper.fmtDBObj(record, "activityObj", "cover");
            record.put("timeDesc", TimeHelper.timestamp2Time(MapUtil.getLong(record, "activityStart"), "HH:mm"));
        }

        return list;
    }

    /**
     * 获取从某天开始可报名的日期
     *
     * @param {*} fromDay  日期 Y-M-D
     */
    public List<Map<String, Object>> getActivityHasDaysFromDay(String fromDay) {
        long start = TimeHelper.time2Timestamp(fromDay + " 00:00:00");


        Where<ActivityModel> where = new Where<>();
        where.eq("ACTIVITY_STATUS", ActivityModel.STATUS.NORMAL);
        where.ge("ACTIVITY_START", start);


        String fields = "ACTIVITY_START";

        List ret = new ArrayList();

        List<ActivityModel> list = activityMapper.getAllList(where, fields);
        for (ActivityModel activityModel : list) {
            String day = TimeHelper.timestamp2Time(activityModel.getActivityStart(), "yyyy-MM-dd");
            if (!ret.contains(day)) ret.add(day);
        }

        return ret;
    }
}
