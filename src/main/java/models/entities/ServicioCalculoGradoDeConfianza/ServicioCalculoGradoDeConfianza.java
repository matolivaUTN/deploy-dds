package models.entities.ServicioCalculoGradoDeConfianza;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import okhttp3.RequestBody;


public interface ServicioCalculoGradoDeConfianza {
    @POST("miembro")
    Call <Double> puntajeMiembro(
            //@Body MiembroServicio2 miembro
            @Body RequestBody body
    );
    @POST("comunidad")
    Call <Double> puntajeComunidad(
        //@Body MiembroServicio2 miembro
        @Body RequestBody body
    );
}
