package app;

import org.json.*;


public class Member {
	private int student_id;
	private String email;
	private String student_name;
	private String student_password;
	private int point;
	private int mission_id;
	private int riding_time;
	/** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
	private MemberHelper mh =  MemberHelper.getHelper();
	/**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立會員資料時，產生一名新的會員
     */
	public Member(int student_id, String email, String student_name, String student_password) {
		this.student_id = student_id;
		this.email = email;
		this.student_name = student_name;
		this.student_password = student_password;
		update();
	}
	/**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢會員資料時，將每一筆資料新增為一個會員物件
     */
    public Member(int student_id, String email, String student_name, String student_password, int point, int mission_id, int riding_time) {
        this.student_id = student_id;
        this.email = email;
        this.student_name = student_name;
        this.student_password = student_password;
        this.point = point;
        this.mission_id = mission_id;
        this.riding_time = riding_time;
    }
    
    //更新接取任務相關資訊by黃宇帆
    public Member(int student_id, int point, int mission_id, int riding_time) {
		this.student_id = student_id;
		this.point = getPoint()+ point;
		this.mission_id = mission_id;
		this.riding_time = riding_time;
		mh.updateMission(this);
	}
    
    public int getStudent_id() {
        return this.student_id;
    }
    public String getEmail() {
        return this.email;
    }
    public String getStudent_name() {
        return this.student_name;
    }
    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
    public String getStudent_password() {
        return this.student_password;
    }
    public void setStudent_password(String student_password) {
        this.student_password = student_password;
    }
    public int getPoint() {
        return this.point;
    }
    public int getMission_id() {
        return this.mission_id;
    }
    public int getRiding_time() {
        return this.riding_time;
    }
    /**
     * 更新會員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.student_id != 0) {
            /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
            data = mh.update(this);
        }
        return data;
    }
    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("student_id", getStudent_id());
        jso.put("email", getEmail());
        jso.put("student_name", getStudent_name());
        jso.put("student_password", getStudent_password());
        jso.put("point", getPoint());
        jso.put("mission_id", getMission_id());
        jso.put("riding_time", getRiding_time());
        
        return jso;
    }
}
