package ncu.im3069.demo.app;

import java.sql.Date;
import java.util.Collection;

import org.json.JSONObject;

public class ReservationList {
	
	private int reservation_id;
	private String members_email;
	private Date reservation_date;
	private int product_id;

	
	/**
     * 實例化（Instantiates）一個新的（new）ReservationList物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立清單資料時，產生一張新的清單
     *
     * @param product_id 產品編號
     * @param members_email 會員信箱
     * @param sch_date 預約日期
     */
	//沒有提供清單編號，代表剛剛才創建
	public ReservationList(String members_email, Date reservation_date, int product_id) 
	{
		// TODO Auto-generated constructor stub
		this.product_id = product_id;
		this.members_email = members_email;
		this.reservation_date = reservation_date;
	}
	
	/**
     * 實例化（Instantiates）一個新的（new）ReservationList物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立清單資料時，產生一張新的清單
     *
     * @param reservation_id 清單編號
     * @param product_id 產品編號
     * @param members_email 會員信箱
     * @param sch_date 預約日期
     */
	public ReservationList(int reservation_id, String members_email, Date reservation_date, int product_id) 
	{
		// TODO Auto-generated constructor stub
		this.reservation_id = reservation_id;
		this.product_id = product_id;
		this.members_email = members_email;
		this.reservation_date = reservation_date;
	}
	
	public int getResID()
	{
		return reservation_id;
	}
	
	public String getMembersEmail()
	{
		return members_email;
	}
	
	public Date getResDate()
	{
		return reservation_date;
	}
	
	public int getProID()
	{
		return product_id;
	}

	/**
     * 取得該洽詢清單所有資料
     *
     * @return the data 取得該洽詢清單之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() 
    {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("reservation_id", getResID());
        jso.put("members_email", getMembersEmail());
        jso.put("reservation_date", getResDate());
        jso.put("product_id", getProID());
        
        return jso;
    }
    
    public void update(Date date)
    {
    	this.reservation_date = date;
    }
    
    

}
