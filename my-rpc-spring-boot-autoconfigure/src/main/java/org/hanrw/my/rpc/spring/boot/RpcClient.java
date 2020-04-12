package org.hanrw.my.rpc.spring.boot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * @author hanrw
 * @date 2020/4/2 10:24 AM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcClient {

  @AliasFor("name")
  String value() default "";

  @AliasFor("value")
  String name() default "";
}
