package org.hanrw.my.rpc.spring.boot;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author hanrw
 * @date 2020/4/2 10:36 AM
 */
@Data
public class RpcClientFactoryBean implements FactoryBean {

  /**
   * 传入接口对象 这里构造参数是Class类型,但是在QueryImportBeanDefinitionRegistrar传入构造参数的时候是String类型的全类名
   * builder.addPropertyValue(className); 为什么这里可以转化成Class对象 关键在于ConstructorResolver 会根据需要的类型找到对应的属性编辑器
   * findDefaultEditor(requiredType); 从而进行类型转换 convertedValue = converter.convertIfNecessary(originalValue, paramType,
   * methodParam);
   *
   * @param type
   */
  private Class<?> type;

  private String name;

  @Override
  public Object getObject() throws Exception {
    System.out.println("service name is:" + name);
    return null;
  }

  @Override
  public Class<?> getObjectType() {
    return type.getClass();
  }
}
