package models.entities.georef;

import models.entities.georef.entities.ListadoDeDepartamentos;
import models.entities.georef.entities.ListadoDeMunicipios;
import models.entities.georef.entities.ListadoDeProvincias;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeorefService {
  @GET("provincias")
  Call<ListadoDeProvincias> provincias();

  @GET("provincias")
  Call<ListadoDeProvincias> provincias(@Query("campos") String campos);

  @GET("departamentos")
  Call<ListadoDeDepartamentos> departamentos();

  @GET("departamentos")
  Call<ListadoDeDepartamentos> departamentos(@Query("provincia") Long idProvincia);

  @GET("municipios")
  Call<ListadoDeMunicipios> municipios(@Query("provincia") Long idProvincia);

  @GET("municipios")
  Call<ListadoDeMunicipios> municipios(@Query("provincia") Long idProvincia, @Query("campos") String campos);

  @GET("municipios")
  Call<ListadoDeMunicipios> municipios(@Query("provincia") Long idProvincia, @Query("campos") String campos, @Query("max") int max);

}
