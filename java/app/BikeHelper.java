package app;

import java.sql.*;
import java.time.LocalDateTime;
import org.json.*;

import util.DBMgr;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class BikeHelper<br>
 * BikeHelper類別（class）主要管理所有與Bike相關與資料庫之方法（method）
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class BikeHelper {

	/**
	 * 實例化（Instantiates）一個新的（new）BikeHelper物件<br>
	 * 採用Singleton不需要透過new
	 */
	private BikeHelper() {

	}

	/** 靜態變數，儲存MemberHelper物件 */
	private static BikeHelper bh;

	/** 儲存JDBC資料庫連線 */
	private Connection conn = null;// util/DBMgr/getConnection

	/** 儲存JDBC預準備之SQL指令 */
	private PreparedStatement pres = null;

	/**
	 * 靜態方法<br>
	 * 實作Singleton（單例模式），僅允許建立一個BikeHelper物件
	 *
	 * @return the helper 回傳BikeHelper物件
	 */
	public static BikeHelper getHelper() {
		/** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
		if (bh == null)
			bh = new BikeHelper();

		return bh;
	}

	/**
	 * 建立該名會員至資料庫
	 *
	 * @param m 一名會員之Member物件
	 * @return the JSON object 回傳SQL指令執行之結果
	 */
	public JSONObject create(Bike b) {
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
			String sql = "INSERT INTO `ncu_sa_bike`.`tbl_bike`(`station_id`, `available`, `modified`, `created`, `bike_note`, `borrow_times`)"
					+ " VALUES(?, ?, ?, ?, ?, ?)";

			/** 取得所需之參數 */
			int id = b.getBike_id();
			int available = 1;
			String bike_note = b.getBike_note();
			int borrow_times = 0;

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setInt(1, id);
			pres.setInt(2, available);
			pres.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
			pres.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
			pres.setString(5, bike_note);
			pres.setInt(6, borrow_times);

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
	 * 更新一名會員之會員資料
	 *
	 * @param m 一名會員之Member物件
	 * @return the JSONObject 回傳SQL指令執行結果與執行之資料
	 */
	public JSONObject update(Bike b) {
		/** 紀錄回傳之資料 */
		JSONArray jsa = new JSONArray();
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
			String sql = "Update `ncu_sa_bike`.`tbl_bike` SET `station_id` = ? ,`available` = ? , `modified` = ? , `bike_note` = ?, `borrow_times` = ? WHERE `bike_id` = ?";
			/** 取得所需之參數 */
			int bike_id = b.getBike_id();
			int station_id = b.getStation_id();
			int available = b.getAvailable();
			String bike_note = b.getBike_note();
			Timestamp currentTime = b.getModified();
			int borrow_times = b.getBorrow_times();

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setInt(1, station_id);
			pres.setInt(2, available);
			pres.setTimestamp(3, currentTime);
			pres.setString(4, bike_note);
			pres.setInt(5, borrow_times);
			pres.setInt(6, bike_id);
			/** 執行更新之SQL指令並記錄影響之行數 */
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
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);

		return response;
	}

	/**
	 * 透過會員編號（ID）取得會員資料
	 *
	 * @param id 會員編號
	 * @return the JSON object 回傳SQL執行結果與該會員編號之會員資料
	 */
	public JSONObject getByID(int id) {
        /** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
        Bike b = null;
        /** 用於儲存所有檢索回之會員，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        //ResultSet rs2 = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT `tbl_bike`.*, `tbl_station`.`station_name` "
              + "FROM `ncu_sa_bike`.`tbl_bike` JOIN `ncu_sa_bike`.`tbl_station` ON `tbl_bike`.`station_id` = `tbl_station`.`station_id` "
              + "WHERE bike_id = ? LIMIT 1";
            //String sql = "SELECT * FROM `ncu_sa_bike`.`tbl_bike` WHERE bike_id = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();
            System.out.println(rs);

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                
                /** 將 ResultSet 之資料取出 */
                int bike_id = rs.getInt("bike_id");
                int station_id = rs.getInt("station_id");
                int available = rs.getInt("available");
                String bike_note = rs.getString("bike_note");
                int borrow_times = rs.getInt("borrow_times");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                String station_name = rs.getString("station_name");
                //String station_name = "YAYA";

                //String sql2 = "SELECT ststion_name FROM `ncu_sa_bike`.`tbl_station` WHERE station_id = ? LIMIT 1";
                /** 將參數回填至SQL指令當中 */
                //pres = conn.prepareStatement(sql2);
                //pres.setInt(1, station_id);
                /** 執行查詢之SQL指令並記錄其回傳之資料 */
                //rs2 = pres.executeQuery();
                //System.out.println(rs2);
                /** 紀錄真實執行的SQL指令，並印出 **/
                //exexcute_sql = pres.toString();
                //System.out.println(exexcute_sql);
                
                //String station_name = rs2.getString("station_name");
                
                
                
                /** 將每一筆會員資料產生一名新Member物件 */
                b = new Bike(bike_id, station_id, station_name, available, modified, created, bike_note, borrow_times);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(b.getData());
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
	 * 透過會員編號（ID）刪除會員
	 *
	 * @param id 會員編號
	 * @return the JSONObject 回傳SQL執行結果
	 */
	public JSONObject deleteByID(int id) {
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
			String sql = "DELETE FROM `ncu_sa_bike`.`tbl_bike` WHERE `bike_id` = ? LIMIT 1";

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

	public JSONArray getByStationID(int stationID) {
		JSONArray bikeArray = new JSONArray();

		try {
			conn = DBMgr.getConnection();
			String sql = "SELECT * FROM `ncu_sa_bike`.`tbl_bike` WHERE `station_id` = ?";
			pres = conn.prepareStatement(sql);
			pres.setInt(1, stationID);
			ResultSet rs = pres.executeQuery();

			while (rs.next()) {
				int bike_id = rs.getInt("bike_id");
				int station_id = rs.getInt("station_id");
				int available = rs.getInt("available");
				String bike_note = rs.getString("bike_note");
				// ... other columns ...

				Bike bike = new Bike(bike_id, station_id, bike_note, available, null, null, bike_note, available);
				bikeArray.put(bike.getData());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBMgr.close(pres, conn);
		}

		return bikeArray;
	}
}
