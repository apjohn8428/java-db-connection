package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import dbConnection.DBconnection;

public class Main01 {

	static Scanner sc = new Scanner(System.in);
	
	// 전역변수로 로그인한 유저의 아이디를 저장
	static String loginId = null;
	
	// 회원가입 메서드
	public static void join() {
		
		System.out.print("아이디 입력: ");
		String userid = sc.nextLine();
		userid = userid.trim();
		
		System.out.print("비밀번호 입력: ");
		String password = sc.nextLine();
		password = password.trim();
		
		System.out.print("이름 입력: ");
		String username = sc.nextLine();
		username = username.trim();
		
		// sql문 작성
		String sql = "INSERT INTO member(userid, password, username) VALUES(?, ?, ?)";
		// ?: 입력할 values의 위치와 개수만 잡아 놓은 것
		
		try {
			// DB 연결 커넥션 생성
			Connection conn = DBconnection.getConnection();
			
			// sql문 입력 객체 생성
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 사용자로부터 입력받은 값이 저장된 변수를 ? 자리에 대입
			pstmt.setString(1, userid);
			pstmt.setString(2, password);
			pstmt.setString(3, username);
			
			// sql문 실행: insert,update,delete -> executeUpadate / select -> executeQuery
			int result = pstmt.executeUpdate();
			
			if(result == 1) {
				System.out.println("회원가입 성공");
			} else {
				System.out.println("회원가입 실패");
			}
			
			conn.close();
			
		} catch(Exception e) {
			System.out.println("회원가입 실패");
			e.printStackTrace();
		}
				
	}
	
	// 회원탈퇴 메서드
	public static void memberDelete() {
		
		System.out.print("탈퇴할 아이디 입력: ");
		String userid = sc.nextLine();
		
		String sql = "DELETE FROM member WHERE userid = ?";
		
		try {
			
			Connection conn = DBconnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			
			int result = pstmt.executeUpdate();
			
			if(result == 1) {
				System.out.println("회원탈퇴 성공");
			} else {
				System.out.println("회원탈퇴 실패");
			}
			
			conn.close();
			
		} catch(Exception e) {
			System.out.println("회원탈퇴 실패");
			e.printStackTrace();
		}
	}
	
