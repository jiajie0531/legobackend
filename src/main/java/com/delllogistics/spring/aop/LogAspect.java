package com.delllogistics.spring.aop;

import com.delllogistics.dto.BackendUser;
import com.delllogistics.dto.Result;
import com.delllogistics.entity.sys.SystemLog;
import com.delllogistics.security.TokenUserDetails;
import com.delllogistics.service.sys.SystemLogService;
import com.delllogistics.spring.annotation.LogAnnotation;
import com.delllogistics.util.HttpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Log Aspect.<br/>
 * User: jiajie<br/>
 * Date: 28/01/2018<br/>
 * Time: 3:01 PM<br/>
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private final SystemLogService systemLogService;

    @Autowired
    public LogAspect(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    @Pointcut("execution(* com.delllogistics.controller..*.*(..))&&@annotation(com.delllogistics.spring.annotation.LogAnnotation)")
    public void log() {}

    /**
     * 前置通知
     */
    @Before("log()")
    public void doBeforeController() {
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 后置通知
     */
    @AfterReturning(pointcut = "log()", returning = "retValue")
    public void doAfterController(JoinPoint joinPoint, Object retValue) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        String username = getUsername();
        String methodName = joinPoint.getSignature().getName();
        if(username==null&&methodName.equals("login")){
            username=getLoginUsername(joinPoint);
        }
        String ip = HttpUtils.getIP(request);
        String methodAllName = joinPoint.getSignature().getDeclaringTypeName() + "." + methodName;
        List<Object> recordParams = getRecordParams(logAnnotation, joinPoint);
        long spendTime = System.currentTimeMillis() - startTime.get();
        String  userAgent  =   request.getHeader("User-Agent");
        logger.info("username:{}", username);
        logger.info("ip : " + ip);
        if(logger.isDebugEnabled()){
            logger.debug("action名称 " + logAnnotation.value());
            logger.debug("username:{}", username);
            logger.debug("IP : " + ip);
            logger.debug("userAgent:{}",userAgent);
            logger.debug("CLASS_METHOD : " + methodAllName);
            logger.debug("ARGS :  " + recordParams);
            logger.debug("RESPONSE : " + retValue);
            logger.debug("SPEND TIME : " + spendTime);
        }
        SystemLog systemLog = new SystemLog();
        systemLog.setUsername(username);
        systemLog.setIp(ip);
        systemLog.setRequestMethod(methodAllName);
        if(recordParams!=null){
            systemLog.setRequestParams(recordParams.toString());
        }
        systemLog.setResultMsg(getResultMsg(retValue));
        systemLog.setOperation(logAnnotation.value());
        systemLog.setSpendTime(spendTime);
        systemLog.setUserAgent(userAgent);

        systemLogService.save(systemLog);

    }

    private List<Object> getRecordParams(LogAnnotation logAnnotation,JoinPoint joinPoint){
        int[] paramIndexs = logAnnotation.paramIndexs();
        if(paramIndexs.length!=0){
            Object[] args = joinPoint.getArgs();
            List<Object> list=null;
            for (int paramIndex : paramIndexs) {
                Object obj=args[paramIndex];
                if(obj!=null){
                    if(list==null){
                        list=new ArrayList<>();
                    }
                    list.add(obj);
                }else{
                    logger.warn("{},参数【{}】index不存在",logAnnotation.value(),paramIndex);
                }
            }
            return list;
        }
        return null;
    }


    private String getResultMsg(Object obj){
        if(obj instanceof Result){
            return ((Result) obj).getMsg();
        }
        return null;
    }

    private String getLoginUsername(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof BackendUser){
                BackendUser backendUser= (BackendUser) arg;
                return backendUser.getUsername();
            }
        }
        return null;
    }

    private String getUsername(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal!=null&&principal instanceof TokenUserDetails){
            TokenUserDetails userDetails = (TokenUserDetails) principal;
            return userDetails.getUsername();
        }

        return null;
    }
}
