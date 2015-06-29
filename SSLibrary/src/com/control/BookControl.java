package com.control;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.command.BookUploadCommand;
import com.entity.Book;
import com.entity.BookLog;
import com.entity.User;
import com.entity.UserBook;
import com.frame.Biz;
import com.frame.SearchBiz;
import com.frame.UpdateAndReturnBiz;
import com.util.Nav;

@Controller
public class BookControl {
	@Resource(name="userbiz") //user정보
	Biz userbiz;
	
//	---------------------------------------
	@Resource(name="bookbiz") //책 정보
	Biz bookbiz;
	@Resource(name="bookbiz") //책 검색
	SearchBiz sbookbiz;
//	---------------------------------------	
	@Resource(name="userbookbiz") //대여시 회원의 책들 정보
	Biz userbookbiz;
	@Resource(name="userbookbiz") //대여시 회원의 책들 정보에서 검색
	SearchBiz suserbookbiz;
	@Resource(name="userbookbiz") //반납 시 isreturn y로 바꿔주는 것
	UpdateAndReturnBiz upreuserbookbiz;
//	---------------------------------------	
	@Resource(name="booklogbiz") //모든 대여한 회원들의 책 정보
	Biz booklogbiz;
	@Resource(name="booklogbiz") //대여한 모든 회원들의 책정보에서 검색
	SearchBiz sbooklogbiz;
	@Resource(name="booklogbiz") //책 연장, 반납 시 업데이트 할 때 필요
	UpdateAndReturnBiz uprebiz;
	
//	-------------------------------------Book------------------------------------------
	
