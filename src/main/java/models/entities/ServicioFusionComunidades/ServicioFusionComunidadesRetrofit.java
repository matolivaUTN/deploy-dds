package models.entities.ServicioFusionComunidades;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.entities.ServicioFusionComunidades.entities.RequestPropuestasComunidad;
import models.entities.ServicioFusionComunidades.entities.RequestSugerenciasFusion;
import models.entities.ServicioFusionComunidades.entities.RespuestaFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.RespuestaPropuestaFusion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class ServicioFusionComunidadesRetrofit {
  private static ServicioFusionComunidadesRetrofit instancia = null;
  private static int maximaCantidadRegistrosDefault = 200;
  private static final String urlApi = "http://localhost:8080";
  private Retrofit retrofit;

  private ServicioFusionComunidadesRetrofit() {
    //this.retrofit = new Retrofit.Builder()
    //       .baseUrl(urlApi)
    //      .addConverterFactory(GsonConverterFactory.create())
    //     .build();
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        // Puedes configurar tu OkHttpClient aquí si es necesario
        .build();
    this.retrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(urlApi)
        .client(okHttpClient)
        .build();
  }

  public static ServicioFusionComunidadesRetrofit instancia(){
    if(instancia== null){
      instancia = new ServicioFusionComunidadesRetrofit();
    }
    return instancia;
  }

  public RespuestaPropuestaFusion calcularFusiones(RequestSugerenciasFusion requestSugerenciasFusion) throws IOException {
    ServicioFusionComunidades servicioFusionComunidades = this.retrofit.create(ServicioFusionComunidades.class);

    Gson gson = new GsonBuilder()
        .create();

    String requestSugerenciasFusionJson = gson.toJson(requestSugerenciasFusion);

    RequestBody body = RequestBody.create(requestSugerenciasFusionJson.getBytes());
    Call<RespuestaPropuestaFusion> requestFusiones = servicioFusionComunidades.sugerirFusiones(body);
    Response<RespuestaPropuestaFusion> responseFusiones = requestFusiones.execute();
    System.out.println(responseFusiones);
    return responseFusiones.body();
  }

  public RespuestaFusionComunidades realizarFusiones(RequestPropuestasComunidad requestPropuestasComunidad) throws IOException {
    ServicioFusionComunidades servicioFusionComunidades = this.retrofit.create(ServicioFusionComunidades.class);

    Gson gson = new GsonBuilder()
        .create();

    String requestPropuestasComunidadJson = gson.toJson(requestPropuestasComunidad);

    RequestBody body = RequestBody.create(requestPropuestasComunidadJson.getBytes());
    Call<RespuestaFusionComunidades> requestFusiones = servicioFusionComunidades.realizarFusiones(body);
    Response<RespuestaFusionComunidades> responseFusiones = requestFusiones.execute();
    System.out.println(responseFusiones);
    return responseFusiones.body();
  }

}
