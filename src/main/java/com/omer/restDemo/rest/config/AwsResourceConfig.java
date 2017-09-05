// package com.omer.restDemo.rest.config;
//
// import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
//
// import javax.sql.DataSource;
//
// // @Configuration
// // @ImportResource("classpath:/aws-config.xml")
// // @EnableRdsInstance(databaseName = "${database-name:}",
// //         dbInstanceIdentifier = "${db-instance-identifier:}",
// //         password = "${rdsPassword:}")
// public class AwsResourceConfig {
//
//     @ConfigurationProperties(prefix = "spring.datasource")
//     @Bean
//     @Primary
//     public DataSource dataSource() {
//         return DataSourceBuilder
//                 .create()
//                 .url("jdbc:mysql://rds-db.cbhjsdvongds.us-west-2.rds.amazonaws.com:3306/mydb")
//                 .username("db_user")
//                 .password("passwordfordb")
//                 .build();
//
//     }
// }
