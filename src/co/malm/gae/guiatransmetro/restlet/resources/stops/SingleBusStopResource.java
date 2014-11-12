package co.malm.gae.guiatransmetro.restlet.resources.stops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import co.malm.gae.guiatransmetro.restlet.resources.DatabaseConnectedServerResource;
import co.malm.gae.guiatransmetro.restlet.util.ResultSetUtil;

public class SingleBusStopResource extends DatabaseConnectedServerResource {
	
	@Get("json")
	public String listSingleRoute() {
		String results = "";
		
		try {
			
			JSONObject routeItem = new JSONObject();
			String refParada = (String) getRequest().getAttributes().get("parada");
			final String query_portals = "SELECT p.codigo, p.tipo_parada, p.nombre, X(p.geometria) latitud, Y(p.geometria) longitud "
					+ "FROM paradas p WHERE p.codigo = ? AND p.activo = 0 ";

			if (refParada != null && !refParada.isEmpty()) {
				Connection conn = getDatabaseConnection(getContext());
				PreparedStatement pstm = conn.prepareStatement(query_portals);
				pstm.setString(1, refParada);
				ResultSet resultSet = pstm.executeQuery();

				if (resultSet != null) {
					JSONArray routeItems = ResultSetUtil.convertResultSetToJSON(resultSet);
					if (routeItems.length() > 0) {
						routeItem = routeItems.getJSONObject(0);
					}
					resultSet.close();
					pstm.close();
				}
				conn.close();

				results = new JsonRepresentation(routeItem).getText();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
}
