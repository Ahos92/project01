package project.five.pos.payment.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import project.five.pos.db.DBManager;
import project.five.pos.payment.swing.btn.action.ClickedBtnAction;
import project.five.pos.payment.swing.btn.action.NumberField;

// 주의할점
//라벨에 버튼 넣을수 없음 = 둘 다 컴포넌트 이기 때문에
//패널에 넣으면 너무 커짐


public class AskCoupon extends JFrame {
	
	static Connection conn;
	static PreparedStatement ps;
	//static PreparedStatement ps2;
	static ResultSet rs;
	//static ResultSet rs2;
	
	
	int price;
	static int change;
	
	static int couponPrice;
	static int couponNo = 0;
	static String couponName;
	static String startDate;
	static String expiredDate;
	
	static int device_id = 1004;
	
	public AskCoupon(int price) {
		this.price = price;
		Container c = this.getContentPane();

		LocalDate today = LocalDate.now();
		
		setTitle("쿠폰");
		
        // 쿠폰 입력 패널 3개 1. 사용유무 2. 쿠폰입력 3. 쿠폰 이미지, 할인 가격 표시
		JPanel panel04 = new JPanel(new CardLayout(15, 15));
		
		// 1. 사용유무
		JPanel yes_or_no = new JPanel(new GridLayout(2,1,0,0));
		// 2. 쿠폰 입력
		JPanel input_coupon = new JPanel(new GridLayout(3,1,0,0));
		// 3. 쿠폰 이미지 + 원래가격, 할인 가격 표시
		JPanel display_price = new JPanel(new GridLayout(1,2,0,0));
		
		JLabel ask = new JLabel("쿠폰을 사용하시겠습니까?", SwingConstants.CENTER);
	
		JPanel yesnobtn = new JPanel(new FlowLayout());
		
		JButton yes = new JButton("예");
		
		JPanel result_panel = new JPanel(new GridLayout(3,1,0,0));
		result_panel.setOpaque(false);
		
		JLabel result_explain = new JLabel("주문하신 상품 리스트", SwingConstants.CENTER);
		result_explain.setForeground(Color.WHITE);
		result_explain.setFont(new Font("Serif",Font.BOLD, 36));
		
		JPanel order_list = new JPanel(new FlowLayout());
		order_list.setOpaque(false);
		
		JLabel order_cart = new JLabel(PayPanel.lists);
		order_cart.setForeground(Color.WHITE);
		order_cart.setFont(new Font("Serif",Font.BOLD, 14));
		
		JLabel order_no = new JLabel("주문 번호 : " + PayPanel.orderNumber);
		order_no.setForeground(Color.WHITE);
		order_no.setFont(new Font("Serif",Font.BOLD, 36));
		
		order_list.add(order_cart);
		
		result_panel.add(result_explain);
		result_panel.add(order_list);
		result_panel.add(order_no);
		
		PayPanel.main_center_panel.add("결제후", result_panel);
				
		JButton no = new JButton("아니요");
		
		no.addActionListener(new ActionListener() {

			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				JButton btn = (JButton) e.getSource();
				if(btn.getText().equals("아니요")) {
															
					try {
						conn = DBManager.getConnection();
						
						String sql = "INSERT INTO payment VALUES (payy_seq.nextval, ?, ?, ?, ?, ?, ?, ?)";
						
						ps = conn.prepareStatement(sql);
						
						ps.setString(1, ClickedBtnAction.getPaymentType().toString());
						ps.setString(2, today.toString());
						ps.setString(3, PaidByCard.cardId);
						ps.setString(4, PaidByCard.cardNumber);
						ps.setInt(5, PaidByCash.i_money);
						ps.setInt(6, couponNo);
						ps.setInt(7, device_id);
						
						rs = ps.executeQuery();
						
						if(rs != null) rs.close();
						if(ps != null) ps.close();
						if(conn != null) conn.close();
						
						PaidByCard.cardId = "";
						PaidByCard.cardNumber = "";
						PaidByCash.i_money = 0;
						couponNo = 0;
						
					} catch (SQLException e1) {					
						e1.printStackTrace();
					}
																												
					PayPanel.card_btn.setText("카드");
					PayPanel.card_btn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
					PayPanel.cash_btn.setEnabled(true);
						
					PayPanel.cash_btn.setText("현금");
					PayPanel.cash_btn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
					PayPanel.card_btn.setEnabled(true);
					
					PayPanel.main_card.show(PayPanel.main_center_panel, "결제후");
					dispose();				
			}
				
			}
			
		});
		
