package com.ssafy.fluffitbattle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.service.RedisKeyExpirationListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

//    @Value("${spring.data.redis.host}")
//    private String host;
//
//    @Value("${spring.data.redis.port}")
//    private String port;

//    @Value("${spring.data.redis.password}")
//    private String password;

    private final Environment env;

    private final int BASIC_DATABASE = 0;
    private final int BATTLE_DATABASE = 1;
    private final int USER_BATTLE_DATEBASE = 2;
    private final int WAIT_QUEUE_DATABASE = 4;


    @Bean
    @Primary
    public LettuceConnectionFactory redisConnectionFactory() {
        return createLettuceConnectionFactory(BASIC_DATABASE);  // Default connection factory uses database 0
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactoryForBattle() {
        return createLettuceConnectionFactory(BATTLE_DATABASE);  // Additional connection factory for database 1
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactoryForUserBattle() {
        return createLettuceConnectionFactory(USER_BATTLE_DATEBASE);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactoryForQueue() {
        return createLettuceConnectionFactory(WAIT_QUEUE_DATABASE);  // Additional connection factory for database 1
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory3() {
        return createLettuceConnectionFactory(3);  // Additional connection factory for database 1
    }


    private LettuceConnectionFactory createLettuceConnectionFactory(int database) {
        String host = env.getProperty("spring.data.redis.host");
        String port = env.getProperty("spring.data.redis.port");
        String password = env.getProperty("spring.data.redis.password");

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(Integer.parseInt(port));
        config.setPassword(password);
        config.setDatabase(database);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        return createRedisTemplate(redisConnectionFactory());
    }

    @Bean
    public RedisTemplate<String, Battle> battleRedisTemplate() {
        RedisTemplate<String, Battle> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForBattle());

        template.setKeySerializer(new StringRedisSerializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setValueSerializer(serializer);  // String 타입 값 직렬화

        return template;
    }

    @Bean(name = "userBattleLongRedisTemplate")
    public RedisTemplate<String, Long> userBattleLongRedisTemplate() {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForUserBattle());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }

    @Bean(name = "userBattleObjectRedisTemplate")
    public RedisTemplate<String, Object> userBattleObjectRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForUserBattle());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<Long, LocalDateTime> waitingQueueRedisTemplate() {
        RedisTemplate<Long, LocalDateTime> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForQueue());
        template.setKeySerializer(new GenericToStringSerializer<>(Long.class));  // Long 타입 키 직렬화
        template.setValueSerializer(new GenericToStringSerializer<>(LocalDateTime.class));
        return template;
    }

//    @Bean
//    public RedisTemplate<Long, String> longStringRedisTemplate() {
//        RedisTemplate<Long, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactoryForBattle());
//        template.setKeySerializer(new GenericToStringSerializer<>(Long.class));  // Long 타입 키 직렬화
//        template.setValueSerializer(new StringRedisSerializer());  // String 타입 값 직렬화
//        return template;
//    }

//    @Bean(name = "stringRedisTemplate")
//    public RedisTemplate<String, String> stringRedisTemplate() {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        return template;
//    }

    private RedisTemplate<String, Object> createRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

//    @Bean
//    public RedisMessageListenerContainer keyExpirationListenerContainer(@Qualifier("redisConnectionFactory") RedisConnectionFactory connectionFactory) {
//        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
//        listenerContainer.setConnectionFactory(connectionFactory);
//        return listenerContainer;
//    }

    @Bean
    public ConfigureNotifyKeyspaceEventsAction configureNotifyKeyspaceEventsAction() {
        return new ConfigureNotifyKeyspaceEventsAction();
    }

    @Bean
    public RedisMessageListenerContainer keyExpirationListenerContainer(@Qualifier("redisConnectionFactory") RedisConnectionFactory connectionFactory, RedisKeyExpirationListener listener) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.addMessageListener(listener, new PatternTopic("__keyevent@*__:expired"));
        return listenerContainer;
    }
}
