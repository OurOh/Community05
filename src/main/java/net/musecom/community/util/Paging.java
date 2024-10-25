package net.musecom.community.util;

public class Paging {
   
   private int totalRecords;   //��ü �Խñ� ��
   private int recordsPerPage;  //�� �������� ������ �Խñ� ��
   private int currentPage;  //���� ������
   private int totalPages;   //��ü ������
   private int pagesPerGroup;  //�� ���� ������ ������ ��ȣ�� �� (��:10�̸� 1~10, 11~20)  
    private int currentGroup;  //���� ������ �׷�
   
   public Paging(int totalRecords, int recordsPerPage, int currentPage, int pagesPerGroup) {
      this.totalRecords = totalRecords;
      this.recordsPerPage = recordsPerPage;
      this.currentPage = currentPage;
      this.pagesPerGroup = pagesPerGroup;
      this.totalPages = (int)Math.ceil((double) totalRecords / recordsPerPage);
      this.currentGroup = (int) Math.ceil((double)currentPage / pagesPerGroup); 
   }
   
   //�������� ������ �׷쿡�� ù ��° ��ȣ
   public int getStartPageOfGroup() {
      return (currentGroup - 1) * pagesPerGroup + 1;
   }
   
   //�������� ������ �׷쿡�� ������ ��ȣ
   public int getEndPageOfGroup() {
      int endPage = currentGroup * pagesPerGroup;
      return Math.min(endPage, totalPages);
   }
   
   //���� �������� ���� �Խñ� ��ȣ
   public int getStartRecord() {
      return (currentPage -1) * recordsPerPage;   
   }
   
   //��ü ���ڵ�� ����
   public int getTotalRecords() {
      return totalRecords;
   }
   
   //�� �������� ������ ��ȣ �� ����
   public int getRecordsPerPage() {
      return recordsPerPage;
   }
   
   //���� ������ ��ȣ ����
   public int getCurrentPage() {
      return currentPage;
   }
   
   //��ü ������
   public int getTotalPages() {
      return totalPages;
   }
   
   public int getPagesPerGroup() {
      return pagesPerGroup;
   }
}
