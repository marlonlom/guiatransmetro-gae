package co.malm.gae.guiatransmetro.restlet.resources.routes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import co.malm.gae.guiatransmetro.restlet.resources.DatabaseConnectedServerResource;
import co.malm.gae.guiatransmetro.restlet.util.ResultSetUtil;

public class SingleBusRouteResource extends DatabaseConnectedServerResource {
	
	@Get("json")
	public String listSingleRoute() {
		String results = "";
		
		try {
			
			JSONObject routeItem = new JSONObject();
			String refRuta = (String) getRequest().getAttributes().get("ruta");
			final String query_portals = "SELECT r.codigo, r.tipo_ruta, r.nombre, AsText(r.geometria) geometria FROM rutas r "
					+ "WHERE r.codigo = ? AND r.activo = 0";

			if (refRuta != null && !refRuta.isEmpty()) {
				Connection conn = getDatabaseConnection(getContext());
				PreparedStatement pstm = conn.prepareStatement(query_portals);
				pstm.setString(1, refRuta);
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
