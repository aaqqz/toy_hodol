package com.hodol.toy_hodol.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static java.lang.Math.*;

@Component
public class PaginationResolver extends PageableHandlerMethodArgumentResolver {
    private static final int MAX_PAGE_SIZE = 2000;

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String page = webRequest.getParameter(getParameterNameToUse(getPageParameterName(), methodParameter));
        String pageSize = webRequest.getParameter(getParameterNameToUse(getSizeParameterName(), methodParameter));

        int pageNumber = page != null ? max(0, Integer.parseInt(page) -1 ) : 0;
        int pageSizeNumber = pageSize != null ? min(Integer.parseInt(pageSize), MAX_PAGE_SIZE) : 10;

        return getPageable(methodParameter, Integer.toString(pageNumber), Integer.toString(pageSizeNumber));
    }

}
