package co.malm.gae.guiatransmetro.restlet.resources.routes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;

import co.malm.gae.guiatransmetro.restlet.resources.DatabaseConnectedServerResource;
import co.malm.gae.guiatransmetro.restlet.util.ResultSetUtil;

public class BusTrunkAreasResource extends DatabaseConnectedServerResource {

	@Get("json")
	public String listTrunkAreas() {
		String results = "";
		try {
			JSONArray jsonArray = new JSONArray();

			Connection conn = getDatabaseConnection(getContext());
			final String query_portals = "SELECT troncal.codigo, troncal.nombre, AsText(troncal.geometria) geometria FROM troncales troncal "
					+ "WHERE troncal.activo = 0 ";
			PreparedStatement pstm = conn.prepareStatement(query_portals);
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
