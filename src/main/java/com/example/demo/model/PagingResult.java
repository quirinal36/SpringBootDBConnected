package com.example.demo.model;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PagingResult {
	private final static int PAGE_SIZE_LIST = 10;
	private final static int PAGE_SIZE_CARD = 36;
	protected String query;		// 검색어
	protected int pageNo; 		// 페이지 번호
	
	private int pageSize; 		// 게시 글 수
    private int firstPageNo; 	// 첫 번째 페이지 번호
    private int prevPageNo; 	// 이전 페이지 번호
    private int startPageNo; 	// 시작 페이지 (페이징 네비 기준)
    private int endPageNo; 		// 끝 페이지 (페이징 네비 기준)
    private int nextPageNo; 	// 다음 페이지 번호
    private int finalPageNo; 	// 마지막 페이지 번호
    private int from;
    
	public static final String SUCCESS_MESSAGE = "성공";
    public static final String SERVER_ERROR_MESSAGE = "실패";

    private HttpStatus statusCode;
    private String message;
    private Object data;
    private int totalCount;

    public PagingResult(){}

    public static PagingResult successInstance(){
        return new PagingResult().success();
    }
    public static PagingResult failInstance(){
        return new PagingResult().fail();
    }

    public PagingResult success(){
        statusCode = HttpStatus.OK;
        message = SUCCESS_MESSAGE;
        return this;
    }

    public PagingResult fail(){
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        message = SERVER_ERROR_MESSAGE;
        return this;
    }

    public PagingResult setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public PagingResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public PagingResult setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if(this.totalCount < this.pageSize) {
        	this.pageNo = 1;
        }
        
        this.makePaging();
        return this;
    }

    
    public PagingResult setData(Object data) {
    	this.data = data;
    	return this;
    }
    
    public int getStatusCode() {
        return statusCode.value();
    }
    

    /**
     * 페이징 생성
     */
    private void makePaging() {
        if (this.totalCount == 0) return; // 게시 글 전체 수가 없는 경우
        if (this.pageNo == 0) this.setPageNo(1); // 기본 값 설정
        if (this.pageSize == 0) this.setPageSize(PAGE_SIZE_LIST); // 기본 값 설정

        int finalPage = (totalCount + (pageSize - 1)) / pageSize; // 마지막 페이지
        if (this.pageNo > finalPage) this.setPageNo(finalPage); // 기본 값 설정

        if (this.pageNo < 0 || this.pageNo > finalPage) this.pageNo = 1; // 현재 페이지 유효성 체크

        boolean isNowFirst = pageNo == 1 ? true : false; // 시작 페이지 (전체)
        boolean isNowFinal = pageNo == finalPage ? true : false; // 마지막 페이지 (전체)

        int startPage = ((pageNo - 1) / 10) * 10 + 1; // 시작 페이지 (페이징 네비 기준)
        int endPage = startPage + 10 - 1; // 끝 페이지 (페이징 네비 기준)

        if (endPage > finalPage) { // [마지막 페이지 (페이징 네비 기준) > 마지막 페이지] 보다 큰 경우
            endPage = finalPage;
        }

        this.setFirstPageNo(1); // 첫 번째 페이지 번호

        if (isNowFirst) {
            this.setPrevPageNo(1); // 이전 페이지 번호
        } else {
            this.setPrevPageNo(((pageNo - 1) < 1 ? 1 : (pageNo - 1))); // 이전 페이지 번호
        }

        this.setStartPageNo(startPage); // 시작 페이지 (페이징 네비 기준)
        this.setEndPageNo(endPage); // 끝 페이지 (페이징 네비 기준)

        this.setFrom((this.getPageNo()-1) * this.getPageSize());
        
        if (isNowFinal) {
            this.setNextPageNo(finalPage); // 다음 페이지 번호
        } else {
            this.setNextPageNo(((pageNo + 1) > finalPage ? finalPage : (pageNo + 1))); // 다음 페이지 번호
        }

        this.setFinalPageNo(finalPage); // 마지막 페이지 번호
    }
}
