package com.cclinux.projects.actcampus.controller.cust;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.actcampus.service.cust.ActivityService;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Notes: 资讯模块
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/7 5:57
 * @Ver: ccminicloud-framework 3.2.1
 */


@RestController("ActCampusActivityController")
public class ActivityController extends BaseMyCustController {

    @Resource(name = "ActCampusActivityService")
    ActivityService activityService;

    @LoginIgnore
    @RequestMapping(value = "/activity/view")
    public ApiResult view(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {

        // 数据校验
        String[] RULES = {
                "id:must|id|name=id"
        };
        DataCheck.check(input, RULES);


        // 业务
        long id = MapUtil.getLong(input, "id");
        Map<String, Object> ret = activityService.view(id, userId);


        if (ObjectUtil.isNotEmpty(ret)) {

            String start = TimeHelper.timestamp2Time(MapUtil.getLong(ret, "activityStart"), "yyyy-MM-dd HH:mm");
            String end = TimeHelper.timestamp2Time(MapUtil.getLong(ret, "activityEnd"), "yyyy-MM-dd HH:mm");
            String time = start + " ~ " + end;
            ret.put("time", time);

            TimeHelper.db2Time(ret, "activityStop", "yyyy-MM-dd HH:mm");

            ret.put("statusDesc", activityService.getActivityStatusDesc(id));
            ret.remove("activityForms");

        }


        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/activity/detail/for/join")
    public ApiResult detailForActivityJoin(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {

        // 数据校验
        String[] RULES = {
                "activityId:must|id|name=id"
        };
        DataCheck.check(input, RULES);


        // 业务
        long activityId = MapUtil.getLong(input, "activityId");
        Map<String, Object> ret = activityService.detailForActivityJoin(userId, activityId);

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/activity/join")
    public ApiResult activityJoin(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {

        // 数据校验
        String[] RULES = {
                "activityId:must|id|name=id",
                "forms:string|name=表单",
                "obj:string|name=表单",
        };
        DataCheck.check(input, RULES);


        // 业务
        long activityId = MapUtil.getLong(input, "activityId");
        String forms = MapUtil.getStr(input, "forms");
        String obj = MapUtil.getStr(input, "obj");

        activityService.activityJoin(userId, activityId, forms, obj);

        return ApiResult.success();
    }

    @RequestMapping(value = "/activity/my/join/cancel")
    public ApiResult cancelMyActivityJoin(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {

        // 数据校验
        String[] RULES = {
                "activityJoinId:must|id|name=id"
        };
        DataCheck.check(input, RULES);


        // 业务
        long activityJoinId = MapUtil.getLong(input, "activityJoinId");

        activityService.cancelMyActivityJoin(userId, activityJoinId);

        return ApiResult.success();
    }


    @LoginIgnore
    @RequestMapping(value = "/activity/list")
    public ApiResult getActivityList(@RequestBody Map<String, Object> input) {
        // 数据校验
        String[] RULES = {
                "cateId:int",
                "search:string|name=搜索条件",
                "page:must|int|default=1",
                "size:must|int|size|default=10",
                "isTotal:bool",
                "sortType:string|name=搜索类型",
                "sortVal:string|name=搜索类型值",
                "orderBy:string|name=排序",
                "whereEx:string|name=附加查询条件",
                "oldTotal:int"
        };
        DataCheck.check(input, RULES);

        // 业务
        PageParams pageParams = new PageParams(input);
        PageResult ret = activityService.getActivityList(pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {
            record.put("statusDesc", activityService.getActivityStatusDesc(record));

            TimeHelper.db2Time(record, "activityStart", "yyyy-MM-dd HH:mm");

            FormHelper.fmtDBObjExclude(record, "activityObj", "desc");

        }

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/activity/my/join/list")
    public ApiResult getMyActivityJoinList(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {
        // 数据校验
        String[] RULES = PageParams.PAGE_CHECK_RULE;
        DataCheck.check(input, PageParams.PAGE_CHECK_RULE);

        // 业务
        PageParams pageParams = new PageParams(input);
        PageResult ret = activityService.getMyActivityJoinList(userId, pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {
            TimeHelper.db2Time(record, "activityStart", "yyyy-MM-dd HH:mm");
            TimeHelper.db2Time(record, "activityEnd", "yyyy-MM-dd HH:mm");
        }

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/activity/my/join/detail")
    public ApiResult getMyActivityJoinDetail(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {
        // 数据校验
        String[] RULES = {
                "activityJoinId:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long activityJoinId = MapUtil.getLong(input, "activityJoinId");
        Map<String, Object> ret = activityService.getMyActivityJoinDetail(userId, activityJoinId);

        if (ObjectUtil.isNotEmpty(ret)) {

            Map<String, Object> activity = BeanUtil.beanToMap(ret.get("activity"));
            String start = TimeHelper.timestamp2Time(MapUtil.getLong(activity, "activityStart"), "yyyy-MM-dd HH:mm");
            String end = TimeHelper.timestamp2Time(MapUtil.getLong(activity, "activityEnd"), "yyyy-MM-dd HH:mm");
            String time = start + " ~ " + end;
            ret.put("time", time);

            TimeHelper.db2Time(ret, "activityJoinCheckTime", "yyyy-MM-dd HH:mm:ss");
        }

        return ApiResult.success(ret);
    }

    @LoginIgnore
    @RequestMapping(value = "/activity/list/by/day")
    public ApiResult getActivityListByDay(@RequestBody Map<String, Object> input) {
        // 数据校验
        String[] RULES = {
                "day:must|string|name=日期"
        };
        DataCheck.check(input, RULES);

        // 业务
        String day = MapUtil.getStr(input, "day");
        Object ret = activityService.getActivityListByDay(day);

        return ApiResult.success(ret);
    }

    @LoginIgnore
    @RequestMapping(value = "/activity/list/has/day")
    public ApiResult getActivityHasDaysFromDay(@RequestBody Map<String, Object> input) {
        // 数据校验
        String[] RULES = {
                "fromDay:must|string|name=日期"
        };
        DataCheck.check(input, RULES);

        // 业务
        String fromDay = MapUtil.getStr(input, "fromDay");
        Object ret = activityService.getActivityHasDaysFromDay(fromDay);

        return ApiResult.success(ret);
    }
}
