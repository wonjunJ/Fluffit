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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.*;
import org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction;

import java.time.LocalDateTime;
import java.util.Arrays;

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
        System.out.println("템플릿은 만들어지냐 ");
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryForUserBattle());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }

    @Bean(name = "userBattleObjectRedisTemplate")
    public StringRedisTemplate userBattleObjectRedisTemplate() {
        System.out.println("그 오브젝트도 템플릿은 만들어지냐 ");
        return new StringRedisTemplate(redisConnectionFactoryForUserBattle());
    }

    @Primary
    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }

//    public class LoggingStringRedisSerializer extends StringRedisSerializer {
//        @Override
//        public byte[] serialize(String string) {
//            byte[] data = super.serialize(string);
//            System.out.println("Serializing string: " + string + " to bytes: " + Arrays.toString(data));
//            return data;
//        }
//
//        @Override
//        public String deserialize(byte[] bytes) {
//            String string = super.deserialize(bytes);
//            System.out.println("Deserializing bytes: " + Arrays.toString(bytes) + " to string: " + string);
//            return string;
//        }
//    }
//
//    public class LoggingJackson2JsonRedisSerializer<T> extends Jackson2JsonRedisSerializer<T> {
//        public LoggingJackson2JsonRedisSerializer(Class<T> type) {
//            super(type);
//        }
//
//        @Override
//        public byte[] serialize(T t) throws SerializationException {
//            byte[] data = super.serialize(t);
//            System.out.println("Serializing object: " + t + " to bytes: " + Arrays.toString(data));
//            return data;
//        }
//
//        @Override
//        public T deserialize(byte[] bytes) throws SerializationException {
//            T object = super.deserialize(bytes);
//            System.out.println("Deserializing bytes: " + Arrays.toString(bytes) + " to object: " + object);
//            return object;
//        }
//    }
//
//
//    @Bean(name = "userBattleObjectRedisTemplate")
//    public RedisTemplate<String, Object> userBattleObjectRedisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactoryForUserBattle());
//        template.setKeySerializer(new LoggingStringRedisSerializer());
//        template.setValueSerializer(new LoggingJackson2JsonRedisSerializer<>(Object.class));
//        template.setHashKeySerializer(new LoggingStringRedisSerializer());
//        template.setHashValueSerializer(new LoggingJackson2JsonRedisSerializer<>(Object.class));
//
//        template.afterPropertiesSet();
//        return template;
//    }

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
