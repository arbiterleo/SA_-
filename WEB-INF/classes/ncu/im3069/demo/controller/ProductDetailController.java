package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.demo.app.ReservationHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class ProductDetailController
 */
@WebServlet("/api/ProductDetail.do")
public class ProductDetailController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	//需要用到的helper
	ProductHelper ph = ProductHelper.getHelper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductDetailController() {
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
        throws ServletException, IOException {
    	
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("product_id");
        
        /** 判斷該字串是否存在，若存在傳值成功，否則代表傳值失敗，因為會員必須登入才能進入此頁面 */
        if (id.isEmpty()) 
        {
            JSONObject resp = new JSONObject();
            
            //回復訊息告知錯誤發生
            resp.put("status", "400");
            resp.put("message", "錯誤，product_id未傳入");
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
            
        }
        else {
        	
        	int id_int =  Integer.valueOf(id);
        	
            /** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
            JSONObject data = ph.getById(id_int).getData();
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "商品資料取得成功");
            resp.put("data", data);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
