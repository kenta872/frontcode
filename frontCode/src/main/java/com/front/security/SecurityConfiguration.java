package com.front.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.front.security.account.AccountService;
import com.front.util.Constants;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
    private AccountService userService;
		
	Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * アカウント情報DB登録時に使用するパスワードエンコーダー
	 * @return
	 */
    @Bean
    public PasswordEncoder passwordEncoder() {

    	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    	
    	//これはハッシュ化済みの値をDBに登録する確認用に出力させるコード//
//    	String password = "admin";
//        String digest = bCryptPasswordEncoder.encode(password);
//        System.out.println("ハッシュ値 = " + digest);
        ///////////////////////////////////////////////////////////////

        return new BCryptPasswordEncoder();
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    //  (2) 主に全体に対するセキュリティ設定を行う
          web.ignoring().antMatchers(Constants.STATIC_RESOURCES);
	}
//	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
        .authorizeRequests()
            // アクセス権限の設定
            // アクセス制限の無いURL
            .antMatchers("/", "/upload","/download" , "/check", "/save", "/comp", "/cancel", "/createAccount", "/accountRegist","/error","/toError","/login").permitAll()
            // ADMINユーザーのみに許可
            .antMatchers("/admin/**").hasRole("ADMIN")
            // その他は認証済みであること
            .anyRequest()
            .authenticated()
            .and()
        // ログイン処理
        .formLogin()
            .loginPage("/login").permitAll()
            .loginProcessingUrl("/login")
            .usernameParameter("username") // ＜＝"username"というname名からデータを取得してくる
  	        .passwordParameter("password") // ＜＝"password"というname名からデータを取得してくる
            .defaultSuccessUrl("/")
  	        .failureUrl("/toError")
            .permitAll()
		    .and()
        // ログアウト処理
		.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            // ログアウト成功時の遷移先
            .logoutSuccessUrl("/login")
            // ログアウト時に削除するクッキー名
            .deleteCookies("JSESSIONID")
            // ログアウト時のセッション破棄を有効化
            .invalidateHttpSession(true)
			.and()
        .exceptionHandling()
        	.accessDeniedPage("/toError");

		
	}
	// 独自の認証処理を実装する場合はオーバーライドする
	// オーバーライドしなければapplication.propertiesに設定したログイン情報がエフォルトに
	@Override
	protected void configure(AuthenticationManagerBuilder auth)  throws Exception {
        //  (4) 主に認証方法の実装の設定を行う
//		auth.authenticationProvider(new AuthProvider());
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}
	

}
