package models.entities.ServicioCalculoGradoDeConfianza;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.entities.ServicioCalculoGradoDeConfianza.entities.ComunidadServicioCalculoGradoDeConfianza;
import models.entities.ServicioCalculoGradoDeConfianza.entities.MiembroServicioCalculoGradoDeConfianza;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDateTime;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class ServicioCalculoGradoDeConfianzaRetrofit {
    private static ServicioCalculoGradoDeConfianzaRetrofit instancia = null;
    private static int maximaCantidadRegistrosDefault = 200;
    private static final String urlApi = "http://localhost:7000";
    private Retrofit retrofit;

    private ServicioCalculoGradoDeConfianzaRetrofit() {
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

    public static ServicioCalculoGradoDeConfianzaRetrofit instancia(){
        if(instancia== null){
            instancia = new ServicioCalculoGradoDeConfianzaRetrofit();
        }
        return instancia;
    }

    public Double calcularPuntajeMiembro(MiembroServicioCalculoGradoDeConfianza miembro2) throws IOException {
        ServicioCalculoGradoDeConfianza servicioCalculoGradoDeConfianza = this.retrofit.create(ServicioCalculoGradoDeConfianza.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String miembroJson = gson.toJson(miembro2);


        //RequestBody body = RequestBody.create(MediaType.parse("application/json"), miembro2.toString());
        RequestBody body = RequestBody.create(miembroJson.getBytes());
        Call<Double> requestPuntos = servicioCalculoGradoDeConfianza.puntajeMiembro(body);
        Response<Double> responsePuntos = requestPuntos.execute();
        System.out.println(responsePuntos);
        return responsePuntos.body();
    }

    public Double calcularPuntajeComunidad(ComunidadServicioCalculoGradoDeConfianza comunidad2) throws IOException {
        ServicioCalculoGradoDeConfianza servicioCalculoGradoDeConfianza = this.retrofit.create(ServicioCalculoGradoDeConfianza.class);

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

        String comunidadJson = gson.toJson(comunidad2);
        RequestBody body = RequestBody.create(comunidadJson.getBytes());
        Call<Double> requestPuntos = servicioCalculoGradoDeConfianza.puntajeComunidad(body);
        Response<Double> responsePuntos = requestPuntos.execute();
        System.out.println(responsePuntos);
        return responsePuntos.body();

    }
}
