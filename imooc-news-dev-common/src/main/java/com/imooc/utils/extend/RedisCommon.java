package com.imooc.utils.extend;

/**
 * @author hp
 * @date 2023-03-09 10:38
 * @explain
 */

public class RedisCommon {
    public static final String MOBILE_SMSCODE="mobile:smscode";
    public static final String REDIS_USER_TOKEN="redis_user_token";

    public static final String REDIS_USER_INFO="redis_user_info";

    public static final Integer MAX_REDIS_AGE=30*24*60*60;

    public static final String REDIS_ADMIN_TOKEN="redis_admin_token";

    public static final String REDIS_ADMIN_INFO="redis_admin_info";

    public static final String REDIS_ALL_CATEGROY="redis_all_categroy";
    public static final String REDIS_ALL_FRIENDLINK="redis_all_friendLink";

    public static final String REDIS_NOT_DELETE_FRIENDLINK="redis_not_delete_friendLink";
    public static final String REDIS_MY_FOLLOWED_COUNT="redis_my_followed_count";
    public static final String REDIS_MY_FANS_COUNT="redis_my_fans_count";

    public static final String REDIS_ARTICLE_READ_COUNT="redis_article_read_count";

    public static final String REDIS_IS_ME_READ="redis_is_me_read";

    public static final String REDIS_ARTICLE_COMMENT_COUNT="redis_article_comment_count";
}