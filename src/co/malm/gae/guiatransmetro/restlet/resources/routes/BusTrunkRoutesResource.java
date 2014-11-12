package co.malm.gae.guiatransmetro.restlet.resources.routes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import co.malm.gae.guiatransmetro.restlet.resources.DatabaseConnectedServerResource;
import co.malm.gae.guiatransmetro.restlet.util.ResultSetUtil;

public class BusTrunkRoutesResource extends DatabaseConnectedServerResource {
	
	@Get("json")
	public String listTrunkAreas() {
		String results = "";
		
		try {
			JSONArray trunkItems = new JSONArray();
			String refTroncal = (String) getRequest().getAttributes().get("troncal");
			
			final String query_portals = "SELECT r.codigo, r.nombre, r.tipo_ruta FROM troncales t LEFT JOIN rutas r ON r.troncal = t.id "
					+ "WHERE t.activo = 0 and r.activo = 0 and t.codigo = ? ORDER BY r.id";

			boolean conditionA = refTroncal != null && !refTroncal.isEmpty();
			if (conditionA) {
				Connection conn = getDatabaseConnection(getContext());
				PreparedStatement pstm = conn.prepareStatement(query_portals);
				pstm.setString(1, refTroncal);
				ResultSet resultSet = pstm.executeQuery();

				if (resultSet != null) {
					trunkItems = ResultSetUtil.convertResultSetToJSON(resultSet);
					resultSet.close();
					pstm.close();
				}
				conn.close();

				results = new JsonRepresentation(trunkItems).getText();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
}
