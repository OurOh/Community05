package net.musecom.community.service;

import org.springframework.stereotype.Service;

import net.musecom.community.util.Paging;

@Service
public class PagingService {
    public Paging getPaging(int totalRecord, int listCount, int page, int pageCount) {
    	return new Paging(totalRecord, listCount, page, pageCount);
    }
}
