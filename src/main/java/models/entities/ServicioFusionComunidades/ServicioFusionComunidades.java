package models.entities.ServicioFusionComunidades;

import models.entities.ServicioFusionComunidades.entities.RespuestaFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.RespuestaPropuestaFusion;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServicioFusionComunidades {
  @POST("sugerencias_fusiones")
  Call<RespuestaPropuestaFusion> sugerirFusiones(
      @Body RequestBody body
  );
  @POST("fusion_comunidades")
  Call<RespuestaFusionComunidades> realizarFusiones(
      @Body RequestBody body
  );
}
