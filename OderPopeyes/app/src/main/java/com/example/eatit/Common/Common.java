package com.example.eatit.Common;

import com.example.eatit.Model.User;

public class Common {
    public static User currentUser;

    public static  String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "Đã xác nhận";
        }else if(status.equals("1")){
            return "Đơn hàng đang được chuyển đến";
        }else{
            return "Đã giao !";
        }
    }


}
