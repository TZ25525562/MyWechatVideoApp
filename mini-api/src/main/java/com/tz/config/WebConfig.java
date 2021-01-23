package com.tz.config;


import com.tz.Interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    /**
     * 将拦截器添加到Spring容器中
     * @return
     */
    @Bean
    public MiniInterceptor getMiniInterceptor(){
        return new MiniInterceptor();
    }

    /**
     * 将自定义拦截器添加到Web配置中，并设置拦截路径
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        访问用户的请求全部拦截
        registry.addInterceptor(new MiniInterceptor()).addPathPatterns("/user/**")
        .addPathPatterns("/bgm/**").addPathPatterns("/video/uploadVideo","video/uploadCover","/video/userLike",
                "/video/userUnlike","/video/saveComment").excludePathPatterns("/user/queryPublisher");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:D:\\仿抖音小程序\\file-dev\\");
    }


}
