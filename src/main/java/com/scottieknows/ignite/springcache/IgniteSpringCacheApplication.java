package com.scottieknows.ignite.springcache;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.eviction.EvictionPolicy;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.NearCacheConfiguration;
import org.apache.ignite.events.EventType;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication(scanBasePackages="com.scottieknows.ignite.configuration;com.scottieknows.ignite.springcache")
public class IgniteSpringCacheApplication {

	public static void main(String[] args) {
        Ignition.setClientMode(true);
		SpringApplication.run(IgniteSpringCacheApplication.class, args);
	}

    @Bean
    @Autowired
    public Ignite ignite(CacheManager cacheManager) {
        try {
            Field field = SpringCacheManager.class.getDeclaredField("ignite");
            field.setAccessible(true);
            return (Ignite) field.get(cacheManager);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Autowired
    public CacheManager cacheManager(IgniteConfiguration igniteConfiguration,
                                     NearCacheConfiguration<Object, Object> nearCacheConfiguration) {
        SpringCacheManager springCacheManager = new SpringCacheManager();
        springCacheManager.setConfiguration(igniteConfiguration);
        springCacheManager.setDynamicNearCacheConfiguration(nearCacheConfiguration);
        CacheConfiguration<Object, Object> cacheConfiguration = new CacheConfiguration<>();
        cacheConfiguration.setName("myids");
        cacheConfiguration.setCacheMode(CacheMode.REPLICATED);
        springCacheManager.setDynamicCacheConfiguration(cacheConfiguration);
        return springCacheManager;
    }

    @Bean
    public LruEvictionPolicy<Object, Object> evictionPolicy(@Value("${maxNearSize:10000}") int max) {
        LruEvictionPolicy<Object, Object> lruEvictionPolicy = new LruEvictionPolicy<>(max);
        return lruEvictionPolicy;
    }

    @Bean
    @Autowired
    public NearCacheConfiguration<Object, Object> nearCacheConfiguration(
            EvictionPolicy<Object, Object> evictionPolicy,
            @Value("${nearStartSize:1000}") int nearStartSize) {
        NearCacheConfiguration<Object, Object> nearCacheConfiguration = new NearCacheConfiguration<>();
        nearCacheConfiguration.setNearEvictionPolicy(evictionPolicy);
        nearCacheConfiguration.setNearStartSize(nearStartSize);
        return nearCacheConfiguration;
    }

}
