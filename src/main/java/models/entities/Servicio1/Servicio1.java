package models.entities.Servicio1;

import models.entities.Servicio1.entities.RespuestaFusionComunidades;
import models.entities.Servicio1.entities.RespuestaPropuestaFusion;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Servicio1 {
  @POST("sugerencias_fusiones")
  Call<RespuestaPropuestaFusion> sugerirFusiones(
      @Body RequestBody body
  );
  @POST("fusion_comunidades")
  Call<RespuestaFusionComunidades> realizarFusiones(
      @Body RequestBody body
  );
}