		// 다음 결제 프로세스 창
		yes.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getButton() == MouseEvent.BUTTON1) {
					((CardLayout)panel04.getLayout()).next(panel04);
				}
			}
		});
		
		// 결제 취소 버튼 (취소 클래스화 하기)
		no.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getButton() == MouseEvent.BUTTON1) {
					dispose();
				}
			}
		});
		
		JLabel input_label = new JLabel("쿠폰번호를 입력해주세요", SwingConstants.CENTER);
		
		JPanel input_field = new JPanel(null);
		JTextField input_text = new JTextField(6);
		input_text.setBounds(175,0,100,25);
		
		input_text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				 
				  JTextField src = (JTextField) e.getSource();
				  
				  if (!Character.isDigit(c)) {
					  e.consume();
					  return;
				  }
				  
				  else if(src.getText().length() >= 6) {
					  e.consume();
					  return;
				  }	  
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {	
			}
		});
		
		JPanel input_btn = new JPanel(new FlowLayout());
		JButton check = new JButton("확인");
		JButton cancel = new JButton("취소");
		
		
		JPanel left_coupon = new JPanel(null);
		JPanel coupon_name = new JPanel();
		coupon_name.setBounds(0, 0, 200, 25);
		
		JPanel coupon_img = new JPanel(new CardLayout(15, 15));
		coupon_img.setBounds(0, 25, 220, 90);
					
		JPanel right_coupon = new JPanel(new GridLayout(2,1,0,0));
		JPanel total_calc = new JPanel();
		JPanel lcheck_panel = new JPanel(); 
		
		for(CouponEnum cpname : CouponEnum.values()) {
			coupon_img.add(cpname.getCname(), new CouponLabel(cpname));
		}
		
	
		JButton check2 = new JButton("확인");
		
		check2.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					conn = DBManager.getConnection();
					
					String sql = "INSERT INTO payment VALUES (payy_seq.nextval, ?, ?, ?, ?, ?, ?, ?)";
					
					ps = conn.prepareStatement(sql);
					
					ps.setString(1, ClickedBtnAction.getPaymentType().toString());
					ps.setString(2, today.toString());
					ps.setString(3, PaidByCard.cardId);
					ps.setString(4, PaidByCard.cardNumber);
					ps.setInt(5, PaidByCash.i_money);
					ps.setInt(6, AskCoupon.couponNo);
					ps.setInt(7, device_id);
					
					rs = ps.executeQuery();
					
					if(rs != null) rs.close();
					if(ps != null) ps.close();
					if(conn != null)conn.close();
					
					PaidByCard.cardId = "";
					PaidByCard.cardNumber = "";
					PaidByCash.i_money = 0;
					couponNo = 0;
					
				} catch (SQLException e1) {					
					e1.printStackTrace();
				}
				
				PayPanel.card_btn.setText("카드");
				PayPanel.card_btn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				PayPanel.cash_btn.setEnabled(true);
					
				PayPanel.cash_btn.setText("현금");
				PayPanel.cash_btn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				PayPanel.card_btn.setEnabled(true);
				
				PayPanel.main_card.show(PayPanel.main_center_panel, "결제후");		
				
				
				new SuccessPayment();
				dispose();
			}
		});
		
		JButton cancel2 = new JButton("취소");
		
		// 결제 취소 버튼
		cancel2.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
												
				new CancelPayment();
				dispose();								
			}
		});
		
		PayPanel.main_center_panel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					//버튼 기능(함수 ClickedBtnAction)
					PayPanel.main_card.show(PayPanel.main_center_panel, "결제전");	
					
				}
				
			}
		});
		
		// 다음 결제 프로세스 창
		check.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					couponNo = 0;
					
					if(e.getButton() == MouseEvent.BUTTON1) {
							couponNo = Integer.parseInt(input_text.getText());
							
							try {
								conn = DBManager.getConnection();

								ps = conn.prepareStatement("SELECT * FROM coupon "
										+ "WHERE coupon_no = ?");
								
								ps.setInt(1, couponNo);
								
								rs = ps.executeQuery();
								
								couponName = "";
								couponPrice = 0;
								startDate ="";
								expiredDate = "";
								if(rs.next()) {
									
									do {
										AskCoupon.couponNo = rs.getInt("coupon_no");
										AskCoupon.couponName = rs.getString("coupon_name");
										AskCoupon.couponPrice = rs.getInt("coupon_price");
										AskCoupon.startDate = rs.getString("start_date").substring(0, 10);
										AskCoupon.expiredDate = rs.getString("expired_date").substring(0, 10);
										
										// 테스트 코드
//										System.out.printf("%-15s%-10d%-10s%-10s\n",
//												rs.getString("coupon_name"),
//												rs.getInt("coupon_price"),
//												rs.getString("start_date").substring(0, 10),
//												rs.getString("expired_date").substring(0, 10)
//												);
																												
										LocalDate stDate = LocalDate.parse(AskCoupon.startDate);
										LocalDate exDate = LocalDate.parse(AskCoupon.expiredDate);
										// 기한이 지난 쿠폰
										if(today.isBefore(stDate) || today.isAfter(exDate)) {
											new ExpiredCoupon();
										}
										else {
										JLabel coupon_label = new JLabel("사용하신 쿠폰은 : " + AskCoupon.couponName + "입니다.");
										
										((CardLayout)coupon_img.getLayout()).show(coupon_img, AskCoupon.couponName);
										
										// 거스름돈 계산
										change = PaidByCash.i_money - (price - AskCoupon.couponPrice);
										
										if(ClickedBtnAction.getPaymentType().toString().contains("CARD")) {
											JLabel calc_label2 = new JLabel("<html>카드 결제 입니다<br/>결제 금액 : " + price + "-" + AskCoupon.couponPrice + "=" + (price - AskCoupon.couponPrice) + " (원)<html>", SwingConstants.CENTER);
											total_calc.add(calc_label2);
											}
										else {
											JLabel calc_label = new JLabel("<html>결제 금액 : " + price + "-" + AskCoupon.couponPrice + "=" + (price - AskCoupon.couponPrice) + " (원)<br/>입금 금액 : " + PaidByCash.i_money + "(원)"
													+ "<br/>거스름돈 : "+  change + "(원)</html>", SwingConstants.CENTER);
											total_calc.add(calc_label);
										}
										
										
										
										
										// 테스트 코드
										//System.out.println(change);
										
										// - 세번째 패널
										coupon_name.add(coupon_label);
										left_coupon.add(coupon_name);
										left_coupon.add(coupon_img);
										
										
										lcheck_panel.add(check2);
										lcheck_panel.add(cancel2);
										right_coupon.add(total_calc);
										right_coupon.add(lcheck_panel);
										
										display_price.add(left_coupon);
										display_price.add(right_coupon);
											
										// 메인에 3의 패널 추가
										panel04.add(display_price);

										((CardLayout)panel04.getLayout()).next(panel04);
										}
										
									}while (rs.next()); 
										
								}
								else {
									// 존재하지 않는 쿠폰 입니다
									new NoneCoupon();	
								}
								
								rs.close();
								ps.close();
								conn.close();
								
							} catch (SQLException ex) {
								ex.printStackTrace();
							}
							
					}
						
				}
			});
			
		// 뒤로가기 버튼
		cancel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					((CardLayout)panel04.getLayout()).previous(panel04);
				}
				
			}
		});
		
		// - 첫번째 패널
		yesnobtn.add(yes);
		yesnobtn.add(no);
		yes_or_no.add(ask);
		yes_or_no.add(yesnobtn);
		
		// - 두번째 패널
		input_field.add(input_text);
		input_btn.add(check);
		input_btn.add(cancel);
		input_coupon.add(input_label);
		input_coupon.add(input_field);
		input_coupon.add(input_btn);
		
		// 메인에 1, 2의 패널들 추가
		panel04.add(yes_or_no);
		panel04.add(input_coupon);
		
		setVisible(true);
		//setLayout(null);
		setSize(500, 185);
		setLocation(1180, 500);
		
		this.add(panel04, BorderLayout.CENTER);
	}

}
