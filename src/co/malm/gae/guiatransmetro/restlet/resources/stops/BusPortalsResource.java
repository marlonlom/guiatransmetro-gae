package co.malm.gae.guiatransmetro.restlet.resources.stops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import co.malm.gae.guiatransmetro.restlet.resources.DatabaseConnectedServerResource;
import co.malm.gae.guiatransmetro.restlet.util.ResultSetUtil;

public class BusPortalsResource extends DatabaseConnectedServerResource {

	@Get("json")
	public String listBusPortals() {
		String results = "";
		try {
			JSONArray jsonArray = new JSONArray();

			Connection conn = getDatabaseConnection(getContext());
			final String query_portals = "SELECT _portal.codigo codigo, _portal.nombre, AsText(_portal.geometria) geometria FROM paradas _portal "
					+ "WHERE _portal.activo = 0 AND _portal.tipo_parada = ?";
			PreparedStatement pstm = conn.prepareStatement(query_portals);
			pstm.setString(1, "Portal");
			ResultSet resultSet = pstm.executeQuery();

			if (resultSet != null) {
				jsonArray = ResultSetUtil.convertResultSetToJSON(resultSet);
				resultSet.close();
				pstm.close();
			}
			conn.close();

			results = new JsonRepresentation(jsonArray).getText();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
}
