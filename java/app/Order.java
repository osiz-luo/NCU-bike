package app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.json.*;


public class Order {
	private int order_id;
	private int student_id;
	private int bike_id;
	private int station1_id;
	private String station1_name;
	private int station2_id;
	private String station2_name;
	private Timestamp start_time;
	private Timestamp end_time;
	private int cost;
	private int point_used;
	private int total_amount;
	private String report_content;
	/** oh，OrderHelper之物件與Order相關之資料庫方法（Sigleton） */
	private OrderHelper oh =  OrderHelper.getHelper();

	/**
     * 實例化（Instantiates）一個新的（new）Order物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立借還資料時，產生一個新的借還表，附加report
     */
	public Order(int student_id, int bike_id, int station1_id, int station2_id, Timestamp start_time, Timestamp end_time, int cost, int point_used, int total_amount, String report_content) {
		this.student_id = student_id;
	    this.bike_id = bike_id;
	    this.station1_id = station1_id;
	    this.station2_id = station2_id;
	    this.start_time = start_time;
	    this.end_time = end_time;
	    this.cost = cost;
	    this.point_used = point_used;
	    this.total_amount = total_amount;
	    this.report_content = report_content;
	    update();
	}

    public Order(int order_id, int student_id, int bike_id, int station1_id, int station2_id, Timestamp start_time, Timestamp end_time,
			int cost, int point_used, int total_amount, String report_content) {
	    this.order_id = order_id;
		this.student_id = student_id;
	    this.bike_id = bike_id;
	    this.station1_id = station1_id;
	    this.station2_id = station2_id;
	    this.start_time = start_time;
	    this.end_time = end_time;
	    this.cost = cost;
	    this.point_used = point_used;
	    this.total_amount=total_amount;
	    this.report_content = report_content;
	}
    public Order(int order_id, int student_id, int bike_id, int station1_id, int station2_id, String station2_name, Timestamp start_time, Timestamp end_time, int cost, int point_used, int total_amount, String report_content) {
    	this.order_id = order_id;
    	this.student_id = student_id;
	    this.bike_id = bike_id;
	    this.station1_id = station1_id;
	    this.station2_id = station2_id;
	    this.station2_name = station2_name;
	    this.start_time = start_time;
	    this.end_time = end_time;
	    this.cost = cost;
	    this.point_used = point_used;
	    this.total_amount = total_amount;
	    this.report_content = report_content;
	}
	public int getOrder_id() {
        return this.order_id;
    }
    public int getStudent_id() {
        return this.student_id;
    }
    public int getBike_id() {
        return this.bike_id;
    }
    public int getStation1_id() {
        return this.station1_id;
    }
    public String getStation1_name() {
        return this.station1_name;
    }
    public int getStation2_id() {
        return this.station2_id;
    }
    public String getStation2_name() {
        return this.station2_name;
    }
    public Timestamp getStart_time() {
        return this.start_time;
    }
    public Timestamp getEnd_time() {
        return this.end_time;
    }
    public int getCost() {
        return this.cost;
    }
    public int getPoint_used() {
        return this.point_used;
    }
    public int getTotal_amount() {
        return this.total_amount;
    }
    public String getReport_content() {
        return this.report_content;
    }
    /**
     * 更新借還表資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該借還表是否已經在資料庫 */
        if(this.order_id != 0) {
            /** 透過OrderHelper物件，更新目前之借還表資料置資料庫中 */
            data = oh.update(this);
        }
        return data;
    }
    /**
     * 取得該借還表所有資料
     *
     * @return the data 取得該借還表之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("order_id", getOrder_id());
        jso.put("student_id", getStudent_id());
        jso.put("bike_id", getBike_id());
        jso.put("station1_id", getStation1_id());
        jso.put("station2_id", getStation2_id());
        jso.put("start_time", getStart_time());
        jso.put("end_time", getEnd_time());
        jso.put("cost", getCost());
        jso.put("point_used", getPoint_used());
        jso.put("total_amount", getTotal_amount());
        jso.put("report_content", getReport_content());
        return jso;
    }
    public JSONObject getOrderData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("order_id", getOrder_id());
        jso.put("student_id", getStudent_id());
        jso.put("bike_id", getBike_id());
        jso.put("station1_id", getStation1_id());
        jso.put("station2_id", getStation2_id());
        jso.put("station2_name", getStation2_name());
        jso.put("start_time", getStart_time());
        jso.put("end_time", getEnd_time());
        jso.put("cost", getCost());
        jso.put("point_used", getPoint_used());
        jso.put("total_amount", getTotal_amount());
        jso.put("report_content", getReport_content());
        return jso;
    }
}
