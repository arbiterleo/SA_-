package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.demo.app.TrackingList;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class UploadProductController
 */
@WebServlet("/api/UploadProductController.do")
public class UploadProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//要用到的helper
	private ProductHelper ph = ProductHelper.getHelper();
	private MemberHelper mh = MemberHelper.getHelper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadProductController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
        
        /** 取出經解析到JSONObject之Request參數 */
        int members_id = jso.getInt("members_id");
        String productcol = jso.getString("productcol");
        String layout = jso.getString("layout");
        String location = jso.getString("location");
        String detail = jso.getString("detail");
        float square = jso.getFloat("square");
        float age = jso.getFloat("age");
        boolean haunted = jso.getBoolean("haunted");
        boolean elevator = jso.getBoolean("elevator");
        double price = jso.getDouble("price");
        
        /** 確保一些必需的資料有傳進來 */
        if (members_id == 0 || productcol.isEmpty() || location.isEmpty()) 
        {
            JSONObject resp = new JSONObject();
            
            //回復訊息告知錯誤發生
            resp.put("status", "400");
            resp.put("message", "錯誤，productcol或members_id或location未傳入");
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
            
        }
        else {
        	
        	/** 建立一個新的product物件 */
            Product p = new Product(mh.getEmailByID(members_id), productcol, layout, location, detail, 
            		square, age, haunted, elevator, price);
            
            JSONObject data = ph.create(p);

            /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 加入產品資料庫...");
            resp.put("response", data);

            /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
            jsr.response(resp, response);
        }
	}

}
