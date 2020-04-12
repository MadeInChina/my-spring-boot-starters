/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hanrw.my.rpc.spring.boot;

import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * RpcClients注册
 */
class RpcClientsRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    // 获取EnableRpcClients注解的所有属性(basePackages)
    Map<String, Object> defaultAttrs = importingClassMetadata
                                           .getAnnotationAttributes(EnableRpcClients.class.getName(), true);
    // 获取扫描路径
    String[] basePackages = (String[]) defaultAttrs.get("basePackages");
    // 获取类扫描器
    ClassPathScanningCandidateComponentProvider scanner = getScanner();
    // 添加过滤条件,只扫描带有RpcClient的类
    scanner.addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));
    // 遍历扫描路径
    for (String basePackage : basePackages) {
      Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
      for (BeanDefinition candidateComponent : candidateComponents) {
        if (candidateComponent instanceof AnnotatedBeanDefinition) {
          // 验证注解类必须是接口类型
          AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
          AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
          Assert.isTrue(
              annotationMetadata.isInterface(), "@RpcClient can only be specified on an interface");
          // 获取注解RpcClient注解的相关属性
          Map<String, Object> attributes =
              annotationMetadata.getAnnotationAttributes(RpcClient.class.getCanonicalName());

          //注册bean定义
          registerRpcClient(registry, annotationMetadata, attributes);
        }
      }
    }
  }

  private void registerRpcClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata,
      Map<String, Object> attributes) {
    String className = annotationMetadata.getClassName();
    BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(RpcClientFactoryBean.class);
    definition.addPropertyValue("type", className);

    String name = (String) attributes.get("serviceId");
    if (!StringUtils.hasText(name)) {
      name = (String) attributes.get("name");
    }
    if (!StringUtils.hasText(name)) {
      name = (String) attributes.get("value");
    }

    definition.addPropertyValue("name", name);

    AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
    BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
  }

  /**
   * 默认的ClassPathScanningCandidateComponentProvider不会扫描接口类型的类 重写方法来实现扫描接口类
   */
  protected ClassPathScanningCandidateComponentProvider getScanner() {
    return new ClassPathScanningCandidateComponentProvider(false) {
      @Override
      protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isCandidate = false;
        // 所有顶级类都过滤出来
        if (beanDefinition.getMetadata().isIndependent()) {
          // 排除注解类型
          if (!beanDefinition.getMetadata().isAnnotation()) {
            isCandidate = true;
          }
        }
        return isCandidate;
      }
    };
  }
}
