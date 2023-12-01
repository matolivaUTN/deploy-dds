package models.entities.rankings;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterOpenCSV implements AdapterCSVWriter{
    @Override
    public void armarInformeDeRanking(List<String> nombreEntidades) {
        String pathAbsoluto = new File("informe.csv").getAbsolutePath();
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathAbsoluto))) {
            String[] header = {"Posicion", "Entidad"};
            writer.writeNext(header);
            List<String[]> data = new ArrayList<>();
            Integer i = 1;
            for(String nombreEntidad: nombreEntidades){
                data.add(new String[]{i.toString(), nombreEntidad});
                i = i + 1;
            }
            writer.writeAll(data);

        } catch (IOException e) {

        }
    }
}
