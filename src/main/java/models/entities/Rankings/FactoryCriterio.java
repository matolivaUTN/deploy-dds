package models.entities.rankings;

public class FactoryCriterio {
    public static CriterioRanking criterio(String nombre) {
        CriterioRanking criterio = null;
        switch (nombre) {
            case "Mayor cantidad de incidentes reportados en la semana": criterio = new MayorCantidadDeIncidentesReportadosEnLaSemana(); break;
            case "Mayor promedio de tiempo de cierre de incidentes": criterio = new MayorPromedioDeTiempoDeCierreDeIncidentes(); break;
            //TODO: hacer el 3er ranking q falta
        }
        return criterio;
    }
}
