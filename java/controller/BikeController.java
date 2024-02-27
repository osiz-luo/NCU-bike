package controller;

import java.io.*;
import java.sql.Timestamp;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import app.Bike;
import app.BikeHelper;
import tool.JsonReader;

/**
 * Servlet implementation class BikeInfoController
 */
@WebServlet("/api/bike.do")
public class BikeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BikeHelper bh = BikeHelper.getHelper();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BikeController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonReader jsr = new JsonReader(request);
		String stationId = jsr.getParameter("id");
		JSONObject jsonResponse = new JSONObject();

		try {
			int stationIdInt = Integer.parseInt(stationId);
			JSONArray bikeData = BikeHelper.getHelper().getByStationID(stationIdInt);

			jsonResponse.put("status", "200");
			jsonResponse.put("message", "成功獲取站點腳踏車資料");
			jsonResponse.put("response", bikeData);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.put("status", "500");
			jsonResponse.put("message", "伺服器内部錯誤");
			jsonResponse.put("response", e.getMessage());
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonResponse.toString());
	}

	/* 租借按鈕被按時呼叫rentBike方法 */
	/**
	 * 處理Http Method請求PUT方法（更新）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		System.out.println("進到doPut了！");
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		/** 取出經解析到JSONObject之Request參數 */
		int bikeid = jso.getInt("bikeid");
		System.out.println(jso.getInt("station_id"));

		System.out.println("這是getByID：" + bh.getByID(bikeid));
		/** 透過BikeHelper物件的update()方法至資料庫更新該腳踏車資料，回傳之資料為JSONObject物件 */
		/*
		 * TODO 這邊要靠BikeHelper取得腳踏車id=1的腳踏車資訊(是JSON格式) 然後把 available的值直接做修改
		 * 邏輯要寫if是1就改成0(借) if是0就改成1(還) 阿BikeHelper目前有問題所以先到這
		 */
		JSONObject jsobike = bh.getByID(bikeid);/*得到該腳踏車的SQL資訊*/
		/*實際上jsobike的樣子長以下這樣　所以要在靠下兩列程式碼去get真正要用的資訊
		*{"data":[{"station_name":"YAYA","bike_note":"狀況良好","created":"2023-12-23 18:39:18.0",
  		*"station_id":1,"available":1,"modified":"2023-12-23 18:39:18.0","bike_id":1,"borrow_times":0}],
 		* "row":1,"time":6359000,"sql":"com.mysql.cj.jdbc.ClientPreparedStatement:
 		* SELECT * FROM `ncu_sa_bike`.`tbl_bike` WHERE bike_id = 1 LIMIT 1"}
  		*/
		JSONArray bikeDataArray = jsobike.getJSONArray("data");/*得到該腳踏車的資訊陣列*/
		JSONObject bikeInfo = bikeDataArray.getJSONObject(0);/*真正得到該腳踏車的資訊*/
	    int station_id =jso.getInt("station_id")==0? bikeInfo.getInt("station_id"):jso.getInt("station_id"); /*判斷動作是租借或歸還*/
		String station_name = bikeInfo.getString("station_name");
		int available =bikeInfo.getInt("available")==1 ? 0 :1;
		int bikeBorrow_times = bikeInfo.getInt("borrow_times");
		if(jso.getInt("station_id")!=0) {
			bikeBorrow_times++;
		}
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		Bike b = new Bike(bikeid, station_id, station_name, available, currentTime ,currentTime ,"狀況良好",bikeBorrow_times);
		System.out.println("這是修改後的腳踏車租借狀態："+b.getAvailable());
		bh.update(b);
		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
		if (b.getAvailable() == 0) {
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "成功租借腳踏車！");

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		} else if (b.getAvailable() == 1){
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "成功歸還腳踏車！");

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		}
		else {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'租借失敗！\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
		}
	}

}
