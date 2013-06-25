package de.centerdevice.roca.config;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import de.centerdevice.roca.centerdevice.CenterDeviceServiceImpl;
import de.centerdevice.roca.centerdevice.CenterDeviceServiceStub;
import de.centerdevice.roca.view.UserGroupsViewPreparer;
import de.centerdevice.roca.oauth.CenterDeviceProvider;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import de.centerdevice.roca.view.MobileClientViewPreparer;
import org.apache.tiles.preparer.ViewPreparer;
import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.tiles3.*;

@Configuration
@ComponentScan(basePackages = {"de.centerdevice.roca"})
public class WebMvcConfig extends WebMvcConfigurationSupport {

    private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages";
    private static final String TILES = "/WEB-INF/tiles/tiles.xml";
    private static final String VIEWS = "/WEB-INF/views/**/views.xml";
    private static final String RESOURCES_HANDLER = "/resources/";
    private static final String RESOURCES_LOCATION = RESOURCES_HANDLER + "**";

    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();
        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
        requestMappingHandlerMapping.setUseTrailingSlashMatch(true);
        return requestMappingHandlerMapping;
    }

    @Bean(name = "messageSource")
    public MessageSource configureMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_SOURCE);
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

    @Bean
    public TilesViewResolver configureTilesViewResolver() {
        return new TilesViewResolver();
    }

    @Bean
    public TilesConfigurer configureTilesConfigurer() {
        TilesConfigurer configurer = new TilesConfigurer();
        configurer.setDefinitions(new String[]{TILES, VIEWS});
        configurer.setPreparerFactoryClass(SpringBeanPreparerFactory.class);
        return configurer;
    }

    @Bean(name = "UserGroupsViewPreparer")
    public ViewPreparer userGroupsViewPreparer() {
        return new UserGroupsViewPreparer();
    }

    @Bean(name = "MobileClientViewPreparer")
    public ViewPreparer mobileClientViewPreparer() {
        return new MobileClientViewPreparer();
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(configureMessageSource());
        return validator;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCES_HANDLER).addResourceLocations(RESOURCES_LOCATION);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public CenterDeviceOAuthConfig getConfig() {
        return new CenterDeviceOAuthConfig();
    }

    @Bean
    @Scope(value = "singleton")
    public OAuthService oauthService() {
        CenterDeviceOAuthConfig config = getConfig();

        return new ServiceBuilder()
                .provider(CenterDeviceProvider.class)
                .apiKey(config.getApiKey())
                .apiSecret(config.getApiSecret())
                .callback(config.getCallbackUrl())
                .build();
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public OAuthAccessToken oauthAccessToken() {
        OAuthAccessToken accessToken = new OAuthAccessToken();

        return accessToken;
    }

    @Bean
    public CenterDeviceService createCenterDeviceService() {
        return new CenterDeviceServiceImpl();
//        return new CenterDeviceServiceStub();
    }
}
