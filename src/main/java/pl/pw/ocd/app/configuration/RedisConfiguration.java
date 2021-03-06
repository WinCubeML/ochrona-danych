package pl.pw.ocd.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();

        String redisUrl = System.getenv("redisConfig");
        if (null == redisUrl)
            redisUrl = "localhost";
        redisConf.setHostName(redisUrl);

        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }
}
