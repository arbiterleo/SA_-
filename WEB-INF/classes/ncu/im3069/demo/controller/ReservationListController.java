package ncu.im3069.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.demo.app.ReservationHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class ReservationListController
 */
//@WebServlet("/api/ReservationListController.do")
public class ReservationListController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
    
	//需要用到的helper
	MemberHelper mh = MemberHelper.getHelper();
	ReservationHelper rh = ReservationHelper.getHelper();
	ProductHelper ph = ProductHelper.getHelper();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservationListController() {
        super();
        // TODO Auto-generated constructor stub
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
        throws ServletException, IOException 
    {
    	
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 用於儲存所有檢索回之清單，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String members_id = jsr.getParameter("members_id");
        
        /** 紀錄product總個數 */
        int count = 0;
        
        /** 判斷字串是否存在，若存在傳值成功，否則代表傳值失敗，因為會員必須登入才能進入此頁面 */
        if (members_id.isEmpty()) 
        {
            JSONObject resp = new JSONObject();
            
            //回復訊息告知錯誤發生
            resp.put("status", "400");
            resp.put("message", "錯誤，members_id未傳入");
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
            
        }
        else {
        	
        	int id_int =  Integer.valueOf(members_id);
        	
            /** 透過ReservationHelper物件的getProductbyMemberID方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
            ArrayList<Product> pl = rh.getProductbyMemberID(id_int);
            
            if(pl.isEmpty())
            {
            	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                
            	//回復訊息告知錯誤發生
                resp.put("status", "400");
                resp.put("message", "錯誤，會員預約清單為空!");
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
            }
            else
            {
            	//遍歷取回的product清單並封裝進JSONArray中
            	pl.forEach((p) -> jsa.put(p.getData()));
            	
            	//取得清單內的product數量
            	count = pl.size();
                
                /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "會員資料取得成功");
                resp.put("count", count);
                resp.put("data", jsa);
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
            }   
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
