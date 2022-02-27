package ncu.im3069.demo.app;

import org.json.*;
import java.sql.*;
import java.util.*;


public class TrackingList {

	private int product_id;
	
	private int tracking_id;
	
	private String members_email;
	
	private TrackingListHelper tlh = TrackingListHelper.getHelper(); 
	
	public TrackingList(int product_id, String members_email) {
		this.product_id = product_id;
		this.members_email = members_email;
	}
	
	public TrackingList(int tracking_id, int product_id, String members_email) {
		this.tracking_id = tracking_id;
		this.product_id = product_id;
		this.members_email = members_email;
	}
	
	public int getProductId() {
		return this.product_id;
	}
	
	public int getTrackingId() {
		return this.tracking_id;
	}
	
	public String getMembersEmail(){
		return this.members_email;
	}
	
	public JSONObject getTrackingListData() {
		JSONObject jso = new JSONObject();
		jso.put("product_id", getProductId());
		jso.put("members_email", getMembersEmail());
		jso.put("tracking_id", getTrackingId());
		return jso;
	}
	
	
}
