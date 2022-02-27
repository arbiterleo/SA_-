package ncu.im3069.demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.tools.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class MemberController<br>
 * MemberController類別（class）主要用於處理Member相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class MemberController extends HttpServlet {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private MemberHelper mh =  MemberHelper.getHelper();
    
    /**
     * 處理Http Method請求POST方法（新增資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        String email = jso.getString("members_email");
        String password = jso.getString("password");
        String action = jso.getString("action");
        
        /** 登入事件產生後可取得member資料 */
        JSONObject query = mh.getByEmail(email); 

        /** 註冊事件 */
        if(action.equals("registe")) {
        	
        	String name = jso.getString("members_name");
            String phone = jso.getString("members_phone");
            int status = jso.getInt("members_status");
            
            /** 建立一個新的會員物件 */
            Member m = new Member(email, password, name, phone, status);
            
            /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
	        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
	            /** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }
	        /** 透過MemberHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
	        else if (!mh.checkDuplicate(m)) {
	            /** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
	            JSONObject data = mh.create(m);
	            
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "成功! 註冊會員資料...");
	            resp.put("response", data);
	            
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        }
	        else {
	            /** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此信箱帳號重複！\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }
        }
        
        /** 登入事件 */
        else if(action.equals("login")){
        	
            Member m = new Member(email, password);
            
            
        	/** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
	        if(email.isEmpty() || password.isEmpty() ) {
	            /** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'欄位不能為空\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }
	        
	        /** 透過MemberHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否存在 */
	        else if (mh.checkDuplicate(m)) {
	        	
	        	if(mh.checkPassword(m)) {
		            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
		            JSONObject resp = new JSONObject();
		            
		            resp.put("status", "200");
		            resp.put("message", "成功登入!!!!");
		            
		            /** 從MemberHelper實現的JSON物件中撈出id */
		            resp.put("response", query.get("id"));
		            
		            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		            jsr.response(resp, response);
		           
	        	}
	        	else {
		            /** 以字串組出JSON格式之資料 */
		            String resp = "{\"status\": \'400\', \"message\": \'登入失敗，密碼錯誤！\', \'response\': \'\'}";
		            /** 透過JsonReader物件回傳到前端（以字串方式） */
		            jsr.response(resp, response);	            
	        	}     
	        }  
	        else {
	            /** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'登入失敗，找不到此帳號！\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }      
        }
        
    }
    
    
    

    /**
     * 處理Http Method請求GET方法（取得資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
 
        
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("members_id");
        
        /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
        if (id.isEmpty()) {
        	
            /** 透過MemberHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
            JSONObject query = mh.getAll();
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有會員資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
            
        }
        else {
        	
        	int id_int =  Integer.valueOf(id);
        	
            /** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
            JSONObject query = mh.getByID(id_int);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "會員資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
    }

    /**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("members_id");
        
        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
        JSONObject query = mh.deleteByID(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "會員移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

    /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("members_id");
        String action = jso.getString("action");

        
        if(action.equals("editdata")) {
        	
        	String email = jso.getString("members_email");
            String name = jso.getString("members_name");
            String phone = jso.getString("members_phone");
        	
        	/** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
	        Member m = new Member(id, email, phone, name);
	        JSONObject query1 = mh.getEmail(m);
	        JSONObject query2 = mh.getPhone(m);
	        
	        /** 獲得member物件自己本身的phone及email，判斷與新的參數是否一樣 */
	        String email_own = (String) query1.get("email");
	        String phone_own = (String) query2.get("phone");
	        
	        /** 透過MemberHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
	        if (!mh.checkDuplicate(m) || email.equals(email_own)) {
	            /** 透過MemberHelper物件的update()方法更新會員至資料庫 */
	            JSONObject data = mh.update(m);
	            
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "成功! 更改會員資料!");
	            resp.put("response", data);
	            
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        }
	        else if (mh.checkPhone(m) && !email.equals(phone_own)) {
	        	/** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'失敗，該號碼已被使用！\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }
	        else {
	            /** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'失敗，該信箱已被使用！\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }
        	
        }
        else if(action.equals("editpassword")) {
        	
        	String pwd = jso.getString("password");
            String n_pwd = jso.getString("new_password");
        	
        	/** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
	        Member m = new Member(id, n_pwd);
	        /** 透過傳入之物件m，取得含有member信箱的JSON */
	        JSONObject query = mh.getEmail(m);
	        String email = (String) query.get("email");
	        
	        /** 再次新建一個以這些參數並加上信箱之會員Member物件 */
	        Member m2 = new Member(id, pwd, email);
	       
	        /** 透過MemberHelper物件的checkPasswor()檢查該會員密碼是否正確 */
	        if (mh.checkPassword(m2)) {
	            /** 透過MemberHelper物件的updatePassword()方法更新會員密碼 */
	            JSONObject data = mh.updatePassword(m);
	            
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "更改會員密碼!");
	            
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        }
	        else {
	            /** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'失敗，原密碼不是正確的！\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }
	        
        	
        }
        
    }
}