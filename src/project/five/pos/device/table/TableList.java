package project.five.pos.device.table;

import java.util.ArrayList;

import project.five.pos.db.PosVO;

public class TableList {

	// ��ȸ�� ������ ��
	int row_length;
	int column_length;
	Object[][] lookUp_list;
	int column;

	// ��� DB ������� ����
	String btn_text;
	public TableList(String btn_text) {
		this.btn_text = btn_text;
	}

	public String[] header() {
		
		if (btn_text.equals("�Ǹ� ���� ��ȸ")) {
			String[] header = {"�Ǹ� ��¥", "�ֹ� ��ȣ", "��ǰ �̸�", "����", "����"};	
			return header;

		} else if (btn_text.equals("���� ���� ��ȸ")) {
			String[] header = {"������", "���� ����", "���� �̸�", "ī�� ��ȣ", "���ϸ��� ���", "���� �ݾ�", "��� �ݾ�","���� ���"};	
			return header;

		} else if (btn_text.equals("ȸ�� ���� ��ȸ")) {
			String[] header = {"ȸ�� ��ȣ", "�̸�", "��ȭ ��ȣ", "���", "�� ���ݾ�", "������", "���ϸ���"};
			return header;
		}
		return null;
	}

	public Object[][] data(ArrayList<PosVO> searchlist, String[] header) {
		row_length = searchlist.size();
		column_length = header.length;		

		lookUp_list = new Object[row_length][column_length];
		
		if (btn_text.equals("�Ǹ� ���� ��ȸ")) {
			for (int i = 0; i < row_length; i++) {
				column = 0;
			lookUp_list[i][column] = searchlist.get(i).getSaled_date();
			lookUp_list[i][++column] = searchlist.get(i).getOrder_no();
			lookUp_list[i][++column] = searchlist.get(i).getSaled_prdouct_name();
			lookUp_list[i][++column] = searchlist.get(i).getSelected_item();
			lookUp_list[i][++column] = searchlist.get(i).getTotal_price();
			};

		} else if (btn_text.equals("���� ���� ��ȸ")) { // Payment table ��ȸ
			for (int i = 0; i < row_length; i++) {
				column = 0;
				lookUp_list[i][column] = searchlist.get(i).getPayment_date();
				lookUp_list[i][++column] = searchlist.get(i).getPayment_type();
				lookUp_list[i][++column] = searchlist.get(i).getBank_id();
				lookUp_list[i][++column] = searchlist.get(i).getCard_num();
				lookUp_list[i][++column] = searchlist.get(i).getUsage_of_milage();
				lookUp_list[i][++column] = searchlist.get(i).getAmount_of_money();
				lookUp_list[i][++column] = searchlist.get(i).getActual_expenditure();
				lookUp_list[i][++column] = searchlist.get(i).getCoupon_no();
				
			};
			
		} else if (btn_text.equals("ȸ�� ���� ��ȸ")) { // customer table ��ȸ
			for (int i = 0; i < row_length; i++) {
				column = 0;
				lookUp_list[i][column] = searchlist.get(i).getCustomer_no();
				lookUp_list[i][++column] = searchlist.get(i).getM_last_name() + searchlist.get(i).getM_first_name();
				lookUp_list[i][++column] = searchlist.get(i).getM_contact_no();
				lookUp_list[i][++column] = searchlist.get(i).getMembership();
				lookUp_list[i][++column] = searchlist.get(i).getAmount_price();
				lookUp_list[i][++column] = searchlist.get(i).getAccumulation_pct();
				lookUp_list[i][++column] = searchlist.get(i).getMileage();
			};
			
		}
			
		return lookUp_list;
	}
}