package mz.co.attendance.control;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import mz.co.attendance.control.dao.repositories.RoleRepository;
import mz.co.attendance.control.service.client.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * The entry point of the Spring Boot application.
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */

@EnableCaching     // enabling caching in the application
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@ComponentScan(basePackages = {"mz.co.attendance.control", "mz.co.attendance.control.service"})
@PWA(name = "Attendance Control", shortName = "Attendance", startPath = "login", backgroundColor = "#468d88", themeColor = "#468d88", offlinePath = "offline.html", offlineResources = {"images/offline-login-banner.jpg"})
@Theme(value = "attendance-control")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final SessionService sessionService;
    @Value("${caching.spring.defaultTTL}")
    private String sessionTTL;

    public Application(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource, SqlInitializationProperties properties, RoleRepository roleRepository) {
        // This bean ensures the database is only initialized when empty
        return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
            @Override
            public boolean initializeDatabase() {
                if (roleRepository.count() == 0L) {
                    return super.initializeDatabase();
                }
                return super.initializeDatabase();
            }
        };
    }

    @Bean
    public WebClient getWebClientBuilder() {
        return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()
                        .compress(true)))
                .build();
    }


    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterAccess(Long.parseLong(sessionTTL), TimeUnit.SECONDS).expireAfterWrite(Long.parseLong(sessionTTL), TimeUnit.SECONDS);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon("icon", "icons/icon.png", "192x192");
    }
}
