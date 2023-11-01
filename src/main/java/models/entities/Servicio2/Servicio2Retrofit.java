package models.entities.Servicio2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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


public class Servicio2Retrofit {
    private static Servicio2Retrofit instancia = null;
    private static int maximaCantidadRegistrosDefault = 200;
    private static final String urlApi = "http://localhost:7000";
    private Retrofit retrofit;

    private Servicio2Retrofit() {
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

    public static Servicio2Retrofit instancia(){
        if(instancia== null){
            instancia = new Servicio2Retrofit();
        }
        return instancia;
    }

    public Double calcularPuntajeMiembro(MiembroServicio2 miembro2) throws IOException {
        Servicio2 servicio2 = this.retrofit.create(Servicio2.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String miembroJson = gson.toJson(miembro2);


        //RequestBody body = RequestBody.create(MediaType.parse("application/json"), miembro2.toString());
        RequestBody body = RequestBody.create(miembroJson.getBytes());
        Call<Double> requestPuntos = servicio2.puntajeMiembro(body);
        Response<Double> responsePuntos = requestPuntos.execute();
        System.out.println(responsePuntos);
        return responsePuntos.body();
    }

    public Double calcularPuntajeComunidad(ComunidadServicio2 comunidad2) throws IOException {
        Servicio2 servicio2 = this.retrofit.create(Servicio2.class);

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

        String comunidadJson = gson.toJson(comunidad2);
        RequestBody body = RequestBody.create(comunidadJson.getBytes());
        Call<Double> requestPuntos = servicio2.puntajeComunidad(body);
        Response<Double> responsePuntos = requestPuntos.execute();
        System.out.println(responsePuntos);
        return responsePuntos.body();

    }
}
