package com.gauravpathak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Spring Security OAuth exposes two endpoints for checking tokens (/oauth/check_token and /oauth/token_key).
     * Those endpoints are not exposed by default (have access "denyAll()").
     * So if you want to verify the tokens with this endpoint you'll have to add this to your authorization servers' config:
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }

    /**
     * Oauth2 authenticates client apps for some access types about user's information.
     * In your example, it configures a client app with name clientapp.
     * inMemory means all the necessary data to create a session will be stored in memory. When you restart your
     * application, all the session data will be gone, which means users need to login and authenticate again.
     * Grant types represent the rights of the client app over user's information. In this case client app have
     * rights to read and write user's password and refresh_token.
     * .authorizedGrantTypes("password", "refresh_token")
     * .scopes("read", "write")
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("clientapp")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("password")
                .scopes("all")
                .and()
                .withClient("resource-server")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("password")
                .scopes("all");
    }

    /**
     * AuthorizationServerEndpointsConfigurer configures strategy how token will be stored and provided.
     * Configure the properties and enhanced functionality of the Authorization Server endpoints.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
