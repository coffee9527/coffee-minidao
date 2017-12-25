package com.coffee.minidao.factory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.coffee.minidao.annotation.MiniDao;

public class MiniDaoClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
	
	private static final Logger logger = Logger.getLogger(MiniDaoClassPathMapperScanner.class);
	
	public MiniDaoClassPathMapperScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotation) {
		super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(annotation));
        if (!MiniDao.class.equals(annotation)) {
            addIncludeFilter(new AnnotationTypeFilter(MiniDao.class));
        }
	}
	
	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		
		if(beanDefinitions.isEmpty())
			logger.warn("No Dao interface was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
		GenericBeanDefinition definition;
		for(BeanDefinitionHolder holder : beanDefinitions) {
			definition = (GenericBeanDefinition)holder.getBeanDefinition();
			definition.getPropertyValues().add("proxy", getRegistry().getBeanDefinition("miniDaoHandler"));
			if(logger.isInfoEnabled()) {
				logger.info("register minidao name is {" + definition.getBeanClassName() + "}");
			}
			definition.setBeanClass(MiniDaoBeanFactory.class);
		}
		return beanDefinitions;
	}

}
