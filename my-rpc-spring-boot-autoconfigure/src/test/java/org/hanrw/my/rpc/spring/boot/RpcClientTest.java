package org.hanrw.my.rpc.spring.boot;

import org.hanrw.my.rpc.spring.boot.config.RpcConfig;
import org.hanrw.my.rpc.spring.boot.services.UserService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author hanrw
 * @date 2020/3/21 11:52 AM
 */
public class RpcClientTest {
  @Test
  public void testEnableMethodCostTimeConfig() {
    // 1.通过AnnotationConfigApplicationContext创建spring容器，参数为@Import标注的类
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(RpcConfig.class);
    context.getBean("org.hanrw.my.rpc.spring.boot.services.UserService");
  }
}
