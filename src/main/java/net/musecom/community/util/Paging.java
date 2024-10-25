package net.musecom.community.util;

public class Paging {
   
   private int totalRecords;   //전체 게시글 수
   private int recordsPerPage;  //한 페이지당 보여줄 게시글 수
   private int currentPage;  //현재 페이지
   private int totalPages;   //전체 페이지
   private int pagesPerGroup;  //한 번에 보여줄 페이지 번호의 수 (예:10이면 1~10, 11~20)  
    private int currentGroup;  //현재 페이지 그룹
   
   public Paging(int totalRecords, int recordsPerPage, int currentPage, int pagesPerGroup) {
      this.totalRecords = totalRecords;
      this.recordsPerPage = recordsPerPage;
      this.currentPage = currentPage;
      this.pagesPerGroup = pagesPerGroup;
      this.totalPages = (int)Math.ceil((double) totalRecords / recordsPerPage);
      this.currentGroup = (int) Math.ceil((double)currentPage / pagesPerGroup); 
   }
   
   //보여지는 페이지 그룹에서 첫 번째 번호
   public int getStartPageOfGroup() {
      return (currentGroup - 1) * pagesPerGroup + 1;
   }
   
   //보여지는 페이지 그룹에서 마지막 번호
   public int getEndPageOfGroup() {
      int endPage = currentGroup * pagesPerGroup;
      return Math.min(endPage, totalPages);
   }
   
   //현재 페이지의 시작 게시글 번호
   public int getStartRecord() {
      return (currentPage -1) * recordsPerPage;   
   }
   
   //전체 레코드수 게터
   public int getTotalRecords() {
      return totalRecords;
   }
   
   //한 페이지당 보여줄 번호 수 게터
   public int getRecordsPerPage() {
      return recordsPerPage;
   }
   
   //현재 페이지 번호 게터
   public int getCurrentPage() {
      return currentPage;
   }
   
   //전체 페이지
   public int getTotalPages() {
      return totalPages;
   }
   
   public int getPagesPerGroup() {
      return pagesPerGroup;
   }
}