	// 회원정보 수정 메서드
	public static void memberUpdate() {
		
//		System.out.print("수정할 아이디 입력: "); // 실제 업무에서는 기본키 변경 x
//		String userid = sc.nextLine();
		
		if(loginId == null) {
			System.out.println("로그인한 유저만 정보수정이 가능합니다");
			login();
		}
		
		System.out.print("변경할 비밀번호 입력: ");
		String password = sc.nextLine();
		
		System.out.print("변경할 이름 입력 : ");
		String username = sc.nextLine();
		
		String sql = "UPDATE member SET password = ? WHERE userid = ?"; // 실제 업무에서는 로그인한 id를 ?에 자동으로 입력
		
		try {
			
			Connection conn = DBconnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, password);
			pstmt.setString(2, username);
			pstmt.setString(3, loginId);
			
			int result = pstmt.executeUpdate();
			
			if(result == 1) {
				System.out.println("비밀번호 수정 성공");
			} else {
				System.out.println("비밀번호 수정 실패");
			}
			
			conn.close();
			
		} catch(Exception e) {
			System.out.println("비밀번호 수정 실패");
			e.printStackTrace();
		}
	}
	
	// 특정 회원정보 출력 메서드
	public static void memberInfo() {
		
		System.out.print("정보를 출력할 아이디 입력: ");
		String userid = sc.nextLine();
		
		String sql = "SELECT * FROM member WHERE userid = ?";
		
		try {
			Connection conn = DBconnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			
			ResultSet rs = pstmt.executeQuery();
			
			// rs 내에 DB로부터 받은 레코드의 존재 여부 확인
			if(rs.next()) {
				System.out.println(userid + " 회원은 존재하는 회원입니다");
				String mid = rs.getString("userid"); // 필드 이름을 입력
				String mpw = rs.getString("password"); 
				String mname = rs.getString("username");
				String mdate = rs.getString("regdate");
				
				System.out.println("아이디, 비밀번호, 이름, 가입일시");
				System.out.println(mid + "/" + mpw + "/" + mname + "/" + mdate);

				
			} else {
				System.out.println(userid + " 회원은 존재하지 않는 회원입니다");
			}
			
			// rs를 먼저 닫고 conn을 닫아야한다
			rs.close();
			conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("회원정보 출력 실패");
		}
	}
	
	// 모든 회원정보 출력 메서드
	public static void allMemberInfo() {
		// 관리자 로그인 확인 검사
		if(loginId == null) {
			System.out.println("관리자로 로그인하세요");
			login();
		} 
		
		if(!loginId.equals("admin")) {
			System.out.println("관리자로 로그인하세요");
			login();
		}
		
		String sql = "SELECT * FROM member";
		
		try {
			Connection conn = DBconnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String mid = rs.getString("userid"); // 필드 이름을 입력
				String mpw = rs.getString("password"); 
				String mname = rs.getString("username");
				String mdate = rs.getString("regdate");
				
				System.out.println("아이디, 비밀번호, 이름, 가입일시");
				System.out.println(mid + "/" + mpw + "/" + mname + "/" + mdate);
				System.out.println("===================================");
			}
			
			rs.close();
			conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 로그인 메서드
	public static void login() {
		System.out.print("로그인 아이디 입력: ");
		String userid = sc.nextLine(); 
		System.out.print("로그인 비밀번호 입력: ");
		String password = sc.nextLine();
		
		String sql = "SELECT * FROM member WHERE userid = ? and password = ?";
		
		try {
			Connection conn = DBconnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			
			// rs 내에 DB로부터 받은 레코드의 존재 여부 확인
			if(rs.next()) {
				System.out.println("로그인 성공");
				
				String mid = rs.getString("userid"); // 필드 이름을 입력
				String mname = rs.getString("username");
				
				loginId = mid; // 로그인 성공한 유저의 아이디를 저장
				
				System.out.println(mname + "님 환영합니다"); // 이름으로 환영인사
				
			} else {
				System.out.println("아이디 또는 비밀번호가 잘못 입력되었습니다");
				login();
			}
			
			// rs를 먼저 닫고 conn을 닫아야한다
			rs.close();
			conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("회원정보 출력 실패");
		}
	}
	
	// 로그아웃 메서드
	public static void logout() {
		loginId = null;
		System.out.println("로그아웃 되셨습니다");
	}
	
	// 게시판 글쓰기 메서드
	public static void boardWrite() {
		
		if(loginId == null) {
			System.out.println("로그인 해주세요");
			login();
		}
		
		System.out.print("게시글 제목: ");
		String title = sc.nextLine();
		
		System.out.print("게시글 내용: ");
		String content = sc.nextLine();
		
		String sql = "INSERT INTO board(title, content, writer_id) VALUES (?, ?, ?)";
		
		try {
			Connection conn = DBconnection.getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setString(3, loginId);
			
			int result = pstmt.executeUpdate();
			
			if(result == 1) {
				System.out.println("게시판 글쓰기 성공");
			} else {
				System.out.println("게시판 글쓰기 실패");
			}
			
			conn.close();
			
		} catch(Exception e) {
			System.out.println("게시판 글쓰기 실패");
			e.printStackTrace();
		}
	}
	
	// 게시판 목록 조회 메서드
	public static void boardList() {
		
		String sql = "SELECT * FROM board";
		
		try {
			Connection conn = DBconnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int bnum= rs.getInt("bno"); // 필드 이름을 입력
				String btitle = rs.getString("title"); 
				String bcontent= rs.getString("content");
				String bwriter = rs.getString("writer");
				String bdate = rs.getString("regdate");
		
				System.out.println("===================================");
				System.out.println("번호: " + bnum);
				System.out.println("제목: " + btitle);
				System.out.println("내용: " + bcontent);
				System.out.println("작성자: " + bwriter);
				System.out.println("작성일: " + bdate);
			}
			System.out.println("===================================");
			
			rs.close();
			conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// 게시글 삭제 메서드
	public static void boardDelete() {
		
		if(loginId == null) {
			System.out.println("로그인 해주세요");
			login();
		}
	
		System.out.print("삭제할 게시글 번호입력: ");
		String bno = sc.nextLine();
		
		String sql = "DELETE FROM board WHERE bnum = ? and writer = ?";
		
		try {
			
			Connection conn = DBconnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, bno);
			pstmt.setString(2, loginId);
			
			int result = pstmt.executeUpdate();
			
			if(result == 1) {
				System.out.println("게시글 삭제 성공");
			} else {
				System.out.println("게시글이 없는 번호이거나 본인이 작성한 글만 삭제 가능합니다");
			}
			
			conn.close();
			
		} catch(Exception e) {
			System.out.println("게시글 삭제 실패");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// join(); 회원정보 입력
		// memberDelete(); 회원정보 삭제
		// memberUpdate(); 회원정보 수정
		// memberInfo(); 특정 회원정보 출력
		// allMemberInfo(); 전체 회원정보 출력 -> 관리자만 호출가능
		// login(); 로그인 기능
		// boardWrite();
		// boardDelete();
		 
		 while(true) {
			 System.out.println("");
			 System.out.println("==========JAVA+MySQL 게시판============");
			 System.out.println("1. 회원가입");
			 System.out.println("2. 로그인");
			 System.out.println("3. 회원정보 수정");
			 System.out.println("4. 회원탈퇴");
			 System.out.println("5. 게시판 목록");
			 System.out.println("6. 게시글 내용 보기");
			 System.out.println("7. 게시글 삭제");
			 System.out.println("8. 로그아웃");
			 System.out.println("9. 프로그램 종료");
			 System.out.println("=====================================");
			 
			 System.out.println("메뉴 선택: ");
			 String menu = sc.nextLine();
			 
			 if(menu.equals("1")) {
				 join();
			 } else if(menu.equals("2")) {
				 login();
			 } else if(menu.equals("3")) {
				 memberUpdate();
			 } else if(menu.equals("4")) {
				 memberDelete();
			 } else if(menu.equals("5")) {
				 allMemberInfo();
			 } else if(menu.equals("6")) {
				 boardList();
			 } else if(menu.equals("7")) {
				 boardDelete();
			 } else if(menu.equals("8")) {
				 logout();
			 } else if(menu.equals("9")) {
				 return;
			 } else {
				 System.out.println("잘못 입력된 번호입니다");
			 }
		 }
		 
	}
	

}
