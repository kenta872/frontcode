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

import com.front.security.account.AccountService;
import com.front.util.Constants;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
    private AccountService userService;
		
	Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String[] STATIC_RESOURCES = {
            "/**/css/**", "/**/js/**", "/**/img/**",
        };
//	/**
//	 * アカウント情報DB登録時に使用するパスワードエンコーダー
//	 * @return
//	 */
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
        // 主にURLごとに異なるセキュリティ設定を行う
		/* Basic認証の場合 */
        // (3) Basic認証の対象となるパス
        http.antMatcher("/admin/**");
        // (5) 対象のすべてのパスに対して認証を有効にする
        http.authorizeRequests().anyRequest().authenticated();
        // (4) Basic認証を指定
        http.httpBasic();
        // (6) すべてのリクエストをステートレスとして設定
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        
//        /* ログインページ対応の場合 */
//        http
//        // AUTHORIZE
//        .authorizeRequests()
////        .mvcMatchers("/","/upload","/delete","/codeCheck","/comp").permitAll() // ＜＝ログイン認証対象外のURL
//        .antMatchers("/","/upload","/delete","/codeCheck","/comp"). permitAll() // <- antMatchersはURLのみが対象、mvcMatchersはファイル名なども対象
//        .anyRequest()
//        .authenticated()
//        .and()
//        // LOGIN
//        .formLogin()
//        .loginPage("/login")
//        .permitAll()
//        .defaultSuccessUrl("/")
//        .usernameParameter("username") // ＜＝"username"というname名からデータを取得してくる
//	    .passwordParameter("password"); // ＜＝"password"というname名からデータを取得してくる
        
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
