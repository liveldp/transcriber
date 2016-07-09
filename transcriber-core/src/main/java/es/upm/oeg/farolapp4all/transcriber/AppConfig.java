package es.upm.oeg.farolapp4all.transcriber;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Copyright 2014-2016 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Nandana Mihindukulasooriya
 * @since 1.0.0
 */
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public static final String SPARQL_ENDPOINT  = "transcriber.sparql.endpoint";

    public static final String RABBIT_HOST = "transcriber.rabbitmq.host";

    public static final String RABBIT_USER = "transcriber.rabbitmq.username";

    public static final String RABBIT_PASSWORD = "transcriber.rabbitmq.password";

    public static final String RABBIT_VIRTUAL_HOST = "transcriber.rabbitmq.virtualhost";

    public static final String RABBIT_CONNECTION_TIMEOUT = "transcriber.rabbitmq.connectionTimeout";

    public static final String RABBIT_HEARTBEAT = "transcriber.rabbitmq.heartbeat";

    public static final String RABBIT_EXCHANGE = "transcriber.rabbitmq.exchange";

    public static final String RABBIT_CONSENSUS = "transcriber.rabbitmq.consensus";

    public static final String RABBIT_MANAGEMENT = "transcriber.rabbitmq.management";

    public static final String CACHE_CONCURRENCY_LEVEL = "transcriber.idcache.concurrency";

    public static final String CACHE_MAX_SIZE = "transcriber.idcache.maxsize";

    public static final String CACHE_EXPIRY  = "transcriber.idcache.expiry";

    public static final String NOTIFIER_DELAY  = "transcriber.notifier.delay";

    public static final String NOTIFIER_PERIOD  = "transcriber.notifier.period";

    private static Config config;

    private static Cache<String, String> idCache;

    private static Queue<String> newLamppostQueue = new ConcurrentLinkedQueue<>();

    private static final String CACHE_NAME = "idCache";


    public static void init() {
        config = ConfigFactory.load();
        String configString = config.root().render(ConfigRenderOptions.defaults());
        logger.debug("Config : {}", configString);

        Expiry<Object, Object> expiry = Expirations.timeToLiveExpiration(new Duration(10, TimeUnit.MINUTES));

        CacheManager cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(CACHE_NAME,
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                                ResourcePoolsBuilder.heap(1000))
                                .withExpiry(expiry)
                )
                .build();
        cacheManager.init();

        idCache = cacheManager.getCache(CACHE_NAME, String.class, String.class);


//        idCache = CacheBuilder.newBuilder()
//                .concurrencyLevel(config.getInt(CACHE_CONCURRENCY_LEVEL))
//                .weakKeys()
//                .maximumSize(config.getInt(CACHE_MAX_SIZE))
//                .expireAfterWrite(config.getInt(CACHE_EXPIRY), TimeUnit.MINUTES)
//                .build();
    }

    public static String getString(String key) {
        return config.getString(key);
    }

    public static int getInt(String key) {
        return config.getInt(key);
    }

    public static Cache<String, String> getIdCache() {
        return idCache;
    }

    public static Queue<String> getNewLamppostQueue() {
        return newLamppostQueue;
    }
}
