package es.upm.oeg.farolapp4all.transcriber.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import es.upm.oeg.farolapp4all.transcriber.AppConfig;
import org.ehcache.Cache;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.InputStream;
import java.util.Properties;

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
public class AppConfigTest {

    private  Properties testProperties = new Properties();

    @Before
    public void loadTestConfig() throws Exception {

        //Loading the filtered properties from the test file
        try (final InputStream stream =
                     this.getClass().getResourceAsStream("/test-config.properties")) {
            System.out.println(stream);
            testProperties.load(stream);
        }

    }

    @Test
    public void testDefaultConfig() {

        String defaultSparqlEndpoint = testProperties.getProperty("farolapp.local.sparql");
        String defaultRabbitmqHost = testProperties.getProperty("farolapp.local.rabbitmq.host");
        String defaultRabbitmqUsername = testProperties.getProperty("farolapp.local.rabbitmq.username");
        String defaultRabbitmqPassword = testProperties.getProperty("farolapp.local.rabbitmq.password");
        String defaultRabbitmqVirtualHost = testProperties.getProperty("farolapp.local.rabbitmq.virtualhost");
        String defaultRabbitmqExchange = testProperties.getProperty("farolapp.local.rabbitmq.exchange");
        int defaultRabbitmqTimeout = 60;
        int defaultRabbitmqHeartbeat = 60;
        int defaultIdcacheConcurrency = 10;
        int defaultIdcacheMaxsize = 1000;
        int defaultIdcacheExpiry = 5;
        int defaultNotifierDelay = 30000;
        int defaultNotifierPeriod = 20000;


        AppConfig.init();

        String sparqlEndpoint = AppConfig.getString(AppConfig.SPARQL_ENDPOINT);
        assertThat(sparqlEndpoint, equalTo(defaultSparqlEndpoint));

        String rabbitmqHost = AppConfig.getString(AppConfig.RABBIT_HOST);
        assertThat(rabbitmqHost, equalTo(defaultRabbitmqHost));

        String rabbitUsername = AppConfig.getString(AppConfig.RABBIT_USER);
        assertThat(rabbitUsername, equalTo(defaultRabbitmqUsername));

        String rabbitmqPassword = AppConfig.getString(AppConfig.RABBIT_PASSWORD);
        assertThat(rabbitmqPassword, equalTo(defaultRabbitmqPassword));

        String rabbitmqVirtualHost = AppConfig.getString(AppConfig.RABBIT_VIRTUAL_HOST);
        assertThat(rabbitmqVirtualHost, equalTo(defaultRabbitmqVirtualHost));

        String rabbitmqExchange = AppConfig.getString(AppConfig.RABBIT_EXCHANGE);
        assertThat(rabbitmqExchange, equalTo(defaultRabbitmqExchange));

        int rabbitmqTimeout = AppConfig.getInt(AppConfig.RABBIT_CONNECTION_TIMEOUT);
        assertThat(rabbitmqTimeout, equalTo(defaultRabbitmqTimeout));

        int rabbitmqHeartbeat = AppConfig.getInt(AppConfig.RABBIT_HEARTBEAT);
        assertThat(rabbitmqHeartbeat, equalTo(defaultRabbitmqHeartbeat));

        int idcacheConcurrency = AppConfig.getInt(AppConfig.CACHE_CONCURRENCY_LEVEL);
        assertThat(idcacheConcurrency, equalTo(defaultIdcacheConcurrency));

        int idcacheMaxsize= AppConfig.getInt(AppConfig.CACHE_MAX_SIZE);
        assertThat(idcacheMaxsize, equalTo(defaultIdcacheMaxsize));

        int idcacheExpiry= AppConfig.getInt(AppConfig.CACHE_EXPIRY);
        assertThat(idcacheExpiry, equalTo(defaultIdcacheExpiry));

        int notifierDelay= AppConfig.getInt(AppConfig.NOTIFIER_DELAY);
        assertThat(notifierDelay, equalTo(defaultNotifierDelay));

        int notifierPeriod= AppConfig.getInt(AppConfig.NOTIFIER_PERIOD);
        assertThat(notifierPeriod, equalTo(defaultNotifierPeriod));
    }

    @Test
    public void testIdcache() {

        String tempID = "temp3794d9c2-5bed-42c8-8bc0-37599b6deb9a";
        String uri = "http://farolas.linkeddata.es/resource/5c74620c-0f58-464f-a747-c37d9df495d5";

        AppConfig.init();
        Cache<String, String> idCache = AppConfig.getIdCache();
        idCache.put(tempID, uri);

        String cachedURI = idCache.get(tempID);

        assertThat("check not null", cachedURI, notNullValue());
        assertThat("check for correct value", cachedURI, equalTo(uri));

    }

}
