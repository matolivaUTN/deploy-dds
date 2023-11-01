package models.entities.Servicio1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import models.entities.Servicio1.entities.ComunidadServicio1;
import models.entities.Servicio1.entities.RequestPropuestasComunidad;
import models.entities.Servicio1.entities.RequestSugerenciasFusion;
import models.entities.Servicio1.entities.RespuestaFusionComunidades;
import models.entities.Servicio1.entities.RespuestaPropuestaFusion;
import models.entities.Servicio2.entities.ComunidadServicio2;
import models.entities.Servicio2.entities.MiembroServicio2;
import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDateTime;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class Servicio1Retrofit {
  private static Servicio1Retrofit instancia = null;
  private static int maximaCantidadRegistrosDefault = 200;
  private static final String urlApi = "http://localhost:8080";
  private Retrofit retrofit;

  private Servicio1Retrofit() {
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

  public static Servicio1Retrofit instancia(){
    if(instancia== null){
      instancia = new Servicio1Retrofit();
    }
    return instancia;
  }

  public RespuestaPropuestaFusion calcularFusiones(RequestSugerenciasFusion requestSugerenciasFusion) throws IOException {
    Servicio1 servicio1 = this.retrofit.create(Servicio1.class);

    Gson gson = new GsonBuilder()
        .create();

    String requestSugerenciasFusionJson = gson.toJson(requestSugerenciasFusion);

    RequestBody body = RequestBody.create(requestSugerenciasFusionJson.getBytes());
    Call<RespuestaPropuestaFusion> requestFusiones = servicio1.sugerirFusiones(body);
    Response<RespuestaPropuestaFusion> responseFusiones = requestFusiones.execute();
    System.out.println(responseFusiones);
    return responseFusiones.body();
  }

  public RespuestaFusionComunidades realizarFusiones(RequestPropuestasComunidad requestPropuestasComunidad) throws IOException {
    Servicio1 servicio1 = this.retrofit.create(Servicio1.class);

    Gson gson = new GsonBuilder()
        .create();

    String requestPropuestasComunidadJson = gson.toJson(requestPropuestasComunidad);

    RequestBody body = RequestBody.create(requestPropuestasComunidadJson.getBytes());
    Call<RespuestaFusionComunidades> requestFusiones = servicio1.realizarFusiones(body);
    Response<RespuestaFusionComunidades> responseFusiones = requestFusiones.execute();
    System.out.println(responseFusiones);
    return responseFusiones.body();
  }

}
