package org.hanrw.my.rpc.spring.boot.services;

import org.hanrw.my.rpc.spring.boot.RpcClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户调用远程注解
 * @author hanrw
 * @date 2020/4/12 3:40 PM
 */
@RpcClient(value = "userService")
public interface UserService {
  @RequestMapping(method = RequestMethod.GET, value = "/hello")
  String getHello();
}
