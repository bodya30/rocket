package com.epam.mentoring.rocket.social;

import com.epam.mentoring.rocket.social.facebook.FacebookConnectionSignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import javax.sql.DataSource;

@Configuration
public class SocialConfig {

    private static final String FACEBOOK_CLIENT_ID = "spring.social.facebook.client.id";
    private static final String FACEBOOK_CLIENT_SECRET = "spring.social.facebook.client.secret";
    private static final String ENCRYPT_PASSWORD = "social.security.encrypt.password";
    private static final String ENCRYPT_SALT = "social.security.encrypt.salt";

    @Autowired
    private Environment environment;

    @Autowired
    private FacebookConnectionSignUp facebookConnectionSignUp;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SignInAdapter facebookSignInAdapter;

    @Bean
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(
                environment.getProperty(FACEBOOK_CLIENT_ID),
                environment.getProperty(FACEBOOK_CLIENT_SECRET));
        facebookConnectionFactory.setScope("public_profile,email");
        registry.addConnectionFactory(facebookConnectionFactory);

        return registry;
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.queryableText(
                environment.getProperty(ENCRYPT_PASSWORD),
                environment.getProperty(ENCRYPT_SALT));
    }

    @Bean
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository connectionRepository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator(), textEncryptor());
        connectionRepository.setConnectionSignUp(facebookConnectionSignUp);
        return connectionRepository;
    }


    @Bean
    public ProviderSignInController providerSignInController() {
        ProviderSignInController signInController = new ProviderSignInController(connectionFactoryLocator(),
                usersConnectionRepository(), facebookSignInAdapter);
        signInController.setPostSignInUrl("/");
        return signInController;
    }

}
