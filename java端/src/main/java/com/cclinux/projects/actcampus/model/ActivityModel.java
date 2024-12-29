package com.cclinux.projects.actcampus.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cclinux.framework.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Notes: 活动实体
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/07/12 7:18
 * @Ver: ccminicloud-framework 3.2.1
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("actcampus_activity")
public class ActivityModel extends BaseModel {
    /**
     * 状态
     */
    public static final class STATUS {
        public static final int STOP = 0; //非正常
        public static final int NORMAL = 1; // 使用中
    }


    @TableId(value = "ACTIVITY_ID", type = IdType.AUTO)
    private Long activityId;

    @TableField("ACTIVITY_TITLE")
    private String activityTitle;

    @TableField("ACTIVITY_STATUS")
    private int activityStatus;

    @TableField("ACTIVITY_CATE_ID")
    private long activityCateId;

    @TableField("ACTIVITY_CATE_NAME")
    private String activityCateName;

    // 最大人数
    @TableField("ACTIVITY_MAX_CNT")
    private long activityMaxCnt;

    // 开始时间
    @TableField("ACTIVITY_START")
    private long activityStart;

    // 结束时间
    @TableField("ACTIVITY_END")
    private long activityEnd;

    // 停止报名时间
    @TableField("ACTIVITY_STOP")
    private long activityStop;

    @TableField("ACTIVITY_ORDER")
    private int activityOrder;

    @TableField("ACTIVITY_VOUCH")
    private int activityVouch;

    @TableField("ACTIVITY_VIEW_CNT")
    private long activityViewCnt;

    @TableField("ACTIVITY_JOIN_CNT")
    private long activityJoinCnt;

    @TableField("ACTIVITY_FORMS")
    private String activityForms;

    @TableField("ACTIVITY_OBJ")
    private String activityObj;

    @TableField("ACTIVITY_ADDRESS")
    private String activityAddress;

    @TableField("ACTIVITY_ADDRESS_GEO")
    private String activityAddressGeo;


}
