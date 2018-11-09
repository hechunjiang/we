package com.sven.huinews.international.entity.response;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.entity.Comment;

import java.util.ArrayList;

public class CommentReponse extends BaseResponse {

    public CommentReponse(){

    }

    private ArrayList<Comment> data;

    public ArrayList<Comment> getData() {
        return data;
    }

    public void setData(ArrayList<Comment> data) {
        this.data = data;
    }


}
