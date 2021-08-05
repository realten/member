package kr.datasolution.member.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA DATABASE 설정
 *
 * @Configuration 클래스가 하나 이상의 @Bean 메소드를 선언하고 Bean 정의 및 서비스 요청을 생성하기 위한 Annotation
 * @EnableTransactionManagement 트랜잭션 관리 Annotation
 * @EnableJpaRepositories JpaRepository 관리 Annotation
 *  - entityManagerFactoryRef : EntityManagerFactory Bean명
 *  - transactionManagerRef : PlatformTransactionManager Bean명
 *  - basePackages : JpaRepository 패키지 경로 설정
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "platformTransactionManager",
        basePackages = "kr.datasolution.member.**.repository")
public class JpaDatasourceConfiguration {
    /**
     * JPA DATABASE - DATASOURCE 설정
     *
     * @ConfigurationProperties 기본 설정 주소와 다른 경우 직접 설정하기 위한 Annotation
     * @return DataSource
     */
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "datasource.jpa.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * JPA DATABASE - JPA 설정
     *
     * @return JpaProperties
     */
    @Primary
    @Bean(name = "jpaProperties")
    public JpaProperties jpaProperties() {
        JpaProperties jpaProperties = new JpaProperties();
        Map<String, String> prop = new HashMap<>();
        prop.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        jpaProperties.setProperties(prop);
        return jpaProperties;
    }

    /**
     * JPA DATABASE - Entity Manager Factory 설정
     * packages : Domain의 패키지 경로를 명시
     *
     * @param builder EntityManagerFactoryBuilder
     * @param dataSource DataSource
     * @param jpaProperties JpaProperties
     * @return LocalContainerEntityManagerFactoryBean
     */
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localEntityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                                @Qualifier("dataSource") DataSource dataSource,
                                                                                @Qualifier("jpaProperties") JpaProperties jpaProperties) {
        return builder
                .dataSource(dataSource)
                .properties(jpaProperties.getProperties())
                .packages("kr.datasolution.member.**.domain")
                .build();
    }

    /**
     * JPA DATABASE - EntityManager 설정
     *
     * @param entityManagerFactory EntityManagerFactory
     * @return EntityManager
     */
    @Bean(name = "entityManager")
    @Primary
    public EntityManager entityManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * JPA DATABASE - TRANSACTION 설정
     *
     * @param entityManagerFactory EntityManagerFactory
     * @return PlatformTransactionManager
     */
    @Primary
    @Bean(name = "platformTransactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

