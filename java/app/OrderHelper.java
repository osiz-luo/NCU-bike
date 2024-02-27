package app;

import java.sql.*;
import java.time.LocalDateTime;
import org.json.*;

import util.DBMgr;

public class OrderHelper {
    
    /**
     * 實例化（Instantiates）一個新的（new）OrderHelper物件<br>
     * 採用Singleton不需要透過new
     */
    private OrderHelper() {
        
    }
    
    /** 靜態變數，儲存OrderHelper物件 */
    private static OrderHelper oh;
    
    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;//util/DBMgr/getConnection
    
    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個OrderHelper物件
     *
     * @return the helper 回傳OrderHelper物件
     */
    public static OrderHelper getHelper() {
        /** Singleton檢查是否已經有OrderHelper物件，若無則new一個，若有則直接回傳 */
        if(oh == null) oh = new OrderHelper();
        
        return oh;
    }
    /**
     * 建立該名會員至資料庫
     *
     * @param m 一名會員之Member物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(Order o) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long startTime = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `ncu_sa_bike`.`tbl_order`(`student_id`, `bike_id`, `station1_id`, `station2_id`, `start_time`,`end_time`,`cost`,`point_used`,`total_amount`,`report_content`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            int student_id = o.getStudent_id();
            int bike_id = o.getBike_id();
            int station1_id = o.getStation1_id();
            int station2_id = o.getStation2_id();
            Timestamp start_time = o.getStart_time();
            Timestamp end_time = o.getEnd_time();
            int cost = o.getCost();
            int point_used = o.getPoint_used();
            int total_amount = o.getTotal_amount();
            String report_content = o.getReport_content();
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, student_id);
            pres.setInt(2, bike_id);
            pres.setInt(3, station1_id);
            pres.setInt(4, station2_id);
            pres.setTimestamp(5, start_time);
            pres.setTimestamp(6, end_time);
            pres.setInt(7, cost);
            pres.setInt(8, point_used);
            pres.setInt(9, total_amount);
            pres.setString(10, report_content);
            
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
        long duration = (end_time - startTime);

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
    public JSONObject update(Order o) {
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
            String sql = "Update `ncu_sa_bike`.`tbl_order` SET `student_id` = ? ,`bike_id` = ? ,`station1_id` = ? ,`station2_id` = ?, `end_time` = ? , `cost` = ?, `point_used` = ?, `total_amount` = ?, `report_content` = ? WHERE `order_id` = ?";
            /** 取得所需之參數 */
            int student_id = o.getStudent_id();
            int bike_id = o.getBike_id();
            int station1_id = o.getStation1_id();
            int station2_id = o.getStation2_id();
            int cost = o.getCost();
            int point_used = o.getPoint_used();
            int total_amount = o.getTotal_amount();
            String report_content = o.getReport_content();
            int order_id = o.getOrder_id();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, student_id);
            pres.setInt(2, bike_id);
            pres.setInt(3, station1_id);
            pres.setInt(4, station2_id);
            pres.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(6, cost);
            pres.setInt(7, point_used);
            pres.setInt(8, total_amount);
            pres.setString(9, report_content);
            pres.setInt(10, order_id);
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
     * 取回所有會員資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public JSONObject getAll() {
        /** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
        Order o = null;
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
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT `tbl_order`.*, `tbl_station`.`station_name` "
            		+ "FROM `ncu_sa_bike`.`tbl_order` JOIN `ncu_sa_bike`.`tbl_station` ON `tbl_order`.`station_id` = `tbl_station`.`station_id`";
            
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
                int order_id = rs.getInt("order_id");
                int student_id = rs.getInt("Student_id");
                int bike_id = rs.getInt("bike_id");
                int station1_id = rs.getInt("station1_id");
                int station2_id = rs.getInt("station2_id");
                Timestamp start_time1 = rs.getTimestamp("start_time");
                Timestamp end_time = rs.getTimestamp("end_time");
                int cost = rs.getInt("cost");
                int point_used = rs.getInt("point_used");
                int total_amount = rs.getInt("total_amount");
                String report_content = rs.getString("report_content");
                
                
                /** 將每一筆會員資料產生一名新Member物件 */
                o = new Order(order_id, student_id, bike_id, station1_id, station2_id, start_time1, end_time, cost, point_used, total_amount, report_content);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(o.getData());
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
     * 透過會員編號（ID）取得會員資料
     *
     * @param id 會員編號
     * @return the JSON object 回傳SQL執行結果與該會員編號之會員資料
     */
    public JSONObject getByID(int id) {
        /** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
        Order o = null;
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
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT `tbl_order`.*, `tbl_station`.`station_name` "
            		+ "FROM `ncu_sa_bike`.`tbl_order` JOIN `ncu_sa_bike`.`tbl_station` ON `tbl_order`.`station_id` = `tbl_station`.`station_id` "
            		+ "WHERE `order_id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int order_id = rs.getInt("order_id");
                int student_id = rs.getInt("Student_id");
                int bike_id = rs.getInt("bike_id");
                int station1_id = rs.getInt("station1_id");
                int station2_id = rs.getInt("station2_id");
                Timestamp start_time1 = rs.getTimestamp("start_time");
                Timestamp end_time = rs.getTimestamp("end_time");
                int cost = rs.getInt("cost");
                int point_used = rs.getInt("point_used");
                int total_amount = rs.getInt("total_amount");
                String report_content = rs.getString("report_content");
                
                
                /** 將每一筆會員資料產生一名新Member物件 */
                o = new Order(order_id, student_id, bike_id, station1_id, station2_id, start_time1, end_time, cost, point_used, total_amount, report_content);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(o.getData());
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
     * 透過會員編號（ID）取得會員資料
     *
     * @param id 會員編號
     * @return the JSON object 回傳SQL執行結果與該會員編號之會員資料
     */
    public JSONObject getByStudentID(int id) {//目前是給order.html使用
        /** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
        Order o = null;
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
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT `tbl_order`.*, `tbl_station`.`station_name` "
            		+ "FROM `ncu_sa_bike`.`tbl_order` JOIN `ncu_sa_bike`.`tbl_station` ON `tbl_order`.`station2_id` = `tbl_station`.`station_id` "
            		+ "WHERE `student_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int order_id = rs.getInt("order_id");
                int student_id = rs.getInt("Student_id");
                int bike_id = rs.getInt("bike_id");
                int station1_id = rs.getInt("station1_id");
                int station2_id = rs.getInt("station2_id");
                String station2_name = rs.getString("station_name");
                Timestamp start_time1 = rs.getTimestamp("start_time");
                Timestamp end_time = rs.getTimestamp("end_time");
                int cost = rs.getInt("cost");
                int point_used = rs.getInt("point_used");
                int total_amount = rs.getInt("total_amount");
                String report_content = rs.getString("report_content");
                
                
                /** 將每一筆會員資料產生一名新Member物件 */
                o = new Order(order_id, student_id, bike_id, station1_id, station2_id, station2_name, start_time1, end_time, cost, point_used, total_amount, report_content);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(o.getOrderData());
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
            String sql = "DELETE FROM `ncu_sa_bike`.`tbl_order` WHERE `order_id` = ? LIMIT 1";
            
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
    public JSONObject getByMaxOrderId(int studentId) {
	    JSONObject response = new JSONObject();
	    JSONArray jsa = new JSONArray();
	    String exexcute_sql = "";
	    long start_time = System.nanoTime();
	    int row = 0;
	    ResultSet rs = null;

	    try {
	        conn = DBMgr.getConnection();
	        String sql = "SELECT * FROM `ncu_sa_bike`.`tbl_order` WHERE `student_id` = ? ORDER BY `order_id` DESC LIMIT 1";
	        pres = conn.prepareStatement(sql);
	        pres.setInt(1, studentId); 

	        rs = pres.executeQuery();
	        exexcute_sql = pres.toString();
	        System.out.println(exexcute_sql);

	        while (rs.next()) {
	            row += 1;
	            int order_id = rs.getInt("order_id");
	            int student_id = rs.getInt("student_id");
	            int bike_id = rs.getInt("bike_id");
	            int station1_id = rs.getInt("station1_id");
	            int station2_id = rs.getInt("station2_id");
	            Timestamp start_time1 = rs.getTimestamp("start_time");
	            Timestamp end_time = rs.getTimestamp("end_time");
	            int cost = rs.getInt("cost");
	            int point_used = rs.getInt("point_used");
	            int total_amount = rs.getInt("total_amount");
	            String report_content = rs.getString("report_content");

	            Order o = new Order(order_id, student_id, bike_id, station1_id, station2_id, start_time1, end_time, cost, point_used, total_amount, report_content);
	            jsa.put(o.getData());
	        }
	    } catch (SQLException e) {
	        System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBMgr.close(rs, pres, conn);
	    }

	    long end_time = System.nanoTime();
	    long duration = (end_time - start_time);

	    response.put("sql", exexcute_sql);
	    response.put("row", row);
	    response.put("time", duration);
	    response.put("data", jsa);

	    return response;
	}}
