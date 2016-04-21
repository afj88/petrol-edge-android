package it.petroledge.spotthatcar.service;

import java.util.List;
import java.util.UUID;

import it.petroledge.spotthatcar.model.CarModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by friz on 02/04/16.
 */
public interface IApis {
    @POST("cars")
    Call<List<CarModel>> syncCars(@Body List<Long> ids);

}