package ncu.im3069.demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.TrackingList;
import ncu.im3069.demo.app.TrackingListHelper;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/tracking_list.do")
public class TrackingListController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Singleton） */
    private ProductHelper ph =  ProductHelper.getHelper();
    
	/** mh，MemberHelper 之物件與 Member 相關之資料庫方法（Singleton） */
    private MemberHelper mh =  MemberHelper.getHelper();
    
    /** tlh，TrackingListHelper之物件與TrackingList相關之資料庫方法（Singleton） */
    private TrackingListHelper tlh =  TrackingListHelper.getHelper();	
    
    
    public TrackingListController() {
        super();
    }

    
    /**
     * 處理 Http Method 請求 POST 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        
        int tracking_id = jso.getInt("tracking_id");
        int product_id = jso.getInt("product_id");
        String email = jso.getString("members_email");

        /** 建立一個新的物件 */
        TrackingList tl = new TrackingList(tracking_id,product_id,email);
        
        JSONObject data = tlh.create(tl);

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 註冊會員資料...");
        resp.put("response", data);

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

    //OK!
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
    	
    		/** 新建一個 JSONObject 物件之 tl_product 變數，用於紀錄所有查詢回的追蹤商品資料 */
    		JSONObject tl_product = null;
        	
            /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
            JsonReader jsr = new JsonReader(request);         
            
            /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
            String id = jsr.getParameter("id");
            
            /** 透過前端拿到的id去尋找email */
            int id_int =  Integer.valueOf(id);
            JSONObject jso = mh.getEmail(id_int);
	        String email = jso.getString("email");
            	
	        
	        
            /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
            if (!email.isEmpty()) {
            	
            	/** 用於儲存所有之追蹤商品，以JSONArray方式儲存 */
                JSONArray jsa = new JSONArray();
            	
            	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
            	
                /** 透過MemberHelper物件之getProductID()方法取回所有追蹤商品之編號，回傳之資料為JSONObject物件 */
                JSONObject query = tlh.getTrackProduct(email);
                
                /** 獲得商品之編號後再取出有該追蹤清單的所有商品編號，回傳之資料為JSONArray物件 */
                JSONArray pid_array = query.getJSONArray("data");
                
                /** 將JSONArray的商品id依序用ProductHelper物件的getById()方法找出商品詳細資料 */
                tl_product =ph.getById(pid_array);
                
                
                resp.put("status", "200");
                resp.put("message", "追蹤商品取得成功");
                resp.put("response", tl_product);
                
                System.out.println("\nController:");
                System.out.println(resp);
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
                
            }
            else {
            	
            	//int id_int =  Integer.valueOf(id);
            	String email_String = String.valueOf(email);
            	
                /** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
                JSONObject query = tlh.getByEmail(email_String);
                
                /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "追蹤商品資料取得成功");
                resp.put("response", query);
                System.out.println("\nController:");
                System.out.println(resp);
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
            }
        }
    
    //OK!
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
            int tracking_id = jso.getInt("tracking_id");
            
            /** 透過MemberHelper物件的deleteByTrackingId()方法至資料庫刪除該追蹤商品，回傳之資料為JSONObject物件 */
            JSONObject query = tlh.deleteByTrackingId(tracking_id);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "追蹤商品移除成功！");
            resp.put("response", query);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
    
}
