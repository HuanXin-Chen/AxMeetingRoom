package github.ax.meeting.interceptor;

import github.ax.meeting.annotation.LoginCharacter;
import github.ax.meeting.util.Const;
import github.ax.meeting.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

// Token拦截器
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        //获取请求的角色注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        LoginCharacter character = method.getAnnotation(LoginCharacter.class);


        response.setCharacterEncoding("UTF-8");

        String token = request.getHeader("Authorization");
        if (token!=null){
            boolean result = TokenUtil.verify(token);
            request.setAttribute(Const.ATTR_USER_ROLE, TokenUtil.getNo(token));
            //确认登录状态检查当前角色是否有请求的目标方法的权限
            if (result){
                if(Objects.isNull(character)){  //没有注解的方法直接放行
                    return true;
                }
                String name = character.name();
                String checkToken = TokenUtil.getNo(token);
                if((checkToken.equals("admin")&&name.equals("admin"))||(!checkToken.equals("admin")&&name.equals("user"))){
//                    System.out.println("获取token，权限正确,拦截器放行");
                    return true;
                }
            }
        }

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        log.info("发现未授权请求，拦截器拦截到请求");

        return false;
    }
}
