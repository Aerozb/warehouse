package com.yhy.sys.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yhy.sys.common.MyFileUtil;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class MyConfig implements WebMvcConfigurer {


    /**
     * 路由
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("sys/toLogin").setViewName("system/index/login");
        registry.addViewController("sys/index").setViewName("system/index/index");
        registry.addViewController("sys/toDeskManager").setViewName("system/index/deskManager");
        registry.addViewController("sys/toLoginfoManager").setViewName("system/loginfo/loginfoManager");
        registry.addViewController("sys/toNoticeManager").setViewName("system/notice/noticeManager");
        registry.addViewController("sys/toAddOrUpdate").setViewName("system/notice/addOrUpdate");
        registry.addViewController("sys/toDeptManager").setViewName("system/dept/deptManager");
        registry.addViewController("sys/toDeptLeft").setViewName("system/dept/deptLeft");
        registry.addViewController("sys/toDeptRight").setViewName("system/dept/deptRight");
        registry.addViewController("sys/toMenuManager").setViewName("system/menu/menuManager");
        registry.addViewController("sys/toMenuLeft").setViewName("system/menu/menuLeft");
        registry.addViewController("sys/toMenuRight").setViewName("system/menu/menuRight");
        registry.addViewController("sys/toPermissionManager").setViewName("system/permission/permissionManager");
        registry.addViewController("sys/toPermissionLeft").setViewName("system/permission/permissionLeft");
        registry.addViewController("sys/toPermissionRight").setViewName("system/permission/permissionRight");
        registry.addViewController("sys/toRoleManager").setViewName("system/role/roleManager");
        registry.addViewController("sys/toUserManager").setViewName("system/user/userManager");
        registry.addViewController("bus/toCustomerManager").setViewName("business/customer/customerManager");
        registry.addViewController("bus/toProviderManager").setViewName("business/provider/providerManager");
        registry.addViewController("bus/toGoodsManager").setViewName("business/goods/goodsManager");


    }

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/image/**").addResourceLocations(MyFileUtil.IMAGE_DIRECTORY);
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    /**
     * 去除LocalDateTime序列化后的T ---开始
     */
    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 去除LocalDateTime序列化后的T ---结束
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }
}