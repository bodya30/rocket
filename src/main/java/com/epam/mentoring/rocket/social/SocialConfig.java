package com.epam.mentoring.rocket.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.github.connect.GitHubConnectionFactory;

import javax.sql.DataSource;

@Configuration
public class SocialConfig {

    private static final String FACEBOOK_CLIENT_ID = "spring.social.facebook.client.id";
    private static final String FACEBOOK_CLIENT_SECRET = "spring.social.facebook.client.secret";
    private static final String GITHUB_CLIENT_ID = "spring.social.github.client.id";
    private static final String GITHUB_CLIENT_SECRET = "spring.social.github.client.secret";
    private static final String ENCRYPT_PASSWORD = "social.security.encrypt.password";
    private static final String ENCRYPT_SALT = "social.security.encrypt.salt";

    @Autowired
    private Environment environment;

    @Autowired
    private ConnectionSignUp defaultConnectionSignUp;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SignInAdapter defaultSignInAdapter;

    @Bean
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

        registry.addConnectionFactory(new FacebookConnectionFactory(
                environment.getProperty(FACEBOOK_CLIENT_ID),
                environment.getProperty(FACEBOOK_CLIENT_SECRET)));

        GitHubConnectionFactory gitHubConnectionFactory = new GitHubConnectionFactory(
                environment.getProperty(GITHUB_CLIENT_ID),
                environment.getProperty(GITHUB_CLIENT_SECRET));
        gitHubConnectionFactory.setScope("user,user:email");
        registry.addConnectionFactory(gitHubConnectionFactory);

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
        connectionRepository.setConnectionSignUp(defaultConnectionSignUp);
        return connectionRepository;
    }

    @Bean
    public ProviderSignInController providerSignInController() {
        ProviderSignInController signInController = new ProviderSignInController(connectionFactoryLocator(),
                usersConnectionRepository(), defaultSignInAdapter);
        signInController.setPostSignInUrl("/");
        signInController.setSignUpUrl("/registration?error=true");
        return signInController;
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils() {
        return new ProviderSignInUtils(connectionFactoryLocator(), usersConnectionRepository());
    }

}
