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
package com.scottieknows.ignite.configuration;

import java.util.Collections;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.EventType;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteConfig {

    @Bean
    @Autowired
    public IgniteConfiguration igniteConfiguration(TcpDiscoveryIpFinder tcpResolver,
            @Value("${IGNITE_HOME:#{systemProperties['java.io.tmpdir']}}") String igniteHome) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setIncludeEventTypes(EventType.EVTS_ALL);
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setIgniteInstanceName("springDataNode");
        // need to set this or else there will slowness at startup
        igniteConfiguration.setIgniteHome(igniteHome);
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
        tcpDiscoverySpi.setIpFinder(tcpResolver);
        tcpDiscoverySpi.setJoinTimeout(3000);
        tcpDiscoverySpi.setAckTimeout(3000);
        tcpDiscoverySpi.setSocketTimeout(3000);
        tcpDiscoverySpi.setNetworkTimeout(3000);
        tcpDiscoverySpi.failureDetectionTimeoutEnabled(true);
        return igniteConfiguration;
    }

    @Bean
    public TcpDiscoveryIpFinder tcpResolver() {
        TcpDiscoveryMulticastIpFinder tcpDiscoveryMulticastIpFinder = new TcpDiscoveryMulticastIpFinder();
        tcpDiscoveryMulticastIpFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47501"));
        return tcpDiscoveryMulticastIpFinder;
    }

}
