package ncu.im3069.demo.app;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.*;
import org.json.JSONObject;

import ncu.im3069.demo.util.DBMgr;

public class ReservationHelper 
{
	/**
     * 實例化（Instantiates）一個新的（new）ReservationHelper物件<br>
     * 採用Singleton不需要透過new
     */
    private ReservationHelper() 
    {
        
    }
    
    /** 靜態變數，儲存ReservationHelper物件 */
    private static ReservationHelper rh;
    
    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;
    
    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;
    
    /** 要用到的helper */
    private ProductHelper ph = ProductHelper.getHelper();
    private MemberHelper mh = MemberHelper.getHelper();
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個ReservationrHelper物件
     *
     * @return the helper 回傳ReservationHelper物件
     */
    public static ReservationHelper getHelper() 
    {
        /** Singleton檢查是否已經有ReservationHelper物件，若無則new一個，若有則直接回傳 */
        if(rh == null) rh = new ReservationHelper();
        
        return rh;
    }
    
    /**
     * 建立該清單至資料庫
     *
     * @param rl 一張清單之ReservationList物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(ReservationList rl) {
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
            String sql = "INSERT INTO `missa`.`reservation_list`(`members_email`, `reservation_date`, `product_id`)"
                    + " VALUES(?, ?, ?)";
            
            /** 取得所需之參數 */
            String email = rl.getMembersEmail();
            Date date = rl.getResDate();
            int proID = rl.getProID();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            pres.setDate(2, date);
            pres.setInt(3, proID);
            
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
    
    /**
     * 取回所有清單資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public ArrayList<Product> getProductbyMemberID(int members_id) 
    {
    	/** 新建一個 Product 物件之 product 變數，用於紀錄每一查詢回之product資料 */
        Product product = null;
        /** 用於儲存所有檢索回之product，以arraylist方式儲存 */
        ArrayList<Product> ProductList = new ArrayList<Product>();
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
            String sql = "SELECT * FROM `missa`.`reservation_list` WHERE `members_email` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, mh.getEmailByID(members_id));
            
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                product = ph.getById(rs.getInt("product_id"));
                
                /** 將 ResultSet 之資料取出 */
                /*int product_id = rs.getInt("product_id");
                String productcol = rs.getString("productcol");
                int price = rs.getInt("price");
                String picture = rs.getString("picture");
                String members_email = rs.getString("members_email");
            	int reservaed_times = rs.getInt("reservaed_times");	
            	float square = rs.getFloat("square");	
            	String location = rs.getString("location");	
            	boolean elevator = rs.getBoolean("elevator");	
            	String layout = rs.getString("layout");	
            	String detail = rs.getString("detail");	
            	float age = rs.getFloat("age");	
            	boolean haunted = rs.getBoolean("haunted");	
            	int tracked_times = rs.getInt("tracked_times");
                
                /** 將每一筆資料產生一新Product物件 */
                /*product = new Product(product_id, productcol, price, picture, members_email, 
                		reservaed_times, square, location, elevator, layout, detail, age, 
                		haunted, tracked_times);
                /** 將該Product加入進 ArrayList 內 */
                ProductList.add(product);
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
        
        /** 將花費時間、影響行數印出 */
        System.out.println("affected row : " + row);
        System.out.println("used time : " + duration);
        
        //回傳product清單
        return ProductList;
    }
    
    /**
     * 取回所有清單資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public JSONObject getAll() {
        /** 新建一張 ResList 物件之 rl 變數，用於紀錄每一位查詢回之會員資料 */
        ReservationList rl = null;
        /** 用於儲存所有檢索回之清單，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`reservation_list`";
            
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
                int reservation_id = rs.getInt("reservation_id");
                String members_email = rs.getString("members_email");
                Date reservation_date = rs.getDate("reservation_date");
                int product_id = rs.getInt("product_id");
                
                /** 將每一筆清單資料產生一張新ResList物件 */
                rl = new ReservationList(reservation_id, members_email, reservation_date, product_id);
                /** 取出該張清單之資料並封裝至 JSONsonArray 內 */
                jsa.put(rl.getData());
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
    
    /**
     * 透過預約清單編號（ID）刪除預約清單
     *
     * @param id 預約清單編號
     * @return the JSONObject 回傳SQL執行結果
     */
    public JSONObject deleteByID(int id) 
    {
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
            String sql = "DELETE FROM `missa`.`reservation_list` WHERE `reservation_id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行刪除之SQL指令並記錄影響之行數 */
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
            DBMgr.close(rs, pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);

        return response;
    }
    
    /**
     * 透過會員信箱（ID）刪除預約清單
     *
     * @param mail 會員信箱
     * @return the JSONObject 回傳SQL執行結果
     */
    public JSONObject deleteByMail(String mail) {
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
            String sql = "DELETE FROM `missa`.`reservation_list` WHERE `members_email` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, mail);
            /** 執行刪除之SQL指令並記錄影響之行數 */
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
            DBMgr.close(rs, pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);

        return response;
    }
    
    public boolean checkDuplicate(ReservationList rl)
    {
    	/** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
    	/** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = -1;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        /** 儲存用來修改日期的資料 */
        int res_id = 0;
        Date res_date = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`reservation_list` "
            		+ "WHERE `members_email` = ? AND `product_id` = ?";
            
            /** 取得所需之參數 */
            String email = rl.getMembersEmail();
            int product_id = rl.getProID();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, email);
            pres.setInt(2, product_id);
            
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            rs.last();
            row = rs.getRow();
            
            //取得用來update的資料
            res_id = rs.getInt("reservation_id");
            res_date = rs.getDate("reservation_date");
            
            System.out.println(row);
            System.out.println(rs.toString());

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
        
        /** 
         * 判斷是否已經有這張清單在資料庫中
         * 若無一筆則回傳False，否則回傳true
         */
        if(row <= 0 )
        {
        	return false;
        }
        else
        {
        	return true;	
        }
        
    }
    
    public boolean update(ReservationList rl)
    {
    	/** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null; 
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`reservation_list` "
            		+ "SET `reservation_date` = ? "
            		+ "WHERE `members_email` = ? AND `product_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setDate(1, rl.getResDate());
            pres.setString(2, rl.getMembersEmail());
            pres.setInt(3, rl.getProID());
            
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();
            System.out.println("row : " + row);

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
            DBMgr.close(rs, pres, conn);
        }
        
        /** 
         * 判斷是否更新成功 
         */
        if(row > 0 )
        {
        	return true;
        }
        else
        {
        	return false;	
        }	
    }
}