	@RequestMapping("/bookmain.do")  //메인////////////////////////////////////////////
	public ModelAndView bookmain(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("main");
		ArrayList<Object> list = null;
		try {
			list= bookbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.addObject("booklist",list);
		mv.addObject("nav", Nav.book);
		mv.addObject("center", "book/booksearch.jsp");
		return mv; 
	}
	
	/*@ResponseBody //ajax용 booklist 불러오는것
	@RequestMapping("/booklist.do")
	public ResponseEntity<String> booklist() throws Exception{
		ResponseEntity<String> returnData = null;
		
		HttpHeaders header = new HttpHeaders(); 
		header.add("Content-type", "application/json;charset=EUC-KR");
		ArrayList<Object> list = new ArrayList<Object>();
		list = bookbiz.get();
		
		JSONArray ja = new JSONArray();
		for(Object obj:list){
			Book book = (Book)obj;
			JSONObject jo = new JSONObject();
			jo.put("id", book.getId());
			jo.put("name", book.getName());
			jo.put("writer", book.getWriter());
			jo.put("img", book.getImg());
			jo.put("floor", book.getFloor());
			jo.put("total_qt", book.getTotal_qt());
			jo.put("current_qt", book.getCurrent_qt());
			jo.put("reg_date", book.getReg_date());
			
			ja.add(jo);
		}
		returnData = new ResponseEntity<String>(
				ja.toJSONString(),
				header,
				HttpStatus.CREATED //강제로 결과를 만들어 넣어주는것
				);
		return returnData;
	}*/
	
	@ResponseBody //검색하기////////////////////////////////////////////
	@RequestMapping("/booksearch.do")
	public ResponseEntity<String> booksearch(String issearch, String category,String search){
		ResponseEntity<String> returnData = null;
		
		HttpHeaders header = new HttpHeaders(); 
		header.add("Content-type", "application/json;charset=EUC-KR");
		
		ArrayList<Object> list = new ArrayList<Object>();
		ArrayList<Object> resultlist = new ArrayList<Object>();
		ArrayList<Object> sublist1 = new ArrayList<Object>();
		ArrayList<Object> sublist2 = new ArrayList<Object>();	
		
		System.out.println(issearch);
		System.out.println(category);
		System.out.println(search);

		if(issearch.equals("name")){ //책제목 검색할 때
			try {
				list = sbookbiz.getname(search);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(issearch.equals("writer")){ //글쓴이 검색할 때
				try {
					list = sbookbiz.getwriter(search);
				} catch (Exception e) {
					e.printStackTrace();
				}	
		}else{ //책제목과 글쓴이 모두에서 검색할 때
			try {
				list = sbookbiz.getname(search);
				for (Object o : list) {
					sublist1.add(o);
				}
				sublist2 = sbookbiz.getwriter(search);
				for (Object o1 : sublist2) {
					Book b1 = (Book) o1;
					for (Object o2 : sublist1) {
						Book b2 = (Book) o2;
						if(b1.getId().equals(b2.getId())){
						}else{
							sublist1.add(o1);							
						}
					}
				}
				
				list = sublist1;				
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		switch(category){ //카테고리 분류
		case "i": //카테고리가 IT일 경우
			for (Object o : list) {
				Book b = (Book) o;
				if(b.getId().substring(0,1).equals("i")){
					resultlist.add(o);
					System.out.println(resultlist);
				}
			}
			break;
		case "n": //카테고리가 소설일 경우
			for (Object o : list) {
				Book b = (Book) o;
				if(b.getId().substring(0,1).equals("n")){
					resultlist.add(o);
				}
			}
			break;
		case "m": //카테고리가 만화책일 경우
			for (Object o : list) {
				Book b = (Book) o;
				if(b.getId().substring(0,1).equals("m")){
					resultlist.add(o);
				}
			}
			break;
		default :  //카테고리 전체에서 검색
				resultlist = list;
			break;
		}
		
		
		JSONArray ja = new JSONArray();
		for(Object obj:resultlist){ //resultlist를 jason으로 넘겨줌
			Book book = (Book)obj;
			JSONObject jo = new JSONObject();
			jo.put("bid", book.getId());
			jo.put("name", book.getName());
			jo.put("writer", book.getWriter());
			jo.put("img", book.getImg());
			jo.put("floor", book.getFloor());
			jo.put("total_qt", book.getTotal_qt());
			jo.put("current_qt", book.getCurrent_qt());
			jo.put("reg_date", book.getReg_date());
			
			ja.add(jo);
		}
		returnData = new ResponseEntity<String>(
				ja.toJSONString(),
				header,
				HttpStatus.CREATED //강제로 결과를 만들어 넣어주는것
				);
		return returnData;
	}
	
	@RequestMapping("/bookdetail.do") //책 아이디 눌렀을 때 나오는 책 상세 정보//////////////////////////
	public ModelAndView bookdetail(String id){
		ModelAndView mv = new ModelAndView("main");
		Object result = null;
		try {
			result= bookbiz.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.addObject("bookdetail",result);
		mv.addObject("nav", Nav.bookdetail);
		mv.addObject("center", "book/bookdetail.jsp");
		return mv; 
	}
	
	@RequestMapping("/bookregister.do") // 책 등록페이지////////////////////////////////////////
	public ModelAndView bookregister(){
		ModelAndView mv = new ModelAndView("main");
		mv.addObject("nav", Nav.bookregister);
		mv.addObject("center", "admin/book/register.jsp");
		return mv; 
	}
	
	@RequestMapping("/bookregisterimpl.do") // 책 등록impl////////////////////////////////////
	public String bookregisterimpl(HttpServletRequest request, BookUploadCommand book){
		Book b = new Book(book.getId(), book.getName(), book.getWriter(), 
				book.getImg().getOriginalFilename(), book.getFloor(), 
				book.getTotal_qt(), book.getTotal_qt());
		System.out.println(b);
		try {
			Book b1= (Book) bookbiz.register(b);
			HttpSession session = request.getSession();
			session.setAttribute("bookregister", b1);
			System.out.println(b1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MultipartFile file = book.getImg();
		String dir = "c:/lib/SSLibrary/web/img/book/";
		String img = file.getOriginalFilename();
		System.out.println(img);
	
			byte[] data;
			try {
				data = file.getBytes();
				FileOutputStream out;
				out = new FileOutputStream(dir
						+ file.getOriginalFilename());
				out.write(data);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
				
		return "redirect:/bookmain.do";
	}
	
	
	@RequestMapping("/bookremoveimpl.do") // 책 삭제impl(참고 : 책을 누구 하나라도 빌리고 있을 시에 삭제가 되지 않음)//
	   public ModelAndView bookremoveimpl(String id, HttpServletRequest request){
	      ModelAndView mv = new ModelAndView("main");
	      Object IsDelete = null;
	      ArrayList<Object> list = null;
	         try {
	            bookbiz.remove(id);
	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         try {
	            IsDelete = bookbiz.get(id);
	            if(IsDelete==null){
	               IsDelete = 0;
	            }else{
	               IsDelete = 1;
	            }
	            list= bookbiz.get();
	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         
	         mv.addObject("isdelete",IsDelete);
	        mv.addObject("booklist",list);
	         mv.addObject("nav", Nav.book);
	         mv.addObject("center", "book/booksearch.jsp");
	      return mv;   
	   }
	
	@RequestMapping("/bookmodify.do") //책 수정페이지////////////////////////////////////////////
	public ModelAndView bookmodify(String id) throws Exception{
		ModelAndView mv = new ModelAndView("main");
			Object b = bookbiz.get(id);
		mv.addObject("bookinfo",b);
		mv.addObject("nav", Nav.bookupdate);
		mv.addObject("center", "admin/book/update.jsp");
		return mv;
	}
	
	@RequestMapping("/bookmodifyimpl.do") //책 수정(기존 사진과 새로운 사진으로 번갈아서 넣을 수 있음)///////////
	public ModelAndView bookmodifyimpl(HttpServletRequest request, BookUploadCommand book) throws Exception{
		System.out.println(book.getId()+" "+book.getName() +" "+ book.getWriter());
		System.out.println(book.getImg().getOriginalFilename()+" "+book.getFloor()+" "+book.getCurrent_qt());
		System.out.println(book.getTotal_qt()+" "+book.getReg_date());
		String img = book.getImg().getOriginalFilename();
		
		String oldimg = request.getParameter("oldimg");
		Book b;
		
		if(img==null || img.equals("")){
			b = new Book(book.getId(),book.getName(), book.getWriter(), oldimg,
					book.getFloor(),book.getTotal_qt(),book.getCurrent_qt());	
			
			System.out.println("oldimg : "+oldimg);
			
		}else{
			b = new Book(book.getId(),book.getName(), book.getWriter(), 
					img ,book.getFloor(),book.getTotal_qt(),book.getCurrent_qt());	
			
			System.out.println("new img : "+img);
			
			MultipartFile file = book.getImg();
			String dir = "c:/lib/SSLibrary/web/img/book/";
			if(file!= null){
				byte[] data = file.getBytes(); //올라온 데이터를 byte array로 변환함. (모든 파일 가능)
				FileOutputStream out = new FileOutputStream(dir+file.getOriginalFilename());
				out.write(data);
				out.close();
			}
		}
		
		bookbiz.modify(b);
		
		ModelAndView mv = new ModelAndView("redirect:/bookdetail.do?id="+book.getId());	
		return mv;
	}
	
	
	
//	--------------------------------------UserBook---------------------------------------
	

	@RequestMapping("/userbookregister.do")  //책 대여하기
	public ModelAndView userbookregister(HttpServletRequest request, String id){
		ModelAndView mv = new ModelAndView("main");		
		HttpSession session = request.getSession();
		String uid = session.getAttribute("id").toString(); //회원 아이디 정보 세션에서 가져오기
		User user = null;
		int  borrowbook = 0; // 책을 빌렸는지 확인 여부 
		//(0 : 아무일도 없음 / 1 : 중복 대여 불가  / 2 : 갯수없어 대여할 수 없음 / 3 : 대여완료  )
		int overlap = 0; //대여가 중복되었는지 여부 (1 : 중복됨 / 2 : 중복 안됨)
		
		Book upbook; // 빌리려는 책 정보 가져오는 곳
		Book upbooknew;  //대여 성공시 대여가능 수 1개 줄이기 위해 넣어줘야 하는 책 업데이트 정보	
		int current_qt = 0; //대여 가능한 책이 몇개인지 가져오는 변수
		
		
		try {
			user = (User) userbiz.get(uid);  //지금 누구 회원이 로그인 했는지 회원 아이디 가져오기
			System.out.println("지금 로그인 한 user  :  "+user.getId());
			System.out.println("빌리려는 책 아이디 : "+id); //지금 빌리려고 하는 책 id
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		//1. 대여 중복인 경우 
		//로그인한 회원이 어떤 책들을 빌렸었는 지 userbook에서 가져온다.(arraylist에 for문으로 넣기)
		//지금 빌리는 책 아이디와 비교한다.(if 문으로 비교)
		//중복된 책이 있다면 빌려주지 않는다. (borrowbook =1)
		
		ArrayList<Object> userbooklist = new ArrayList<Object>(); //회원이 빌렸던 책들 알기 위해 만든 변수
		try {
			userbooklist = suserbookbiz.getid(uid);// user가 빌린 책 넣기
			System.out.println("user가 빌린 책 : "+userbooklist);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		String bid ="";
		
		if(userbooklist.size()==0){ // 대여를 한번도 안한 경우
			overlap=2;
			System.out.println("중복 안됬지롱");
			
		}else{ // 대여를 한번이라도 한 사람인 경우
			
			for (Object obj : userbooklist) { //회원이 빌린 책id들과 지금 대여하려는 책 id 비교함
				UserBook userbook = (UserBook) obj; 
				bid = userbook.getB_id();// id 뽑아와서 넣기
			
				if(bid.equals(id) || bid==id){// 대여할려는 책이 중복일 경우
					System.out.println("---------------------이미 대여한 책이라 빌릴 수 없음---------------------");
					overlap =1; //중복된 경우
					borrowbook =1;
					try {
						upbook = (Book)bookbiz.get(id);//현재 빌리려는 책의 정보를 가져온다.
						mv.addObject("bookdetail",upbook);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					} 
					
				}else{
					overlap =2; //중복 안 된경우
				}
			}
			System.out.println("중복 여부 : "+overlap);
		}	
		
		if(overlap==2){ //중복이 안된 경우
			//2. 대여할 책이 없는 경우(중복 안되고 current_qt = 0)
			//현재 책 아이디에서 남은 갯수를 가져온다.
			//남은 갯수(current_qt)가 0인것은 대여할 수 없다는 표시를 한다. (borrowbook =2)
		
			try {
					upbook = (Book)bookbiz.get(id); //현재 빌리려는 책의 정보를 가져온다.
					current_qt= upbook.getCurrent_qt(); // 대여 가능한 수 확인한다.
				
						
					if(current_qt==0){ //대여 가능한 책 수량이 0일 경우
							System.out.println("---------------------대여 가능한 책 0---------------------");
							borrowbook=2;
							try {
								upbook = (Book)bookbiz.get(id);//현재 빌리려는 책의 정보를 가져온다.
								mv.addObject("bookdetail",upbook);
							} catch (Exception e) {
								e.printStackTrace();
							} 			
					}else if(current_qt!=0 && bid!=id){//대여가 가능할 경우
						//3. 대여가 가능한 경우(대여 중복이 아니면서 대여할 책이 있는 경우)
						//upbooknew에 갯수를 한개를 빼서 업데이트 한다.
						//userbook과 booklog에 등록한다.
							upbooknew = new Book(upbook.getId(),upbook.getName(),
									upbook.getWriter(),upbook.getImg(),upbook.getFloor(),
									upbook.getTotal_qt(),upbook.getCurrent_qt()-1);
							System.out.println("업데이트 한 book : "+upbooknew);	
							bookbiz.modify(upbooknew);
							UserBook book = new UserBook(user.getId(), id); 
							userbookbiz.register(book); //userbook에 등록
							System.out.println("userbook 등록 : "+book);
							
							BookLog logbook = new BookLog(id, user.getId()); //booklog에 등록
							booklogbiz.register(logbook);
							System.out.println("userbook과 booklog에 등록 완료!!");		
							borrowbook=3;
							Book newbook = (Book) bookbiz.get(upbooknew.getId());
							mv.addObject("bookdetail",newbook);
						}
								
				}catch (Exception e) {
					e.printStackTrace();
				}
		}			
		
		mv.addObject("borrowbook",borrowbook);
		mv.addObject("nav", Nav.bookdetail);
		mv.addObject("center", "book/bookdetail.jsp");
		return mv;
	}
	
	
	@RequestMapping("/userbookmodifyimpl.do") //책 연장하기//////////////////////////////////////////
	public ModelAndView userbookmodifyimpl(HttpServletRequest request, String id) throws Exception{
	 ModelAndView mv = new ModelAndView("main");
	 HttpSession session = request.getSession();
	 String uid = session.getAttribute("id").toString(); //회원 아이디 정보 세션에서 가져오기
	 int isqt = 0;
	 UserBook book =  new UserBook(uid, id); 
	 UserBook usersbook = (UserBook) userbookbiz.get(book); //회원이 빌렸던 책의 정보 userbook에서 가져오기

	 ArrayList<Object> userbooklist = new ArrayList<Object>(); 
	 ArrayList<Object> booklist = new ArrayList<Object>();  //다시 usinginfo에 들어갈 정보
	 int renew_qt = usersbook.getRenew_qt(); //연장횟수가 몇번인지 가져오기
	if(renew_qt>=2){ // 연장이 2번 이상이면
		System.out.println("더 연장할 수 없습니다.");
		isqt =1;
		
	}else{ //연장이 2번 미만이면
		userbookbiz.modify(book);//유저의 책 정보 업데이트 -> 연장횟수 증가, 7일 증가... 2번만 되야함...
		
	 //booklog  업데이트
	 BookLog blog = new BookLog(id, uid);
	 ArrayList<Object> booklog = new ArrayList<Object>(); 
	 booklog = sbooklogbiz.getid(blog); //Booklog에서 회원이 빌렸던 책의 정보를 가져온다.
	
	 
	 for (Object obj : booklog) {//새로 연장한 정보를 넣어준다.
		 BookLog logbook = (BookLog)obj;
		 BookLog logbook2 = new BookLog(logbook.getId(), logbook.getB_id(), 
				 logbook.getU_id(), usersbook.getRenew_qt());
		 uprebiz.logupdate(logbook2); //연장 정보 업데이트
		 System.out.println(logbook2);
		 
	}
	 
 	 System.out.println("연장 완료");
 	 isqt =2;
	}
	
	userbooklist = suserbookbiz.getid(uid);// userbook에서 꺼내옴(다시 usinginfo에 들어갈 정보)
	for (Object obj : userbooklist) {
		UserBook userbook = (UserBook) obj;
		String bid = userbook.getB_id();// id 뽑아옴
		Book book1 = (Book)bookbiz.get(bid);// 하나씩 찾음
		
		String[] info = { bid, book1.getName(),
				userbook.getStart_date(), userbook.getEnd_date() };
		// 현재 이용 정보에 필요한 값 String 배열에 넣음
		booklist.add(info);// array에 담음
	}
	 //mv.addObject("booklist",booklist); //바뀐것 다시 넣어줌
	 session.setAttribute("booklist",booklist);
	 mv.addObject("qt",isqt);
	 mv.addObject("center","user/usinginfo.jsp");
	 return mv;
	}
	

	@RequestMapping("/userbookremove.do") //반납하기 : UserBook에 반납 정보 수정,Log에 반납처리///////////////////
	public ModelAndView userbookremove(HttpServletRequest request, String id) throws Exception{
		ModelAndView mv = new ModelAndView("main");
		HttpSession session = request.getSession();
		String uid = session.getAttribute("id").toString(); //회원 아이디 정보 세션에서 가져오기
		//1.회원의 아이디와 책 아이디를 가지고 booklog 테이블에 반납정보를 업데이트 한다.
		
		 BookLog blog = new BookLog(id, uid);
		 ArrayList<Object> booklog = new ArrayList<Object>(); 
		 booklog = sbooklogbiz.getid(blog); //Booklog에서 회원이 빌렸던 책의 정보를 가져온다.
		
		 
		 for (Object obj : booklog) {//새로 연장한 정보를 넣어준다.
			 BookLog logbook = (BookLog)obj;
			 BookLog logbook2 = new BookLog(logbook.getId(),logbook.getB_id(),logbook.getU_id(),logbook.getReal_date());
			 booklogbiz.modify(logbook2); // 반납 정보 보내준다. real_date가 업데이트 됨
			 System.out.println("booklog 업데이트 완료");	 
		}

		int returnqt = 0; // 반납했는지 안했는지 
		
		//2. UserBook의 isreturn을 y로 바꾼다.
		UserBook userbook = new UserBook(uid, id);
		userbook = (UserBook) userbookbiz.get(userbook);
		UserBook modifyuserbook = new UserBook(uid, id, "y");
		upreuserbookbiz.logreturn(modifyuserbook); // y로 수정
	
		returnqt =1;
		
		ArrayList<Object> userbooklist = new ArrayList<Object>(); 
		ArrayList<Object> booklist = new ArrayList<Object>();  //다시 usinginfo에 들어갈 정보
		userbooklist = suserbookbiz.getid(uid);// userbook에서 꺼내옴(다시 usinginfo에 들어갈 정보)
		for (Object obj : userbooklist) {
			UserBook userbook1 = (UserBook) obj;
			String bid = userbook1.getB_id();// id 뽑아옴
			Book book1 = (Book)bookbiz.get(bid);// 하나씩 찾음
			
			String[] info = { bid, book1.getName(),
					userbook1.getStart_date(), userbook1.getEnd_date() };
			// 현재 이용 정보에 필요한 값 String 배열에 넣음
			booklist.add(info);// array에 담음
		}
		
		System.out.println("회원이 반납했습니다. 관리자님 확인해주세요.");
		mv.addObject("returnqt",returnqt);
		session.setAttribute("booklist",booklist);
		mv.addObject("center","user/usinginfo.jsp");
		return mv;
	}
	
	
	@RequestMapping("/bookloglist.do") //자기 대여 내역 보기 (마이페이지 : 도서 이력)////////////////////
	public ModelAndView bookloglist(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("main");
		HttpSession session = request.getSession();
		String uid = session.getAttribute("id").toString(); //회원 아이디 정보 세션에서 가져오기
		ArrayList<Object> result = new ArrayList<Object>();
		result= sbooklogbiz.getname(uid); //uid로 가져온 booklog의 리스트들....
		mv.addObject("booklist",result);
		mv.addObject("center", "book/booklist.jsp");
		return mv; 
	}
	
	@RequestMapping("/userbookremoveconfirm.do") //관리자가 반납한 도서 내역 확인하는 창///////////////////
	public ModelAndView userbookremoveconfirm(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("main");
		HttpSession session = request.getSession();
		String uid = session.getAttribute("id").toString(); //회원 아이디 정보 세션에서 가져오기
		ArrayList<Object> result = new ArrayList<Object>();
		result = suserbookbiz.getname(uid); //반납이 y인 유저들의 정보 
		mv.addObject("userbooklist",result);
		mv.addObject("center", "admin/book/returnbook.jsp");
		return mv;
	}
	
	@RequestMapping("/userbookremoveimpl.do")//관리자가 반납한 도서 내역을 확인했을 때 userbook 지우는 것////
	public ModelAndView userbookremoveimpl(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("main");
		HttpSession session = request.getSession();
		String uid = session.getAttribute("id").toString(); //회원 아이디 정보 세션에서 가져오기
		ArrayList<Object> result = new ArrayList<Object>();
		result = suserbookbiz.getname(uid); //반납이 y인 유저들의 정보 
		for (Object obj : result) {
			UserBook userbook = (UserBook) obj;
			String userid = userbook.getU_id();
			String bookid = userbook.getB_id();
			userbook = new UserBook(userid, bookid); //리스트에서 뽑아온 유저 아이디와 책 아이디를 가져온다.
			userbookbiz.remove(userbook); //이 객체를 userbook리스트에서 지운다.
			System.out.println("반납확인완료");
		}
		mv.addObject("userbooklist",result);
		mv.addObject("center", "admin/book/returnbook.jsp");
		return mv;
	}
	
}





