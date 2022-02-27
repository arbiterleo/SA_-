package ncu.im3069.demo.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.ReservationHelper;
import ncu.im3069.demo.app.ReservationList;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class ReservationInput
 */
@WebServlet("/api/ReservationInputController.do")
public class ReservationInputController extends HttpServlet 
{
	
	private static final long serialVersionUID = 1L;
	
	/** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
	private MemberHelper mh = MemberHelper.getHelper();
	
	/** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
	private ReservationHelper rh = ReservationHelper.getHelper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservationInputController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 處理Http Method請求POST方法（新增資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException 
    {
            /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
            JsonReader jsr = new JsonReader(request);
            JSONObject jso = jsr.getObject();
            
            int members_id = 0, product_id = 0;
            
            /** 取出經解析到JSONObject之Request參數 */
            members_id = jso.getInt("members_id");
            String date = jso.getString("reservation_date");
            product_id = jso.getInt("product_id");
            
            if(members_id == 0 || product_id == 0 || date.isEmpty())
            {
            	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "400");
	            resp.put("message", "失敗! members_id or product_id or date為空!");
	            resp.put("response", "error5487");
	            
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
            }
            else if(members_id != 0 || product_id != 0 && !date.isEmpty())
            {
            	Date reservation_date = Date.valueOf(date);
            	
            	//使用members_id提取members_email
            	String members_email = mh.getEmailByID(members_id);
            	
            	/** 建立一個新的清單物件 */
            	ReservationList rl = new ReservationList(members_email, reservation_date, product_id);
            	
            	/** 透過MemberHelper物件的checkDuplicate()檢查該清單電子郵件信箱與產品id是否有重複 */
    	        if (!rh.checkDuplicate(rl))
    	        {
    	        	/** 透過ReservationHelper物件的create()方法新建一個清單至資料庫 */
    	            JSONObject data = rh.create(rl);
    	            
    	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
    	            JSONObject resp = new JSONObject();
    	            resp.put("status", "200");
    	            resp.put("message", "新增清單成功!");
    	            resp.put("response", data);
    	            
    	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
    	            jsr.response(resp, response);
    	        }
    	        else
    	        {
    	        	if(rh.update(rl))
    	        	{
    	        		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        	            JSONObject resp = new JSONObject();
        	            resp.put("status", "200");
        	            resp.put("message", "修改清單成功!");
        	            resp.put("response", "success5487");
        	            
        	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        	            jsr.response(resp, response);
    	        	}
    	        	else
    	        	{
    	        		/** 以字串組出JSON格式之資料 */
        	            String resp = "{\"status\": \'400\', \"message\": \'新增或修改錯誤，可能存在重複資料！\', \'response\': \'\'}";
        	            /** 透過JsonReader物件回傳到前端（以字串方式） */
        	            jsr.response(resp, response);
    	        	} 	
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
        
        /** 判斷該字串是否存在，若存在傳值成功，否則代表傳值失敗，因為會員必須登入才能進入此頁面 */
        if (id.isEmpty()) 
        {
            JSONObject resp = new JSONObject();
            
            //回復訊息告知錯誤發生
            resp.put("status", "400");
            resp.put("message", "錯誤，會員未登入或members_id未傳入");
    
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	/*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}*/

}
