package com.sven.huinews.international.entity;

import com.sven.huinews.international.entity.response.TaskListResponse;

public class TaskWapperBean {

    /**
     * id : 25
     * title : Novice watching  task
     * title_gold : 6500
     * title_gold_type : 1
     * content : Award 6500 coins for finishing the watching tasks
     * button : Details
     * button_url : newcomersReading.html
     * button_type : 1
     * is_login : 0
     * tile_type : 2
     * key_code : noob_read_reward
     * is_activation : 1
     * expire_time : 1893427200
     * activation_time : 1514736000
     */

    private int id;
    private String title;
    private String title_gold;
    private int title_gold_type;
    private String content;
    private String button;
    private String button_url;
    private int button_type;
    private int is_login;
    private int tile_type;
    private String key_code;
    private int is_activation;
    private int expire_time;
    private int activation_time;

    public TaskWapperBean wapperBean(TaskListResponse.DataBeanX.DataBean.Menu2Bean menu2Bean){
        this.setId(menu2Bean.getId());
        this.setTitle(menu2Bean.getTitle());
        this.setTitle_gold(menu2Bean.getTitle_gold());
        this.setTitle_gold_type(menu2Bean.getTitle_gold_type());
        this.setContent(menu2Bean.getContent());
        this.setButton(menu2Bean.getButton());
        this.setButton_url(menu2Bean.getButton_url());
        this.setButton_type(menu2Bean.getButton_type());
        this.setIs_login(menu2Bean.getIs_login());
        this.setTile_type(menu2Bean.getTile_type());
        this.setKey_code(menu2Bean.getKey_code());
        this.setIs_activation(menu2Bean.getIs_activation());
        this.setExpire_time(menu2Bean.getExpire_time());
        this.setActivation_time(menu2Bean.getActivation_time());

        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_gold() {
        return title_gold;
    }

    public void setTitle_gold(String title_gold) {
        this.title_gold = title_gold;
    }

    public int getTitle_gold_type() {
        return title_gold_type;
    }

    public void setTitle_gold_type(int title_gold_type) {
        this.title_gold_type = title_gold_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getButton_url() {
        return button_url;
    }

    public void setButton_url(String button_url) {
        this.button_url = button_url;
    }

    public int getButton_type() {
        return button_type;
    }

    public void setButton_type(int button_type) {
        this.button_type = button_type;
    }

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public int getTile_type() {
        return tile_type;
    }

    public void setTile_type(int tile_type) {
        this.tile_type = tile_type;
    }

    public String getKey_code() {
        return key_code;
    }

    public void setKey_code(String key_code) {
        this.key_code = key_code;
    }

    public int getIs_activation() {
        return is_activation;
    }

    public void setIs_activation(int is_activation) {
        this.is_activation = is_activation;
    }

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public int getActivation_time() {
        return activation_time;
    }

    public void setActivation_time(int activation_time) {
        this.activation_time = activation_time;
    }
}
