package com.worldgether.mbal.config;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;

@Profile("!local")
@Configuration
public class ExternalMongoConfig extends AbstractMongoConfiguration{

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;
    @Value("${spring.data.mongodb.port}")
    private Integer mongoPort;
    @Value("${spring.data.mongodb.database}")
    private String mongoDbName;
    @Value("${spring.data.mongodb.username:#{null}}")
    private String mongoUsername;
    @Value("${spring.data.mongodb.password:#{null}}")
    private String mongoPassword;

    @Override
    protected String getDatabaseName(){ return mongoDbName; }

    @Override
    @Bean(destroyMethod = "close")
    public Mongo mongo() throws Exception {
        if(!StringUtils.isEmpty(mongoUsername) && !StringUtils.isEmpty(mongoPassword)){
            return new MongoClient(Collections.singletonList(new ServerAddress(mongoHost, mongoPort)),
                    Collections.singletonList(MongoCredential.createCredential(mongoUsername, mongoDbName, mongoPassword.toCharArray())));
        }
        return new MongoClient(mongoHost,mongoPort);
    }

    @Override
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        //remove _class
        DefaultDbRefResolver defaultDbRefResolver = new DefaultDbRefResolver(mongoDbFactory());

        MappingMongoConverter converter =
                new MappingMongoConverter(defaultDbRefResolver, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return new MongoTemplate(mongoDbFactory(), converter);

    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public MongodExecutable embeddedMongoServer(IMongodConfig mongodConfig) throws IOException {
        return null;
    }

}

