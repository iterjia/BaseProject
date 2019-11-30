package com.hy.common.constant;

public interface CmnConst {
    String HTTP_ATTR_LOGIN_USER = "SESSION_ATTR_LOGIN_USER";
    String HTTP_HEADER_TOKEN = "token";
    String EVAL_STATUS_COMPLETED = "1";
    String ADMIN = "admin";

    int TITLE_LV1 = 1; //普通员工
    int TITLE_LV2 = 1<<1; //考评小组成员
    int TITLE_LV3 = 1<<2; //室主任
    int TITLE_LV4 = 1<<3; //总监
    int TITLE_LV5 = 1<<4; //所长
    int TITLE_LV6 = 1<<5; //部长

    int EXPLAIN_POLICY_RESPECTIVE = 0; //评价项逐项说明
    int EXPLAIN_POLICY_SYNCRETIC = 1; //所有评价项说明内容合一

    int MATCH_DEPT_FLAG_LV0 = 0; //与被评价人同部门
    int MATCH_DEPT_FLAG_LV1 = 1; //与被评价人同上级部门

    int FLAG_FALSE = 0;
    int FLAG_TRUE = 1;

    int SCORE_POLICY_0_10 = 0; //评分为0-10分，0代表不清楚
    int SCORE_POLICY_1_10 = 1; //评分为1-10分
    int SCORE_POLICY_EVEN = 2; //评分为偶数
}
