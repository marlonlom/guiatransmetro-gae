package co.malm.gae.guiatransmetro.restlet.resources.stops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import co.malm.gae.guiatransmetro.restlet.resources.DatabaseConnectedServerResource;
import co.malm.gae.guiatransmetro.restlet.util.ResultSetUtil;

public class SingleBusStopRoutesResource extends DatabaseConnectedServerResource {
	
	@Get("json")
	public String listTrunkAreas() {
		String results = "";
		
		try {
			JSONArray scaleItems = new JSONArray();
			String refParada = (String) getRequest().getAttributes().get("parada");
			
			final String sql_prepared_query = "SELECT r.codigo, r.nombre, r.tipo_ruta FROM guia_transmetro_db.escalas e "
					+ "LEFT JOIN guia_transmetro_db.rutas r ON r.id = e.id_ruta LEFT JOIN guia_transmetro_db.paradas p ON p.id = e.id_parada "
					+ "WHERE p.codigo = ? AND r.activo = 0 AND e.activo = 0 ORDER BY r.troncal, r.nombre";

			boolean conditionA = refParada != null && !refParada.isEmpty();
			if (conditionA) {
				Connection conn = getDatabaseConnection(getContext());
				PreparedStatement pstm = conn.prepareStatement(sql_prepared_query);
				pstm.setString(1, refParada);
				ResultSet resultSet = pstm.executeQuery();

				if (resultSet != null) {
					scaleItems = ResultSetUtil.convertResultSetToJSON(resultSet);
					resultSet.close();
					pstm.close();
				}
				conn.close();

				results = new JsonRepresentation(scaleItems).getText();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
}
