package kr.datasolution.member.configuration;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * MYBATIS DATABASE 설정
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
public class MybatisDataSourceConfiguration {
    /** Application Context 객체 */
    @Autowired
    private ApplicationContext applicationContext;

    /** Mybatis Config 설정 파일 경로 */
    @Value("${datasource.mybatis.config-location}")
    private String mybatisConfigLocation;

    /** Mybatis DATABASE 종류 */
    @Value("${datasource.pagehelper.second.database-type}")
    private String mybatisDataSource;

    /**
     * 공통 SQL_SESSION_FACTORY 세팅
     *
     * @param dataSource 적용 대상 DataSource
     * @param databaseType 연결되는 DATABASE 종류
     * @return 생성된 SqlSessionFactory
     * @throws Exception resource 참조 및 SqlSessionFactory 객체 생성 실패 시 발생
     */
    private SqlSessionFactory setCommonSqlSessionFactory(DataSource dataSource, String databaseType) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        /*
         * PageHelper 정의 - 이기종간의 DB 연결에 따른 Paging 쿼리 정보가 다르게 처리되어야 하므로 아래와 같이 설정 진행<br>
         * ※ 자동으로 인식하도록 설정이 가능하나 sqlserver2012 경우, sqlserver2005 버전의 페이징이 처리되므로 수동으로 지정하는 것을 권장함
         */
        PageInterceptor pageInterceptor = new PageInterceptor();

        Properties properties = new Properties();
        // 지정 가능 databaseType : oracle, mysql, mariadb, sqlite, hsqldb, postgresql, db2, sqlserver, informix, h2, sqlserver2012, derby
        properties.setProperty("helperDialect", databaseType);

        pageInterceptor.setProperties(properties);

        Interceptor[] interceptor = new Interceptor[1];
        interceptor[0] = pageInterceptor;

        sqlSessionFactoryBean.setPlugins(interceptor);

        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource(mybatisConfigLocation));
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    /**
     * MYBATIS DATABASE - DATASOURCE 설정
     *
     * @return DataSource
     */
    @Bean(name = "mybatisDataSource")
    @ConfigurationProperties(prefix = "datasource.mybatis.hikari")
    public DataSource mybatisDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name="mybatisDataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager2(@Qualifier ("mybatisDataSource") DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    /**
     * MYBATIS DATABASE - SQL SESSION FACTORY 설정
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name="mybatisSqlSessionFactory")
    public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("mybatisDataSource") DataSource dataSource) throws Exception {
        return setCommonSqlSessionFactory(dataSource, mybatisDataSource);
    }

    /**
     * MYBATIS DATABASE - SQL SESSION TEMPLATE 설정
     *
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name="sqlSessionTemplate")
    public SqlSessionTemplate mybatisSqlSessionTemplate(@Qualifier("mybatisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
