package ncu.im3069.demo.app;

import java.sql.*;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;
import ncu.im3069.demo.app.Product;

public class ProductHelper {
    private ProductHelper() {
        
    }
    
    private static ProductHelper ph;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    public static ProductHelper getHelper() {
        /** Singleton檢查是否已經有ProductHelper物件，若無則new一個，若有則直接回傳 */
        if(ph == null) ph = new ProductHelper();
        
        return ph;
    }
    
    /**
     * 建立該product至資料庫
     *
     * @param p product物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(Product p) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`product`(`productcol`, `price`, `members_email`, "
            		+ "`square`, `location`, `elevator`, `layout`, `detail`, `age`, `haunted`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
 
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, p.getProductcol());
            pres.setDouble(2, p.getPrice());
            pres.setString(3, p.getEmail());
            pres.setFloat(4, p.getSquare());
            pres.setString(5, p.getLocation());
            pres.setBoolean(6, p.getElevator());
            pres.setString(7, p.getLayout());
            pres.setString(8, p.getDetail());
            pres.setFloat(9, p.getAge());
            pres.setBoolean(10, p.getHaunted());
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("time", duration);
        response.put("row", row);

        return response;
    }
    
    public JSONObject getAll() {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Product p = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`product`";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int id = rs.getInt("product_id");
                String productcol = rs.getString("productcol");
                int price = rs.getInt("price");
                float square = rs.getFloat("square");
                String location = rs.getString("location");
                boolean elevator =rs.getBoolean("elevator");
                String layout = rs.getString("layout");
                String detail =rs.getString("detail");
                float age = rs.getInt("age");
                boolean haunted = rs.getBoolean("haunted");
              
                
                /** 將每一筆會員資料產生一名新TrackingList物件 */
                p = new Product(id,productcol,price,square,location,elevator,layout,detail,age,haunted);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(p.getData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject getByIdList(String data) {
      /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
      Product p = null;
      /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
      JSONArray jsa = new JSONArray();
      /** 記錄實際執行之SQL指令 */
      String exexcute_sql = "";
      /** 紀錄程式開始執行時間 */
      long start_time = System.nanoTime();
      /** 紀錄SQL總行數 */
      int row = 0;
      /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
      ResultSet rs = null;

      try {
          /** 取得資料庫之連線 */
          conn = DBMgr.getConnection();
          String[] in_para = DBMgr.stringToArray(data, ",");
          /** SQL指令 */
          String sql = "SELECT * FROM `missa`.`product` WHERE `product_id`";
          for (int i=0 ; i < in_para.length ; i++) {
              sql += (i == 0) ? "in (?" : ", ?";
              sql += (i == in_para.length-1) ? ")" : "";
          }
          
          /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
          pres = conn.prepareStatement(sql);
          for (int i=0 ; i < in_para.length ; i++) {
            pres.setString(i+1, in_para[i]);
          }
          /** 執行查詢之SQL指令並記錄其回傳之資料 */
          rs = pres.executeQuery();

          /** 紀錄真實執行的SQL指令，並印出 **/
          exexcute_sql = pres.toString();
          System.out.println(exexcute_sql);
          
          /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
          while(rs.next()) {
              /** 每執行一次迴圈表示有一筆資料 */
              row += 1;
              
              /** 將 ResultSet 之資料取出 */
              int id = rs.getInt("product_id");
              String productcol = rs.getString("productcol");
              int price = rs.getInt("price");
              String detail =rs.getString("detail");
              String picture = rs.getString("picture");
              
              /** 將每一筆商品資料產生一名新Product物件 */
              p = new Product(id, productcol, price, picture, detail);
              /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
              jsa.put(p.getData());
          }

      } catch (SQLException e) {
          /** 印出JDBC SQL指令錯誤 **/
          System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
      } catch (Exception e) {
          /** 若錯誤則印出錯誤訊息 */
          e.printStackTrace();
      } finally {
          /** 關閉連線並釋放所有資料庫相關之資源 **/
          DBMgr.close(rs, pres, conn);
      }
      
      /** 紀錄程式結束執行時間 */
      long end_time = System.nanoTime();
      /** 紀錄程式執行時間 */
      long duration = (end_time - start_time);
      
      /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
      JSONObject response = new JSONObject();
      response.put("sql", exexcute_sql);
      response.put("row", row);
      response.put("time", duration);
      response.put("data", jsa);

      return response;
    }
    
    public JSONObject getById(JSONArray array) {
    	  
    	/** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
        Product p = null;
        /** 用於儲存所有檢索回之追蹤商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        
        
        try {
        	/** 逐一的將所有在追蹤清單的商品都存入JSONArray中傳回Controller */
        	for(int i=0; i<array.length(); i++) {
        		/** 取得追蹤的其中之一商品編號 */
	        	int p_id = array.getInt(i);
	        	
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "SELECT * FROM `missa`.`product` WHERE product_id = ?";
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql);
	            pres.setInt(1, p_id);
	            
	            System.out.print(sql);
	            
	            
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();
	
	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            
	            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
	            /** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
	            while(rs.next()) {
	                /** 每執行一次迴圈表示有一筆資料 */
	                row += 1;
	                
	                /** 將 ResultSet 之資料取出 */
	                int id = rs.getInt("product_id");
	                String productcol = rs.getString("productcol");
	                int price = rs.getInt("price");
	                float square = rs.getFloat("square");
	                String location = rs.getString("location");
	                boolean elevator =rs.getBoolean("elevator");
	                String layout = rs.getString("layout");
	                String detail =rs.getString("detail");
	                float age = rs.getInt("age");
	                boolean haunted = rs.getBoolean("haunted");
	              
	                
	                /** 將每一筆商品資料產生一名新Product物件 */
	                p = new Product(id,productcol,price,square,location,elevator,layout,detail,age,haunted);
	                /** 取出該名商品之資料並封裝至 JSONsonArray 內 */
	                jsa.put(p.getData());
                
            	}
            }
            
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public Product getById(int id) {
        
        Product p = null;
        
        String exexcute_sql = "";
        
        ResultSet rs = null;
        
        try {
            
            conn = DBMgr.getConnection();
           
            String sql = "SELECT * FROM `missa`.`product` WHERE `product_id` = ? LIMIT 1";
            
            
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            
            rs = pres.executeQuery();

            
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
            	 int product_id = rs.getInt("product_id");
                 String productcol = rs.getString("productcol");
                 String members_email = rs.getString("members_email");
                 int price = rs.getInt("price");
                 String picture = rs.getString("picture");
                 int reservaed_times = rs.getInt("reservaed_times");
                 float square = rs.getFloat("square");
                 String location = rs.getString("location");
                 boolean elevator = rs.getBoolean("elevator");
                 String layout = rs.getString("layout");
                 String detail = rs.getString("detail");
                 float age = rs.getFloat("age");
                 boolean haunted = rs.getBoolean("haunted");
                 int tracked_times = rs.getInt("tracked_times");
                 
               
                
                p = new Product(product_id,productcol, price, picture, members_email, reservaed_times,square,location,elevator,layout,detail,age,haunted,tracked_times);
            }

        } catch (SQLException e) {
            
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {
            
            DBMgr.close(rs, pres, conn);
        }

        return p;
    }
}    