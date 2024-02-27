package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import app.Admin;
import app.AdminHelper;
import app.Member;
import tool.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class MemberController<br>
 * MemberController類別（class）主要用於處理Member相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
@WebServlet("/api/admin.do")
public class AdminController extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ah，AdminHelper之物件與Admin相關之資料庫方法（Sigleton） */
	private AdminHelper ah = AdminHelper.getHelper();

	/**
	 * 處理Http Method請求POST方法（新增資料）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();

		/** 取出經解析到JSONObject之Request參數 */
		String name = jso.getString("admin_name");
		String account = jso.getString("account");
		String password = jso.getString("admin_password");

		/** 建立一個新的會員物件 */
		Admin a = new Admin(account, password, name);
		/** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
		if (account.isEmpty() || password.isEmpty() || name.isEmpty()) {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
		}
		/** 透過MemberHelper物件的checkDuplicate()檢查該會員id是否有重複 */
		else if (!ah.checkDuplicate(a)) {
			/** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
			JSONObject data = ah.create(a);
			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "成功註冊！");
			resp.put("response", data);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		} else {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此學號重複！\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
		}
	}

	/**
	 * 處理Http Method請求GET方法（取得資料）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		/** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
		String account = jsr.getParameter("account");
		String password = jsr.getParameter("password");
		System.out.println("輸入的Account： " + account + "密碼： " + password);

		/** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
		if (account.isEmpty() && password.isEmpty()) {// 什麼參數都不給:getAll()
			/** 透過MemberHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
			JSONObject query = ah.getAll();
			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "所有會員資料取得成功");
			resp.put("response", query);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		} else if (!account.isEmpty() && !password.isEmpty()) {
			// 給account跟password:在controller內比對完回傳比對結果(其實應該整包回傳在member.java內做比對)

			JSONObject jsoadmin = ah.getByAccount(account);/* 得到該會員的SQL資訊 */
			JSONArray adminDataArray = jsoadmin.getJSONArray("data");/* 得到該會員的資訊陣列 */
			JSONObject adminInfo = adminDataArray.getJSONObject(0);/* 真正得到該會員的資訊 */
			String adminPassword = adminInfo.getString("admin_password");
			/** 透過AdminHelper物件的getByID()方法自資料庫取回該名管理員之資料，回傳之資料為JSONObject物件 */
			JSONObject query = ah.getByAccount(account);
			System.out.println("這是使用者的密碼：" + adminPassword);
			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			if (adminPassword.equals(password)) {
				resp.put("status", "200");
				resp.put("message", "登入成功");
				resp.put("response", query);
				resp.put("redirect", "admin.html");
			} else {
				resp.put("status", "500");
				resp.put("message", "登入失敗，帳號或密碼錯誤");
				resp.put("response", query);
			}
			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		} else if (account.isEmpty()) {
			/* TODO */
		} else if (!account.isEmpty() && account.isEmpty()) {// 只有給id:getByID,Payment.html要mission相關資訊,其他html如果要getByID也可以使用
			/** 透過MemberHelper物件之getByID()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
			System.out.println("這是getByID：" + ah.getByAccount(account));
			JSONObject jsomember = ah.getByAccount(account);/* 得到該會員的SQL資訊 */
			JSONArray memberDataArray = jsomember.getJSONArray("data");/* 得到該會員的資訊陣列 */
			JSONObject memberInfo = memberDataArray.getJSONObject(0);/* 真正得到該會員的資訊 */
			int memberMissionId = memberInfo.getInt("mission_id");
			/** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
			JSONObject query = ah.getByAccount(account);
			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "會員資料取得成功");
			resp.put("missionId", memberMissionId);
			resp.put("response", query);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		}
	}

	/**
	 * 處理Http Method請求DELETE方法（刪除）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();

		/** 取出經解析到JSONObject之Request參數 */
		int id = jso.getInt("id");

		/** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
		JSONObject query = ah.deleteByID(id);
		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
		resp.put("message", "會員移除成功！");
		resp.put("response", query);

		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		jsr.response(resp, response);
	}

	/**
	 * 處理Http Method請求PUT方法（更新）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */

	// 目前此功能僅適用更新會員任務相關資訊,如需其他put功能請自行增加條件 by黃宇帆
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		String op = jso.getString("op");
		if (op.equals("setmemberinfo")) {
			String account = jso.getString("account");
			String name = jso.getString("name");
			String password = jso.getString("password");
			Admin a = new Admin(account, password, name);
			/** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
			JSONObject data = ah.updateAdminInfo(a);
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "成功! 更新會員資料...");
			resp.put("response", data);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		}
		/** 取出經解析到JSONObject之Request參數 */

		else {
			//int id = jso.getInt("id");
			//int mission_id = jso.getInt("mission_id");
			//int riding_time = jso.getInt("riding_time");
		//	int point = jso.getInt("point");
			/** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
			//Admin a = new admin(id, point, mission_id, riding_time);

			/** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
			//JSONObject data = m.update();
			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			//JSONObject resp = new JSONObject();
			//resp.put("status", "200");
			//resp.put("message", "成功! 更新會員資料...");
			//resp.put("response", data);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			//jsr.response(resp, response);
		}
	}
}
