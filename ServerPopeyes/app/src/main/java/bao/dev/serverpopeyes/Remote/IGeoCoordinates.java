package bao.dev.serverpopeyes.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGeoCoordinates {
    @GET("map/api/geocode/json")
    Call<String> getGeoCode(@Query("address") String address);

    @GET("map/api/directions/json")
    Call<String> getDirections(@Query("origin") String origin,@Query("destination") String destination);

}
