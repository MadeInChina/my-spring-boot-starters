package org.hanrw.my.rpc.spring.boot.config;

import org.hanrw.my.rpc.spring.boot.EnableRpcClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author hanrw
 * @date 2020/4/12 3:46 PM
 */
@Configuration
@EnableRpcClients(basePackages = "org.hanrw.my.rpc.spring.boot.services")
public class RpcConfig {

}
