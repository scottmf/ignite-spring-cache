/**
 * Copyright (C) 2018 Scott Feldstein
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.scottieknows.ignite.springdata;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.cache.Cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.EventType;
import org.apache.ignite.internal.util.IgniteUtils;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.scottieknows.ignite.springcache.IgniteSpringCacheApplication;

@EnableIgniteRepositories
@SpringBootApplication(scanBasePackages="com.scottieknows.ignite.configuration")
public class IgniteSpringDataApplication {

    public static void main(String[] args) {
        Ignition.setClientMode(true);
        IgniteUtils.setIgniteHome(System.getProperty("java.io.tmpdir"));
        ConfigurableApplicationContext ctx = SpringApplication.run(IgniteSpringDataApplication.class, args);
        PersonRepository repo = ctx.getBean(PersonRepository.class);
        TreeMap<Long, Person> persons = new TreeMap<>();

        persons.put(1L, new Person(1L, 2000L, "John", "Smith", 15000, "Worked for Apple"));
        persons.put(2L, new Person(2L, 2000L, "Brad", "Pitt", 16000, "Worked for Oracle"));
        persons.put(3L, new Person(3L, 1000L, "Mark", "Tomson", 10000, "Worked for Sun"));

        // Adding data into the repository.
        repo.save(persons);

        List<Person> peeps = repo.findByFirstName("John");

        for (Person person : peeps) {
            System.out.println("   >>>   " + person);
        }

        Cache.Entry<Long, Person> topPerson = repo.findTopByLastNameLike("Smith");

        System.out.println("\n>>> Top Person with surname 'Smith': " + topPerson.getValue());
        ctx.close();
    }

    @Bean
    @Autowired
    public Ignite igniteInstance(IgniteConfiguration igniteConfiguration) {
        // Defining and creating a new cache to be used by Ignite Spring Data repository.
        CacheConfiguration ccfg = new CacheConfiguration("PersonCache");
        // Setting SQL schema for the cache.
        ccfg.setIndexedTypes(Long.class, Person.class);
        igniteConfiguration.setCacheConfiguration(ccfg);
        return Ignition.start(igniteConfiguration);
    }

}
