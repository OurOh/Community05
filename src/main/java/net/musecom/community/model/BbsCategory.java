package net.musecom.community.model;

import lombok.Data;

@Data
public class BbsCategory {
  private int id;  //�⺻ ��
  private int bbsid;  //bbs�� ���̵� (�ܷ�Ű)
  private String categorytext;  //ī�װ� ����
  private int categorynum;  //ī�װ� ����
}
