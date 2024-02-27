package app;

import org.json.*;


public class Admin {
	private int admin_id;
	private String admin_name;
	private String account;
	private String admin_password;
	/** ah，AdminHelper之物件與Admin相關之資料庫方法（Sigleton） */
	private AdminHelper ah =  AdminHelper.getHelper();
	/**
     * 實例化（Instantiates）一個新的（new）Admin物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立資料時，產生一名新的帳號
     */
	public Admin(String account, String admin_password, String admin_name) {
		this.account = account;
	    this.admin_password = admin_password;
	    this.admin_name = admin_name;
	    update();
	}
	/**
     * 實例化（Instantiates）一個新的（new）Admin物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢管理員資料時，將每一筆資料新增為一個管理員物件
     */
    public Admin(int admin_id, String admin_name, String account, String admin_password ) {
        this.admin_id = admin_id;
        this.admin_name = admin_name;
        this.account = account;
        this.admin_password = admin_password;
    }
    public int getAdmin_id() {
        return this.admin_id;
    }
    public String getAccount() {
        return this.account;
    }
    public String getAdmin_name() {
        return this.admin_name;
    }
    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }
    public String getAdmin_password() {
        return this.admin_password;
    }
    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    /**
     * 更新管理員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名管理員是否已經在資料庫 */
        if(this.admin_id != 0) {
            /** 透過MemberHelper物件，更新目前之管理員資料置資料庫中 */
            data = ah.update(this);
        }
        return data;
    }
    /**
     * 取得該名管理員所有資料
     *
     * @return the data 取得該名管理員之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名管理員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("admin_id", getAdmin_id());
        jso.put("account", getAccount());
        jso.put("admin_name", getAdmin_name());
        jso.put("admin_password", getAdmin_password());
        
        return jso;
    }
}
