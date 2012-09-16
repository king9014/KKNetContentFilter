package cn.jinren.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * STRING FILTER CHAIN
 * @author KING
 * @date 2012.9.15
 */
public class StrFilterChain implements StrFilter {

	List<StrFilter> strFilters = new ArrayList<StrFilter>();
	
	public StrFilterChain addStrFilter(StrFilter strFilter) {
		strFilters.add(strFilter);
		return this;
	}
	
	public StrFilterChain addStrFilter(StrFilter[] strFilter) {
		for(StrFilter filter : strFilter) {
			strFilters.add(filter);
		}
		return this;
	}
	
	public StrFilterChain removeStrFilter(StrFilter strFilter) {
		strFilters.remove(strFilter);
		return this;
	}
	
	@Override
	public String doFilter(String str) {
		String result = str;
		for(StrFilter strFilter : strFilters) {
			result = strFilter.doFilter(result);
		}
		return result;
	}

}
