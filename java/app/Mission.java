package app;

import org.json.*;


public class Mission {
	private int mission_id;
	private String mission_name;
	private String content;
	private int target_time;
	private int reward;
	/** msh，MissionHelper之物件與Mission相關之資料庫方法（Sigleton） */
	private MissionHelper msh =  MissionHelper.getHelper();
	/**
     * 實例化（Instantiates）一個新的（new）Mission物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立任務資料時，產生一個新的任務
     */
	public Mission(String mission_name, String content, int target_time, int reward) {
		this.mission_name = mission_name;
	    this.content = content;
	    this.target_time = target_time;
	    this.reward = reward;
	    update();
	}
	/**
     * 實例化（Instantiates）一個新的（new）Mission物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢任務資料時，將每一筆資料新增為一個任務物件
     */
    public Mission(int mission_id, String mission_name, String content, int target_time, int reward) {
        this.mission_id = mission_id;
		this.mission_name = mission_name;
	    this.content = content;
	    this.target_time = target_time;
	    this.reward = reward;
    }
    public int getMission_id() {
        return this.mission_id;
    }
    public String getMission_name() {
        return this.mission_name;
    }
    public void setMission_name(String mission_name) {
        this.mission_name = mission_name;
    }
    public String getContent() {
        return this.content;
    }
    public int getTarget_time() {
        return this.target_time;
    }
    public int getReward() {
        return this.reward;
    }
    /**
     * 更新任務資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該任務是否已經在資料庫 */
        if(this.mission_id != 0) {
            /** 透過MissionHelper物件，更新目前之任務資料置資料庫中 */
            data = msh.update(this);
        }
        return data;
    }
    /**
     * 取得該任務所有資料
     *
     * @return the data 取得該任務之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("mission_id", getMission_id());
        jso.put("mission_name", getMission_name());
        jso.put("content", getContent());
        jso.put("target_time", getTarget_time());
        jso.put("reward", getReward());
        
        return jso;
    }
}
