package com.cclinux.projects.actcampus.service.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.helper.FileHelper;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.actcampus.comm.ProjectConfig;
import com.cclinux.projects.actcampus.mapper.ActivityJoinMapper;
import com.cclinux.projects.actcampus.mapper.ActivityMapper;
import com.cclinux.projects.actcampus.model.ActivityJoinModel;
import com.cclinux.projects.actcampus.model.ActivityModel;
import com.cclinux.projects.actcampus.service.cust.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Notes: 活动业务逻辑
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/7/15 12:10
 * @Ver: ccminicloud-framework 3.2.1
 */


@Service("ActCampusAdminActivityService")
public class AdminActivityService extends BaseMyAdminService {


    @Resource(name = "ActCampusActivityMapper")
    private ActivityMapper activityMapper;

    @Resource(name = "ActCampusActivityJoinMapper")
    private ActivityJoinMapper activityJoinMapper;

    @Resource(name = "ActCampusActivityService")
    private ActivityService activityService;


    /**
     * 添加活动
     */
    public long insertActivity(ActivityModel activity) {
        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");
        return 0;
    }

    /**
     * 修改活动
     */
    public void editActivity(ActivityModel activity) {

        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }


    /**
     * 活动列表
     */
    public PageResult getAdminActivityList(PageParams pageRequest) {

        Where<ActivityModel> where = new Where<>();

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
                case "status": {
                    where.eq("ACTIVITY_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "vouch": {
                    where.eq("ACTIVITY_VOUCH", 1);
                    break;
                }
                case "top": {
                    where.eq("ACTIVITY_ORDER", 0);
                    break;
                }
                case "sort": {
                    logger.info("SortVal" + sortVal);
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByAsc("ACTIVITY_ORDER");
        where.orderByDesc("ACTIVITY_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return activityMapper.getPageList(page, where, "*");
    }

    /**
     * 活动报名名单
     */
    public PageResult getAdminActivityJoinList(PageParams pageRequest) {

        Where<ActivityJoinModel> where = new Where<>();


        long activityId = pageRequest.getParamLong("activityId");
        where.eq("ACTIVITY_JOIN_ACTIVITY_ID", activityId);

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(wrapper -> {
                wrapper.or().like("ACTIVITY_JOIN_OBJ", search);
            });
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "check": {
                    where.eq("ACTIVITY_JOIN_IS_CHECK", Convert.toInt(sortVal));
                    break;
                }
                case "status": {
                    where.eq("ACTIVITY_JOIN_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "sort": {
                    logger.info("SortVal" + sortVal);
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByDesc("ACTIVITY_JOIN_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return activityJoinMapper.getPageList(page, where, "*");
    }

    /**
     * 删除活动
     */
    public void delActivity(long id) {
        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");

    }

    /**
     * 删除活动报名
     */
    public void delActivityJoin(long activityJoinId) {

        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }


    /**
     * 获取单个活动
     */
    public Map<String, Object> getActivityDetail(long id) {
        return activityMapper.getOneMap(id);
    }

    /**
     * 修改活动状态
     */
    public void statusActivity(long id, int status) {
        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }


    /**
     * 管理员按钮核销
     */
    public void checkinActivityJoin(long activityJoinId, int flag) {
        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }

    /**
     * 修改排序
     */
    public void orderActivity(long id, int order) {
        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }


    /**
     * 首页设定
     */
    public void vouchActivity(long id, int vouch) {
        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }

    /**
     * 导出名单
     */
    public Map<String, Object> exportActivityJoinExcel(long activityId) {
        appError("{校园活动}该功能暂不开放，如有需要请加作者微信：cclinux0730");
        return null;
    }

    /**
     * 清空名单
     */
    public void clearActivityAll(long activityId) {
        Where<ActivityJoinModel> where = new Where<>();
        where.eq("ACTIVITY_JOIN_ACTIVITY_ID", activityId);
        activityJoinMapper.delete(where);

        UpdateWhere<ActivityModel> uw = new UpdateWhere<>();
        uw.eq("ACTIVITY_ID", activityId);
        uw.set("ACTIVITY_JOIN_CNT", 0);
        activityMapper.edit(uw);
    }

    /**
     * 管理员扫码核销
     */
    public void scanActivityJoin(long activityId, String code) {
        Where<ActivityJoinModel> where = new Where<>();
        where.eq("ACTIVITY_JOIN_ACTIVITY_ID", activityId);
        where.eq("ACTIVITY_JOIN_CODE", code);
        ActivityJoinModel activityJoin = activityJoinMapper.getOne(where);
        if (ObjectUtil.isEmpty(activityJoin)) throw new AppException("没有该用户的报名记录，核销失败");

        if (activityJoin.getActivityJoinStatus() != ActivityJoinModel.STATUS.NORMAL)
            throw new AppException("该用户未报名成功，核销失败");

        if (activityJoin.getActivityJoinIsCheck() == 1) throw new AppException("该用户已签到/核销，无须重复核销");

        UpdateWhere<ActivityJoinModel> uw = new UpdateWhere<>();
        uw.eq("ACTIVITY_JOIN_ACTIVITY_ID", activityId);
        uw.eq("ACTIVITY_JOIN_CODE", code);
        uw.set("ACTIVITY_JOIN_IS_CHECK", 1);
        uw.set("ACTIVITY_JOIN_CHECK_TIME", TimeHelper.timestamp());
        activityJoinMapper.edit(uw);
    }

}
