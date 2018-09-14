package cn.footman.search.service;

import cn.footman.common.pojo.SearchResult;


public interface SearchService {

    SearchResult search(String keyword,int page,int rows)throws Exception;
}
