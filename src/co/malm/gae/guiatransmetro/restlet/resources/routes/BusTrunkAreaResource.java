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

public class BusTrunkAreaResource extends DatabaseConnectedServerResource {

	@Get("json")
	public String listTrunkArea() {
		String results = "";
		try {
			JSONObject trunkItem = new JSONObject();
			String refTroncal = (String) getRequest().getAttributes().get("troncal");
			final String query_portals = "SELECT troncal.codigo, troncal.nombre, troncal.color, AsText(troncal.geometria) geometria FROM troncales troncal "
					+ "WHERE troncal.activo = 0 AND troncal.codigo = ?";

			if (refTroncal != null && !refTroncal.isEmpty()) {
				Connection conn = getDatabaseConnection(getContext());
				PreparedStatement pstm = conn.prepareStatement(query_portals);
				pstm.setString(1, refTroncal);
				ResultSet resultSet = pstm.executeQuery();

				if (resultSet != null) {
					JSONArray trunkItems = ResultSetUtil.convertResultSetToJSON(resultSet);
					if (trunkItems.length() > 0) {
						trunkItem = trunkItems.getJSONObject(0);
					}
					resultSet.close();
					pstm.close();
				}
				conn.close();

				results = new JsonRepresentation(trunkItem).getText();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
}
