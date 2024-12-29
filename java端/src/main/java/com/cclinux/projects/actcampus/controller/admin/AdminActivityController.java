package com.cclinux.projects.actcampus.controller.admin;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cclinux.framework.annotation.DemoShow;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.actcampus.model.ActivityModel;
import com.cclinux.projects.actcampus.service.admin.AdminActivityService;
import com.cclinux.projects.actcampus.service.cust.ActivityService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("ActCampusAdminActivityController")
public class AdminActivityController extends BaseMyAdminController {

    @Resource(name = "ActCampusAdminActivityService")
    private AdminActivityService adminActivityService;

    @Resource(name = "ActCampusActivityService")
    private ActivityService activityService;


    @RequestMapping(value = "/admin/activity/list")
    public ApiResult getAdminActivityList(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = PageParams.PAGE_CHECK_RULE;
        DataCheck.check(input, RULES);

        // 业务
        PageParams pageParams = new PageParams(input);
        PageResult ret = adminActivityService.getAdminActivityList(pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {

            record.put("statusDesc", activityService.getActivityStatusDesc(record));

            FormHelper.removeField(record, "activityForms");
            FormHelper.removeField(record, "activityObj");

            TimeHelper.db2Time(record, "activityStart", "yyyy-MM-dd HH:mm");
            TimeHelper.db2Time(record, "activityEnd", "yyyy-MM-dd HH:mm");
            TimeHelper.db2Time(record, "activityStop", "yyyy-MM-dd HH:mm");
        }

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/admin/activity/join/list")
    public ApiResult getAdminActivityJoinList(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES =  {
                "activityId:must|int",
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
        PageResult ret = adminActivityService.getAdminActivityJoinList(pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {

            TimeHelper.db2Time(record, "activityJoinCheckTime");
            FormHelper.db2Forms(record, "activityJoinForms");
            FormHelper.removeField(record, "activityJoinObj");
        }

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/admin/activity/join/check")
    public ApiResult checkinActivityJoin(@RequestBody Map<String, Object> input) {

        // 数据校验
        // 数据校验
        String[] RULES = {
                "activityJoinId:must|long",
                "flag:must|int"
        };
        DataCheck.check(input, RULES);

        // 业务
        long activityId = MapUtil.getLong(input, "activityJoinId");
        int flag = MapUtil.getInt(input, "flag");
        adminActivityService.checkinActivityJoin(activityId, flag);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/activity/del")
    public ApiResult delActivity(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long"
        };
        DataCheck.check(input, RULES);

        // 业务
        long activityId = MapUtil.getLong(input, "id");
        adminActivityService.delActivity(activityId);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/activity/join/del")
    public ApiResult delActivityJoin(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "activityJoinId:must|long"
        };
        DataCheck.check(input, RULES);

        // 业务
        long activityJoinId = MapUtil.getLong(input, "activityJoinId");
        adminActivityService.delActivityJoin(activityJoinId);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/activity/status")
    public ApiResult statusActivity(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "status:must|int|name=状态"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int status = MapUtil.getInt(input, "status");
        adminActivityService.statusActivity(id, status);

        Map<String, Object> ret = new HashMap<>();
        ret.put("statusDesc", activityService.getActivityStatusDesc(id));

        return ApiResult.success(ret);
    }

    @DemoShow
    @RequestMapping(value = "/admin/activity/order")
    public ApiResult orderActivity(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "order:must|int|name=排序号"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int order = MapUtil.getInt(input, "order");
        adminActivityService.orderActivity(id, order);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/activity/vouch")
    public ApiResult vouchActivity(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "vouch:must|int"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int vouch = MapUtil.getInt(input, "vouch");
        adminActivityService.vouchActivity(id, vouch);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/activity/insert")
    public ApiResult insertActivity(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "title:must|string|min=4|max=50|name=标题",
                "cateId:must|long|name=分类",
                "cateName:must|string|name=分类名",
                "order:must|int|min=0|max=9999|name=排序号",
                "maxCnt:must|int|name=人数上限",
                "start:must|string|name=活动开始时间",
                "end:must|string|name=活动结束时间",
                "stop:must|string|name=报名截止时间",
                "address:must|string|name=活动地址",
                "addressGeo:must|string|name=活动地址GEO",
                "forms:must|string|name=表单",
                "obj:must|string|name=表单",
        };
        DataCheck.check(input, RULES);

        // 业务
        ActivityModel activity = this.inputToActivity(input);

        long id = adminActivityService.insertActivity(activity);


        return ApiResult.success(MapUtil.of("id", id));
    }

    @DemoShow
    @RequestMapping(value = "/admin/activity/edit")
    public ApiResult editActivity(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "title:must|string|min=4|max=50|name=标题",
                "cateId:must|long|name=分类",
                "cateName:must|string|name=分类名",
                "order:must|int|min=0|max=9999|name=排序号",
                "maxCnt:must|int|name=人数上限",
                "start:must|string|name=活动开始时间",
                "end:must|string|name=活动结束时间",
                "stop:must|string|name=报名截止时间",
                "address:must|string|name=活动地址",
                "addressGeo:must|string|name=活动地址GEO",
                "forms:must|string|name=表单",
                "obj:must|string|name=表单",
        };
        DataCheck.check(input, RULES);

        // 业务
        ActivityModel activity = this.inputToActivity(input);

        adminActivityService.editActivity(activity);

        String statusDesc = activityService.getActivityStatusDesc(activity.getActivityId());

        return ApiResult.success(MapUtil.of("statusDesc", statusDesc));
    }


    @RequestMapping(value = "/admin/activity/detail")
    public ApiResult getActivityDetail(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        Map<String, Object> ret = adminActivityService.getActivityDetail(id);
        if (ObjectUtil.isNotEmpty(ret)) {
            TimeHelper.db2Time(ret, "activityStart", "yyyy-MM-dd HH:mm");
            TimeHelper.db2Time(ret, "activityEnd", "yyyy-MM-dd HH:mm");
            TimeHelper.db2Time(ret, "activityStop", "yyyy-MM-dd HH:mm");

        }


        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/admin/activity/join/export")
    public ApiResult exportActivityJoinExcel(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "activityId:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long activityId = MapUtil.getLong(input, "activityId");
        Map<String, Object> ret = adminActivityService.exportActivityJoinExcel(activityId);

        return ApiResult.success(ret);
    }

    @DemoShow
    @RequestMapping(value = "/admin/activity/join/clear")
    public ApiResult clearActivityAll(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "activityId:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long activityId = MapUtil.getLong(input, "activityId");
        adminActivityService.clearActivityAll(activityId);

        return ApiResult.success();
    }

    @RequestMapping(value = "/admin/activity/join/scan")
    public ApiResult scanActivityJoin(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "activityId:must|id|name=id",
                "code:must|string|name=核销码"
        };
        DataCheck.check(input, RULES);

        // 业务
        long activityId = MapUtil.getLong(input, "activityId");
        String code = MapUtil.getStr(input, "code");
        adminActivityService.scanActivityJoin(activityId, code);

        return ApiResult.success();
    }

    private ActivityModel inputToActivity(Map<String, Object> input) {
        ActivityModel activity = new ActivityModel();

        activity.setActivityId(MapUtil.getLong(input, "id"));

        activity.setActivityTitle(MapUtil.getStr(input, "title"));
        activity.setActivityCateId(MapUtil.getLong(input, "cateId"));
        activity.setActivityCateName(MapUtil.getStr(input, "cateName"));
        activity.setActivityOrder(MapUtil.getInt(input, "order"));


        activity.setActivityMaxCnt(MapUtil.getLong(input, "maxCnt"));

        String start = MapUtil.getStr(input, "start") + ":00";
        activity.setActivityStart(TimeHelper.time2Timestamp(start));

        String end = MapUtil.getStr(input, "end") + ":00";
        activity.setActivityEnd(TimeHelper.time2Timestamp(end));

        String stop = MapUtil.getStr(input, "stop") + ":00";
        activity.setActivityStop(TimeHelper.time2Timestamp(stop));

        activity.setActivityAddress(MapUtil.getStr(input, "address"));
        activity.setActivityAddressGeo(MapUtil.getStr(input, "addressGeo"));

        activity.setActivityForms(MapUtil.getStr(input, "forms"));
        activity.setActivityObj(MapUtil.getStr(input, "obj"));

        return activity;
    }

}
