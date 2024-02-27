package app;

import org.json.*;


public class Station {
	private int station_id;
	private int num_of_bicycle;
	private String station_name;
	private String station_note;
	private int station_full;
	/** sh，StationHelper之物件與Station相關之資料庫方法（Sigleton） */
	private StationHelper sh = StationHelper.getHelper();
	/**
     * 實例化（Instantiates）一個新的（new）Station物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立站點資料時，產生一個新的站點
     */
	public Station(int num_of_bicycle, String station_name, String station_note, int station_full) {
		this.num_of_bicycle = num_of_bicycle;
	    this.station_name = station_name;
	    this.station_note = station_note;
	    this.station_full = station_full;
	    update();
	}
	/**
     * 實例化（Instantiates）一個新的（new）Station物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢站點資料時，將每一筆資料新增為一個站點物件
     */
    public Station(int station_id, int num_of_bicycle, String station_name, String station_note, int station_full) {
		this.station_id = station_id;
    	this.num_of_bicycle = num_of_bicycle;
	    this.station_name = station_name;
	    this.station_note = station_note;
	    this.station_full = station_full;
    }
    public int getStation_id() {
        return this.station_id;
    }
    public int getNum_of_bicycle() {
        return this.num_of_bicycle;
    }
    public String getStation_name() {
        return this.station_name;
    }
    public String getStation_note() {
        return this.station_note;
    }
    public int getStation_full() {
        return this.station_full;
    }
    /**
     * 更新站點資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該站點是否已經在資料庫 */
        if(this.station_id != 0) {
            /** 透過StationHelper物件，更新目前之站點資料置資料庫中 */
            data = sh.update(this);
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
        jso.put("station_id", getStation_id());
        jso.put("num_of_bicycle", getNum_of_bicycle());
        jso.put("station_name", getStation_name());
        jso.put("station_note", getStation_note());
        jso.put("station_full", getStation_note());
        
        return jso;
    }
}
