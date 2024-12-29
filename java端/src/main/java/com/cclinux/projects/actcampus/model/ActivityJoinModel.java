package com.cclinux.projects.actcampus.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cclinux.framework.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Notes: 活动报名实体
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/11/30 7:18
 * @Ver: ccminicloud-framework 3.2.1
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("actcampus_activity_join")
public class ActivityJoinModel extends BaseModel {
    /**
     * 状态
     */
    public static final class STATUS {
        public static final int STOP = 0; //非正常
        public static final int NORMAL = 1; // 正常
    }


    @TableId(value = "ACTIVITY_JOIN_ID", type = IdType.AUTO)
    private Long activityJoinId;

    @TableField("ACTIVITY_JOIN_ACTIVITY_ID")
    private long activityJoinActivityId;

    @TableField("ACTIVITY_JOIN_CODE")
    private String activityJoinCode;

    @TableField("ACTIVITY_JOIN_IS_CHECK")
    private int activityJoinIsCheck;

    @TableField("ACTIVITY_JOIN_CHECK_TIME")
    private long activityJoinCheckTime;

    @TableField("ACTIVITY_JOIN_USER_ID")
    private long activityJoinUserId;

    @TableField("ACTIVITY_JOIN_FORMS")
    private String activityJoinForms;

    @TableField("ACTIVITY_JOIN_OBJ")
    private String activityJoinObj;

    @TableField("ACTIVITY_JOIN_STATUS")
    private int activityJoinStatus;


}
