package com.example.masterslavetest.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DataSourceConfig {
  public static final String MASTER = "masterDataSource";
  public static final String SLAVE1 = "slave1DataSource";
  public static final String SLAVE2 = "slave2DataSource";

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
  public DataSource masterDataSource(){
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.slave1.hikari")
  public DataSource slave1DataSource(){
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.slave2.hikari")
  public DataSource slave2DataSource(){
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  @Primary
  @DependsOn({MASTER, SLAVE1, SLAVE2})
  public RoutingDataSource routingDataSource(
      @Qualifier(MASTER) DataSource master,
      @Qualifier(SLAVE1) DataSource slave1,
      @Qualifier(SLAVE2) DataSource slave2
      ){
    Map<Object, Object> targetDataSources = new HashMap<>();
    targetDataSources.put(MASTER, master);
    targetDataSources.put(SLAVE1, slave1);
    targetDataSources.put(SLAVE2, slave2);

    RoutingDataSource routingDataSource = new RoutingDataSource();
    routingDataSource.setTargetDataSources(targetDataSources);
    routingDataSource.setDefaultTargetDataSource(master);   //기본은 master

    return routingDataSource;
  }

  @Bean
  public LazyConnectionDataSourceProxy lazyDataSource(RoutingDataSource routingDataSource){
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }

  @Bean
  public PlatformTransactionManager transactionManager(LazyConnectionDataSourceProxy routingDataSource){
    return new DataSourceTransactionManager(routingDataSource);
  }
}
