package models.entities.georef;

import models.entities.georef.entities.ListadoDeDepartamentos;
import models.entities.georef.entities.ListadoDeMunicipios;
import models.entities.georef.entities.ListadoDeProvincias;
import models.entities.georef.entities.Provincia;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ServicioGeoref {
  private static ServicioGeoref instancia = null;
  private static int maximaCantidadRegistrosDefault = 5000;
  private static final String urlApi = "https://apis.datos.gob.ar/georef/api/";
  private Retrofit retrofit;

  private ServicioGeoref() {
    this.retrofit = new Retrofit.Builder()
        .baseUrl(urlApi)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static ServicioGeoref instancia(){
    if(instancia== null){
      instancia = new ServicioGeoref();
    }
    return instancia;
  }

  public ListadoDeProvincias provincias() throws IOException {
    GeorefService georefService = this.retrofit.create(GeorefService.class);
    Call<ListadoDeProvincias> requestProvinciasArgentinas = georefService.provincias();
    Response<ListadoDeProvincias> responseProvinciasArgentinas = requestProvinciasArgentinas.execute();
    return responseProvinciasArgentinas.body();
  }

  public ListadoDeMunicipios municipiosDeProvincia(Provincia provincia) throws IOException {
    GeorefService georefService = this.retrofit.create(GeorefService.class);
    Call<ListadoDeMunicipios> requestListadoDeMunicipios = georefService.municipios(provincia.getId(), "id, nombre", maximaCantidadRegistrosDefault);
    Response<ListadoDeMunicipios> responseListadoDeMunicipios = requestListadoDeMunicipios.execute();
    return responseListadoDeMunicipios.body();
  }

  public ListadoDeDepartamentos departamentosDeProvincia(Provincia provincia) throws IOException {
    GeorefService georefService = this.retrofit.create(GeorefService.class);
    Call<ListadoDeDepartamentos> requestListadoDeDepartamentos = georefService.departamentos(provincia.getId());
    Response<ListadoDeDepartamentos> responseListadoDeDepartamentos = requestListadoDeDepartamentos.execute();
    return responseListadoDeDepartamentos.body();
  }
}