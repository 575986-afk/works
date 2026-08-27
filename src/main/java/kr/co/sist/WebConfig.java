package kr.co.sist;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.sist.audit.Audit2FAInterceptor;
import kr.co.sist.login.LoginInterceptor;
import kr.co.sist.security.RoleLevelInterceptor;
import kr.co.sist.sysadmin.AdminSessionInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer{
	
	private final LoginInterceptor loginInterceptor;
	private final Audit2FAInterceptor audit2FAInterceptor;
	private final RoleLevelInterceptor roleLevelInterceptor;
	private final AdminSessionInterceptor adminSessionInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/**")
		.addResourceLocations("file:///C:/upload/");
	}
	
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/",
                    "/admin/**",
                    "/policy/**",
                    "/error",        
                    "/favicon.ico",
                    "/user/notices",
                    "/notice/notice",
                    "/login",
                    "/works/login/**",
                    "/findId",
                    "/findPw",
                    "/joinForm",
                    "/pwUpdate",
                    "/signupChk",
                    "/verificationChk",
                    "/userJoinForm",
                    "/managerJoinForm",
                    "/loginProcess",
                    "/checkUser",
                    "/verifyCodeProcess",
                    "/resetPwProcess",
                    "/findIdProcess",
                    "/idDup",
                    "/userJoin",
                    "/managerJoin",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/common/**",
                    "/upload/**"
                );

        // 권한 레벨 인터셉터
        registry.addInterceptor(roleLevelInterceptor)
                .addPathPatterns(
                    "/adminUser/company/info",
                    "/adminUser/member/member",
                    "/adminUser/member/organization",
                    "/adminUser/member/group",
                    "/adminUser/member/title",
                    "/adminUser/security/authority",
                    "/adminUser/service/calendar",
                    "/adminUser/audit/loginAudit",
                    "/adminUser/audit/calAudit",
                    "/adminUser/audit/todoAudit",
                    "/adminUser/audit/addrAudit"
                );
        
       // 감사 2단계 인증 인터셉터
       registry.addInterceptor(audit2FAInterceptor)
               .addPathPatterns(
                   "/adminUser/audit/loginAudit",
                   "/adminUser/audit/calAudit",
                   "/adminUser/audit/todoAudit",
                   "/adminUser/audit/addrAudit"
              );
              
       // 시스템관리자
       registry.addInterceptor(adminSessionInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
   }
}