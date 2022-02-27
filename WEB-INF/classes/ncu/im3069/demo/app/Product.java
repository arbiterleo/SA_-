package ncu.im3069.demo.app;

import org.json.*;

public class Product {

    /** 商品編號 */
    private int id;

    /** 房屋價格 */
    private double price;

    /** 房屋圖片 */
    private String image;
    
    private String members_email;

    /** 商品名稱 */
	private String productcol;
	
    /** 房屋坪數 */
    private float square;
    
    /** 房屋地址 */
	private String location;
	
	/** 房屋有無電梯 */
	private Boolean elevator;
	
	/** 房屋格局 */
	private String layout;
	
	/** 房屋詳述 */
	private String detail;
	
	/** 房屋年齡 */
	private float age;
	
	/** 房屋有無鬧鬼 */
	private boolean haunted;
	
    /** 房屋追蹤數 */
    private int tl_times;

    /** 房屋預約數 */
    private int rl_times;
    
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public Product(int id) {
		this.id = id;
	}
	
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param id 產品編號
     * @param productcol 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param detail 產品敘述
     */	
	public Product(int id, String productcol, double price, String picture, String detail) {
		this.id = id;
		this.productcol = productcol;
		this.price =price;
		this.detail = detail;
		this.image = picture;
	}
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param id 產品編號
     * @param productcol 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param square 產品坪數
     * @param location 產品地址
     * @param layout 產品格局
     * @param age 產品年齡
     * @param haunted 產品有無鬼
     * @param elevator 產品有無電梯
     * @param detail 產品敘述
     */	
	public Product(int id, String productcol, int price, float square, String location, Boolean elevator, String layout, String detail, float age, boolean haunted) {
		  this.id = id;
		  this.productcol = productcol;
		  this.price =price;
		  this.square = square;
		  this.location = location;
		  this.elevator = elevator;
		  this.layout = layout;
		  this.detail = detail;
		  this.age = age;
		  this.haunted = haunted;  
	}
	
	public Product(int id, String productcol,int price, String picture, String members_email,int reservaed_times,float square,String location,boolean elevator,String layout,String detail,float age,boolean haunted,int tracked_times ) {
		this.id = id;
		this.productcol = productcol;
		this.price = price;
		this.image = picture;
		this.members_email = members_email;
		this.rl_times=reservaed_times;
		this.square=square;
		this.location=location;
		this.elevator=elevator;
		this.layout=layout;
		this.detail=detail;
		this.age=age;
		this.haunted=haunted;
		this.tl_times=tracked_times;
	}
	
	//uploadProductController要用的建構子
    public Product(String members_email, String productcol, String layout, String location, String detail, float square, float age, boolean haunted, boolean elevator, double price)
    {
    	this.productcol = productcol;
		this.price = price;
		this.members_email = members_email;
		this.square=square;
		this.location=location;
		this.elevator=elevator;
		this.layout=layout;
		this.detail=detail;
		this.age=age;
		this.haunted=haunted;
		this.price = price;
    }
	/**
     * 取得房屋編號
     *
     * @return int 回傳房屋編號
     */
	public int getID() {
		return this.id;
	}

    /**
     * 取得房屋名稱
     *
     * @return String 回傳房屋名稱
     */
	public String getProductcol() {
		return this.productcol;
	}

    /**
     * 取得房屋圖片
     *
     * @return String 回傳房屋圖片
     */
	public String getImage() {
		return this.image;
	}
	
    /**
     * 取得房屋價錢
     *
     * @return double 回傳房屋價錢
     */
	public double getPrice() {
		return this.price;
	}

	/**
     * 取得房屋預約數
     *
     * @return String 回傳房屋預約數
     */
	public int getReservaed_times() {
		return this.rl_times;
	}

	 /**
	  * 取得房屋坪數
	  *
	  * @return String 回傳房屋坪數
	  */	
	public float getSquare() {
		return this.square;
	}

	 /**
	  * 取得房屋地址
	  *
	  * @return String 回傳房屋地址
	  */	
	public String getLocation() {
		return this.location;
	}
	
	 /**
	  * 取得房屋有無電梯
	  *
	  * @return boolean 回傳有無電梯
	  */	
	public boolean getElevator() {
		return this.elevator;
	}
	
	 /**
	  * 取得房屋格局
	  *
	  * @return String 回傳房屋格局
	  */	
	public String getLayout() {
		return this.layout;
	}
	
	 /**
	  * 取得房屋詳述
	  *
	  * @return String 回傳房屋詳述
	  */	
	public String getDetail() {
		return this.detail;
	}
	
	public String getEmail()
	{
		return members_email;
	}
	
	 /**
	  * 取得房屋年齡
	  *
	  * @return float 回傳房屋年齡
	  */	
	public float getAge() {
		return this.age;
	}
	
	 /**
	  * 取得房屋有無鬼
	  *
	  * @return boolean 回傳房屋有無鬼
	  */	
	public boolean getHaunted() {
		return this.haunted;
	}
	
	 /**
	  * 取得房屋預約數
	  *
	  * @return int 回傳房屋預約數
	  */	
	public int getTracked_times() {
		return this.tl_times;
	}

    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("product_id", getID());
        jso.put("productcol", getProductcol());
        jso.put("price", getPrice());
        jso.put("picture", getImage());
        jso.put("members_email", getEmail());
        jso.put("reservaed_times", getReservaed_times());
        jso.put("square", getSquare());
        jso.put("location", getLocation());
        jso.put("elevator", getElevator());
        jso.put("layout", getLayout());
        jso.put("detail", getDetail());
        jso.put("age", getAge());
        jso.put("haunted", getHaunted());
        jso.put("tracked_times", getTracked_times());

        return jso;
    }
}
