package github.ax.meeting.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface AccessInterceptor {
    /** 用哪个字段作为拦截标识，未配置则默认走全部 */
    String key() default "all";
    /** 限制频次（每秒请求次数） */
    double permitsPerSecond() default 20;
    /** 黑名单拦截（多少次限制后加入黑名单）0 不限制 */
    double blacklistCount() default 0;
    /** 拦截后的执行方法 */
    String fallbackMethod() default "";
}
