package app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.json.*;


public class Bike {
	private int bike_id;
	private int station_id;
	private String station_name;
	private int available;
	private Timestamp modified;
	private Timestamp created;
	private String bike_note;
	private int borrow_times;
	/** bh，BikeHelper之物件與Bike相關之資料庫方法（Sigleton） */
	private BikeHelper bh = BikeHelper.getHelper();
	
	/**
     * 實例化（Instantiates）一個新的（new）Bike物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立腳踏車資料時，產生一個新的腳踏車
     */
	public Bike(int station_id, String station_name, int available, String bike_note) {
		this.station_id = station_id;
	    this.station_name = station_name;
	    this.available = available;
	    this.bike_note = bike_note;
	    update();
	}
	/**
     * 實例化（Instantiates）一個新的（new）Bike物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢腳踏車資料時，將每一筆資料新增為一個腳踏車物件
     */
    public Bike(int bike_id, int station_id, String station_name, int available, Timestamp modified, Timestamp created, String bike_note, int borrow_times) {
		this.bike_id = bike_id;
    	this.station_id = station_id;
	    this.station_name = station_name;
	    this.available = available;
	    this.modified = modified;
	    this.created = created;
	    this.bike_note = bike_note;
	    this.borrow_times = borrow_times;
    }
    public int getBike_id() {
        return this.bike_id;
    }
    public int getStation_id() {
        return this.station_id;
    }
    public String getStation_name() {
        return this.station_name;
    }
    public int getAvailable() {
        return this.available;
    }
    public void setAvailable(int available) {
        this.available = available;
    }
    public Timestamp getModified() {
        return this.modified;
    }
    public Timestamp getCreated() {
        return this.created;
    }
    public String getBike_note() {
        return this.bike_note;
    }
    public int getBorrow_times() {
        return this.borrow_times;
    }
    /**
     * 更新腳踏車資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名腳踏車是否已經在資料庫 */
        if(this.bike_id != 0) {
            /** 透過StationHelper物件，更新目前之腳踏車資料置資料庫中 */
            data = bh.update(this);
        }
        return data;
    }
    /**
     * 取得該站點所有資料
     *
     * @return the data 取得該站點之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該站點所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("bike_id", getBike_id());
        jso.put("station_id", getStation_id());
        jso.put("station_name", getStation_name());
        jso.put("available", getAvailable());
        jso.put("modified", getModified());
        jso.put("created", getCreated());
        jso.put("bike_note", getBike_note());
        jso.put("borrow_times", getBorrow_times());
        
        return jso;
    }
}
