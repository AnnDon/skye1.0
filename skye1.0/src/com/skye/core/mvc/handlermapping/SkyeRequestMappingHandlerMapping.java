package com.skye.core.mvc.handlermapping;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.skye.core.context.URLScanCacheUtils;

import javassist.bytecode.Descriptor.Iterator;
/**
 * 让方法名成为请求路径的一部分，而不必为方法添加RequestMapping注解
 * @author majy
 *
 */
public class SkyeRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
	public void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping){
		//super.registerHandlerMethod(handler, method, mapping);//这个方法已经过时
		super.registerMapping(mapping, handler, method);
		Set patterns = getMappingPathPatterns(mapping);
		for(java.util.Iterator i$=patterns.iterator();i$.hasNext();){
			String pattern = (String) i$.next();
			System.out.println(pattern);
			if(!(getPathMatcher()).isPattern(pattern)){
				FunctionDesc funDesc = AnnotationUtils.findAnnotation(method, FunctionDesc.class);
				if(funDesc!=null)
					URLScanCacheUtils.addURL(pattern, funDesc.value());
				else
					URLScanCacheUtils.addURL(pattern, "");
			}
		}
	}
	public RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType){
		RequestCondition methodCondition;
		RequestMapping typeAnnotation;
		RequestCondition typeCondition;
		RequestMappingInfo info = null;
		RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		if(methodAnnotation!=null){
			methodCondition = getCustomMethodCondition(method);
			info = createRequestMappingInfo(methodAnnotation, methodCondition);
			typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			if(typeAnnotation!=null){
				typeCondition = getCustomTypeCondition(handlerType);
				info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
			}
		}else{
			methodCondition = getCustomMethodCondition(method);
			info = createMethodRequestMappingInfo(method, methodCondition);
			if(info!=null){
				typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
				if(typeAnnotation!=null){
					typeCondition = getCustomTypeCondition(handlerType);
					info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
				}
			}
		}
		return info;
	}
	protected RequestMappingInfo createMethodRequestMappingInfo(Method method, RequestCondition<?> customCondition) {
		if (method != null) {
			if (1 != method.getModifiers())
				return null;
			String methodUrl = method.getName();
			if (!(methodUrl.startsWith("/"))) {
				methodUrl = "/" + methodUrl;
				String[] patterns = new String[1];
				patterns[0] = methodUrl;
				RequestMethod[] methods = new RequestMethod[0];
				String[] params = new String[0];
				String[] headers = new String[0];
				String[] consumes = new String[0];
				String[] produces = new String[0];
				return new RequestMappingInfo(
						new PatternsRequestCondition(patterns,getUrlPathHelper(),
								getPathMatcher(),useSuffixPatternMatch(), useTrailingSlashMatch(),getFileExtensions()),
						new RequestMethodsRequestCondition(methods), new ParamsRequestCondition(params),
						new HeadersRequestCondition(headers), new ConsumesRequestCondition(consumes),
						new ProducesRequestCondition(produces, headers, getContentNegotiationManager()), customCondition);
			}
		}
		return null;
	}
}
