package co.malm.gae.guiatransmetro.restlet.resources.routes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import co.malm.gae.guiatransmetro.restlet.resources.DatabaseConnectedServerResource;
import co.malm.gae.guiatransmetro.restlet.util.ResultSetUtil;

public class SingleBusRouteScalesResource extends DatabaseConnectedServerResource {
	
	@Get("json")
	public String listTrunkAreas() {
		String results = "";
		
		try {
			JSONArray scaleItems = new JSONArray();
			String refRuta = (String) getRequest().getAttributes().get("ruta");
			
			final String query_portals = "SELECT e.orden, p.codigo, p.nombre, AsText(p.geometria) geometria FROM escalas e "
					+ "LEFT JOIN rutas r ON r.id = e.id_ruta LEFT JOIN paradas p ON p.id=e.id_parada "
					+ "WHERE r.codigo=? AND r.activo=0 AND e.activo=0 ORDER BY e.orden";

			boolean conditionA = refRuta != null && !refRuta.isEmpty();
			if (conditionA) {
				Connection conn = getDatabaseConnection(getContext());
				PreparedStatement pstm = conn.prepareStatement(query_portals);
				pstm.setString(1, refRuta);
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
