package bao.dev.serverpopeyes.Common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.text.BreakIterator;

import bao.dev.serverpopeyes.Model.Request;
import bao.dev.serverpopeyes.Model.User;
import bao.dev.serverpopeyes.Remote.IGeoCoordinates;
import bao.dev.serverpopeyes.Remote.RetrofitClient;
import retrofit2.Retrofit;

public class Common {
    public static User currentUser;
    public static Request currentRequest;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String baseUrl = "https://maps.googleapis.com";

    public static String converCodeToStatus(String code){
        if(code.equals("0")){
            return "Đã xác nhận";
        }else if(code.equals("1")){
            return "Đơn hàng đang được chuyển đến";
        }else{
            return "Đã giao !";
        }
    }

    public static IGeoCoordinates getGeoCodeService(){
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap ,int newWidth ,int newHight){
        Bitmap scaleBitmap = Bitmap.createBitmap(newWidth,newHight,Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHight/(float)bitmap.getHeight();
        float pivotX=0,pivotY=0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaleBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaleBitmap;
    }
}
