package com.yhy.sys.cache;


import cn.hutool.core.collection.CollectionUtil;
import com.yhy.sys.domain.Dept;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class CacheAspect {

    /**
     * 声明一个缓存容器
     */
    private Map<String, List<Dept>> CACHE_CONTAINER_LIST = new HashMap<>();
    private Map<String, Dept> CACHE_CONTAINER_ONE = new HashMap<>();
    private static final String CACHE_DEPT_PROFIX = "dept:";
    private static final String ALL = "all";

    @Pointcut("execution(* com.yhy.sys.service.impl.DeptServiceImpl.list())")
    public void pointcutList() {
    }

    @Pointcut("execution(* com.yhy.sys.service.impl.DeptServiceImpl.getById(..))")
    public void pointcutGet() {
    }

    @Pointcut("execution(* com.yhy.sys.service.impl.DeptServiceImpl.save(..))")
    public void pointcutSave() {
    }

    @Pointcut("execution(* com.yhy.sys.service.impl.DeptServiceImpl.updateById(..))")
    public void pointcutUpdateById() {
    }

    @Pointcut("execution(* com.yhy.sys.service.impl.DeptServiceImpl.removeById(..))")
    public void pointcutRemove() {
    }

    @Around("pointcutList()")
    public List<Dept> cacheDeptList(ProceedingJoinPoint joinPoint) {
        List<Dept> depts;
        try {
            //从缓存里面取
            depts = CACHE_CONTAINER_LIST.get(CACHE_DEPT_PROFIX + ALL);
            if (CollectionUtil.isNotEmpty(depts)) {
                System.out.println("我是从缓存中取的：集合"+depts.getClass().getSimpleName());
            } else {
                depts = (List<Dept>) joinPoint.proceed();
                CACHE_CONTAINER_LIST.put(CACHE_DEPT_PROFIX + ALL, depts);
                System.out.println("我是数据库中取的：集合"+depts.getClass().getSimpleName());
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
        return depts;
    }

    @Around("pointcutSave() || pointcutUpdateById()")
    public Boolean cacheDeptAdd(ProceedingJoinPoint joinPoint) {
        Boolean isSuccess;
        Dept dept = (Dept) joinPoint.getArgs()[0];
        try {
            isSuccess = (Boolean) joinPoint.proceed();
            if (isSuccess) {
                //从缓存里删除
                CACHE_CONTAINER_LIST.remove(CACHE_DEPT_PROFIX + ALL);
                CACHE_CONTAINER_ONE.remove(CACHE_DEPT_PROFIX + dept.getId());
                System.out.println("我从缓存中更新或添加了dept："+dept.getId()+"和集合");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
        return isSuccess;
    }

    @Around("pointcutRemove()")
    public Boolean cacheDeptDelete(ProceedingJoinPoint joinPoint) {
        Boolean isSuccess;
        Integer deptId = (Integer) joinPoint.getArgs()[0];
        try {
            isSuccess = (Boolean) joinPoint.proceed();
            if (isSuccess) {
                //从缓存里删除
                CACHE_CONTAINER_LIST.remove(CACHE_DEPT_PROFIX + ALL);
                CACHE_CONTAINER_ONE.remove(CACHE_DEPT_PROFIX + deptId);
                System.out.println("我从缓存中删除了dept："+deptId+"和集合");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
        return isSuccess;
    }

    @Around("pointcutGet()")
    public Dept cacheDeptGet(ProceedingJoinPoint joinPoint) {
        Integer deptId = (Integer) joinPoint.getArgs()[0];
        Dept dept = CACHE_CONTAINER_ONE.get(CACHE_DEPT_PROFIX + deptId);
        if (dept != null) {
            System.out.println("我是从缓存中取的：deptId="+dept.getId());
        } else {
            try {
                dept = (Dept) joinPoint.proceed();
                CACHE_CONTAINER_ONE.put(CACHE_DEPT_PROFIX + dept.getId(), dept);
                System.out.println("我是从数据库中取的：deptId="+dept.getId());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return dept;
    }


}
