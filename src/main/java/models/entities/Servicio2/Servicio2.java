package models.entities.Servicio2;
import models.entities.Servicio2.entities.MiembroServicio2;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import okhttp3.RequestBody;


public interface Servicio2 {
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
