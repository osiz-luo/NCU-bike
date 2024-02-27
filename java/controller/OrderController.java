package controller;

import java.sql.Timestamp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import app.Order;
import app.OrderHelper;

import tool.JsonReader;

/**
 * Servlet implementation class OrderController
 */
@WebServlet("/api/order.do")
public class OrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private OrderHelper oh = OrderHelper.getHelper();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OrderController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回
		JsonReader jsr = new JsonReader(request);
		String id = jsr.getParameter("id");
		int idInt = Integer.parseInt(id);
		System.out.println("這是id :" + idInt);
		String opString = jsr.getParameter("op");
		int op = Integer.parseInt(opString);
		if (op == 1) {
			// 获取订单信息
			JSONObject jsonorder = oh.getByMaxOrderId(idInt);
			System.out.println("這是jsonorder :" + jsonorder);
			JSONArray orderDataArray = jsonorder.getJSONArray("data");
			System.out.println("這是orderDataArray :" + orderDataArray);

			// 构建响应
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "订单信息取得成功");
			resp.put("response", orderDataArray);

			// 将响应返回给前端
			jsr.response(resp, response);
			System.out.println("這是最後的resp :" + resp);
		} else {
			// 获取订单信息
			JSONObject ordersheet = oh.getByStudentID(idInt);
			// JSONArray orderDataArray = jsonorder.getJSONArray("data");

			// 构建响应
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "订单信息取得成功");
			resp.put("response", ordersheet);
			// System.out.println("ordercon");
			// System.out.println("response " + data);

			// 将响应返回给前端
			jsr.response(resp, response);

		}
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();

		/** 取出經解析到JSONObject之Request參數 */
		int bike_id = jso.getInt("bike_id");
		int station_id = jso.getInt("station_id");
		int userId = Integer.valueOf(jso.getString("student_id"));

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		/** 建立一個新的訂單物件 */
		Order o = new Order(userId, bike_id, station_id, station_id, currentTime, currentTime, 0, 0, 0, "");
		if (userId == 0 || bike_id == 0 || station_id == 0 || station_id == 0) {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
		}
		/** 透過MemberHelper物件的checkDuplicate()檢查該會員id是否有重複 */
		else {
			/** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
			JSONObject data = oh.create(o);

			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "成功註冊！");
			resp.put("response", data);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		}
//		else {
//			/** 以字串組出JSON格式之資料 */
//			String resp = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此學號重複！\', \'response\': \'\'}";
//			/** 透過JsonReader物件回傳到前端（以字串方式） */
//			jsr.response(resp, response);
//		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		System.out.println("進到doPUT了");

		/** 取出經解析到JSONObject之Request參數 */
		int bike_id = jso.getInt("bike_id");
		int station_id = jso.getInt("station_id");
		String student_id_string = jso.getString("student_id");
		int student_id = Integer.parseInt(student_id_string); // 把網頁上的student_id轉為int型態

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		System.out.println("這是bike_id :" + bike_id);
		System.out.println("這是station_id :" + station_id);
		System.out.println("這是student_id_string :" + student_id_string);
		System.out.println("這是student_id :" + student_id);
		System.out.println("這是currentTime :" + currentTime);

		// 搜尋對應的訂單
		OrderHelper oh = OrderHelper.getHelper();
		JSONObject orderData = oh.getByMaxOrderId(student_id);
		System.out.println("這是orderData :" + orderData);
		if (orderData.getJSONArray("data").length() == 0) {
			// 如果找不到訂單，返回錯誤訊息
			jsr.response("{\"status\": '400', \"message\": '找不到對應的訂單', 'response': ''}", response);
			System.out.println("找不到對應的訂單");
		} else {
			// 取得訂單並更新 station2_id
			JSONObject orderInfo = orderData.getJSONArray("data").getJSONObject(0);

			System.out.println("找到訂單了:" + orderInfo);

			Order o = new Order(orderInfo.getInt("order_id"), student_id, bike_id, orderInfo.getInt("station1_id"),
					station_id, // 新的 station2_id
					currentTime, currentTime, // 更新結束時間
					15, orderInfo.getInt("point_used"), 15, orderInfo.getString("report_content"));

			System.out.println("訂單資料:" + o);

			/** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
			JSONObject data = o.update();
			System.out.println("JSONObjectㄉdata:" + data);
			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			System.out.println("JSONObjectㄉresp:" + resp);
			resp.put("status", "200");
			resp.put("message", "成功! 更新訂單資料...");
			resp.put("response", data);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
			System.out.println("jsr.response:" + response);
		}

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
