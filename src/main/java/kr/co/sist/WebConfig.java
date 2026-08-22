package kr.co.sist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.sist.audit.Audit2FAInterceptor;
import kr.co.sist.login.LoginInterceptor;
import kr.co.sist.security.RoleLevelInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer{
	
	private final LoginInterceptor loginInterceptor;
	private final Audit2FAInterceptor audit2FAInterceptor;
	private final RoleLevelInterceptor roleLevelInterceptor;

	/**
	 * 정적 리소스 매핑
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/**")
		.addResourceLocations("file:///c:/spring/upload/");
	}
	
	/**
    * 인터셉터 등록
    */
   @Override
   public void addInterceptors(InterceptorRegistry registry) {

//       // 로그인 인터셉터
//       registry.addInterceptor(loginInterceptor)
//               .addPathPatterns("/**")
//               .excludePathPatterns(
//                   "/",
//                   "/login",
//                   "/works/login/**",
//                   "/loginProcess",
//                   "/css/**",
//                   "/js/**",
//                   "/images/**"
//               );

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
   }
}