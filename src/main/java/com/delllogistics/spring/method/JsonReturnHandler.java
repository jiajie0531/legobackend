package com.delllogistics.spring.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.delllogistics.dto.Result;
import com.delllogistics.spring.DTO;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.util.EntityConvertUtil;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 处理自定义注解，过滤返回对象
 * Created by xzm on 2017-11-20.
 */
public class JsonReturnHandler implements HandlerMethodReturnValueHandler {

    private final FastJsonConfig fastJsonConfig;

    public JsonReturnHandler(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), JsonConvert.class) || returnType.hasMethodAnnotation(JsonConvert.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        SerializerFeature[] serializerFeatures = fastJsonConfig.getSerializerFeatures();
        JsonConvert jsonConvert = returnType.getMethodAnnotation(JsonConvert.class);
        if(jsonConvert==null){
            writer.write(JSON.toJSONString(returnValue,serializerFeatures));
        }else{
             /*
        优先处理DTO转换
         */
            Class<?> dto = jsonConvert.dto();
            if(!dto.equals(DTO.class)){
                if(returnValue instanceof Page){
                    Page page= (Page) returnValue;
                    List<Object> list = new ArrayList<>();
                    for (Object entity : page) {
                        Object returnObj = EntityConvertUtil.convertToEntity(entity, dto);
                        list.add(returnObj);
                    }
                    PageRequest pageRequest = new PageRequest(page.getNumber(), page.getSize(), page.getSort());
                    PageImpl<Object> page1 = new PageImpl<>(list, pageRequest, page.getTotalElements());
                    writer.write(JSON.toJSONString(page1,serializerFeatures));
                }else if(returnValue instanceof Collection){
                    Collection collection= (Collection) returnValue;
                    List<Object> list = new ArrayList<>();
                    for (Object item : collection) {
                        Object returnObj = EntityConvertUtil.convertToEntity(item, dto);
                        list.add(returnObj);
                    }
                    writer.write(JSON.toJSONString(list,serializerFeatures));
                }else{
                    writer.write(JSON.toJSONString(returnValue,serializerFeatures));
                }


            }else{
                List<SerializeFilter> serializeFilters=new ArrayList<>();
                /*
                处理Result类型
                 */
                if(returnValue instanceof Result){
                    serializeFilters.add(new SimplePropertyPreFilter(Result.class));
                    Result result= (Result) returnValue;
                    /*
                    处理嵌套Page类型
                     */
                    if(result.getData()!=null&&result.getData() instanceof Page){
                        serializeFilters.add(new SimplePropertyPreFilter(Page.class));
                    }
                }else if(returnValue instanceof Page){
                    /*
                    处理Page类型
                     */
                    serializeFilters.add(new SimplePropertyPreFilter(Page.class));
                }

                Class<?> type = jsonConvert.type();
                if(type.equals(DTO.class)){
                    createPropertyFilter(null,jsonConvert.includes(),jsonConvert.excludes(), serializeFilters);
                }else{
                    createPropertyFilter(type,jsonConvert.includes(),jsonConvert.excludes(), serializeFilters);
                }
                NestConvert[] nest = jsonConvert.nest();
                if(nest.length!=0){
                    for (NestConvert nestConvert : nest) {
                        createPropertyFilter(nestConvert.type(),nestConvert.includes(),nestConvert.excludes(), serializeFilters);
                    }
                }
                SerializeFilter[] filters = serializeFilters.toArray(new SerializeFilter[serializeFilters.size()]);

                writer.write(JSON.toJSONString(returnValue, filters, serializerFeatures));
            }
        }
    }


    private void createPropertyFilter(Class<?> clazz,String[] includes,String[] excludes, List<SerializeFilter> serializeFilters) {
        SimplePropertyPreFilter propertyPreFilter = new SimplePropertyPreFilter(clazz,includes);
        propertyPreFilter.getExcludes().addAll(Arrays.asList(excludes));
        serializeFilters.add(propertyPreFilter);
    }

}
