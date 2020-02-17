package com.yhy.sys.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.hutool.core.util.ArrayUtil;
import com.yhy.sys.realm.UserRealm;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(value = {SecurityManager.class})
@ConfigurationProperties(prefix = "shiro")
@Data
public class ShiroAutoConfiguration {
    private static final String SHIRO_DIALECT = "shiroDialect";
    private static final String SHIRO_FILTER = "shiroFilter";
    /**
     * 加密方式
     */
    private String hashAlgorithmName;
    /**
     * 散列次数
     */
    private int hashIterations;
    /**
     * 默认的登陆页面
     */
    private String loginUrl = "/sys/toLogin";
    /**
     * 放行的路径
     */
    private String[] anonUrls;
    /**
     * 登出的路径
     */
    private String logOutUrl;
    /**
     * 拦截的路径
     */
    private String[] authcUlrs;

    /**
     * 声明凭证匹配器（密码校验交给Shiro的SimpleAuthenticationInfo进行处理)
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashIterations(hashIterations);
        credentialsMatcher.setHashAlgorithmName(hashAlgorithmName);
        return credentialsMatcher;
    }

    /**
     * 声明userRealm,将自己的验证方式加入容器
     */
    @Bean
    public UserRealm userRealm(CredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        // 注入凭证匹配器
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    /**
     * 配置SecurityManager,主要是Realm的管理认证
     */
    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注入userRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    @Bean(SHIRO_FILTER)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        // 设置未登陆的时要跳转的页面
        factoryBean.setLoginUrl(loginUrl);
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // 设置放行的路径
        if (ArrayUtil.isNotEmpty(anonUrls)) {
            for (String anonUrl : anonUrls) {
                filterChainDefinitionMap.put(anonUrl, "anon");
            }
        }
        // 设置登出的路径
        if (StringUtils.isNotEmpty(logOutUrl)) {
            filterChainDefinitionMap.put(logOutUrl, "logout");
        }
        // 设置拦截的路径
        if (ArrayUtil.isNotEmpty(authcUlrs)) {
            for (String authc : authcUlrs) {
                filterChainDefinitionMap.put(authc, "authc");
            }
        }
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }

    /**
     * 注册shiro的委托过滤器，相当于之前在web.xml里面配置的
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<DelegatingFilterProxy>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(SHIRO_FILTER);
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * 加入注解的使用，不加入这个注解不生效-----开始
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 加入注解的使用，不加入这个注解不生效-----结束
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用，上面两个方法必须添加，不然会报错
     */
    @Bean(name = SHIRO_DIALECT)
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
