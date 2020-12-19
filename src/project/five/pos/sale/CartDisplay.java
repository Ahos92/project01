package project.five.pos.sale;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import project.five.pos.TestSwingTools;
import project.five.pos.db.PosDAO;
import project.five.pos.db.PosVO;
import project.five.pos.sale.btn.CartBtn;
import project.five.pos.sale.btn.action.*;
import project.five.pos.sale.btn.render.*;

public class CartDisplay extends JFrame {

	JPanel south_p, center_p;

	JButton pay_btn, cancle_btn;

	JLabel info_lab;
	
	JTable cart_table;
	DefaultTableModel dtm;
	JScrollPane scroll;
	String[] header= {"메뉴", "옵션", "수량", "가격", "▲", "▼", "취소"};
	int cell_btn_size;
	
	int order_num;
	PosDAO pos;
	
	public CartDisplay(String device_id, Object[][] select_list) {	
		pos = new PosDAO();
		
		setLayout(new BorderLayout());
		
		south_p = new JPanel();
		center_p = new JPanel();

		info_lab = new JLabel("주문 내역");
		
		// 주문 번호
		order_num = pos.MaxOrderNumber();
		order_num++;
		
		// 선택된 상품 테이블
		dtm = new DefaultTableModel(select_list, header);
		cart_table = new JTable(dtm);
		scroll = new JScrollPane(cart_table);
		scroll.setPreferredSize(new Dimension(480, 100));
		
		// 수량 조절 버튼
		cell_btn_size = 40;	
		createcellBtn(cart_table, "취소", cell_btn_size);
		
		createcellBtn(cart_table, "▲", cell_btn_size);
		
		createcellBtn(cart_table, "▼", cell_btn_size);

		// 결제
		pay_btn = new CartBtn("결제", new PaymentPageAction(this, dtm, order_num, device_id));
		// 취소
		cancle_btn = new CartBtn("취소", new CancleAction(this, dtm));
	
		center_p.add(scroll);
		south_p.add(pay_btn);
		south_p.add(cancle_btn);
		
		add(info_lab, BorderLayout.NORTH);
		add(south_p, BorderLayout.SOUTH);
		add(center_p, BorderLayout.CENTER);
		
		TestSwingTools.initTestFrame(this, "장바구니 화면", true);
	}
	
	/*
	 *	테이블에 삽입할 버튼 만드는 메서드 		
	 */
	private void createcellBtn(JTable cart_table, String btn_txt, int cell_btn_size) {
		if (btn_txt.equals("취소")) {
			cart_table.getColumn(btn_txt).setCellRenderer(new DeleteBtnRender());
			cart_table.getColumn(btn_txt).setCellEditor(new DeleteAction(new JCheckBox(), cart_table, dtm, this));
			cart_table.getColumn(btn_txt).setPreferredWidth(cell_btn_size);
		} else {
			cart_table.getColumn(btn_txt).setCellRenderer(new UpDonwBtnRender(btn_txt));
			cart_table.getColumn(btn_txt).setCellEditor(new UpDownAction(new JCheckBox(), cart_table, btn_txt));
			cart_table.getColumn(btn_txt).setPreferredWidth(cell_btn_size);
		}
	}

}

