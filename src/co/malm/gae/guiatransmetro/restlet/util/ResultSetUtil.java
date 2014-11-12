package co.malm.gae.guiatransmetro.restlet.util;

import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility for converting ResultSets into some Output formats
 * 
 * @author MJLopezM
 */
public class ResultSetUtil {

	/**
	 * Convert a result set into a JSON Array
	 * 
	 * @param resultSet
	 * @return a JSONArray
	 * @throws Exception
	 */
	public static JSONArray convertResultSetToJSON(ResultSet resultSet)
			throws Exception {
		JSONArray jsonArray = new JSONArray();

		if (resultSet != null) {
			while (resultSet.next()) {
				int total_rows = resultSet.getMetaData().getColumnCount();
				JSONObject obj = new JSONObject();
				for (int i = 0; i < total_rows; i++) {
					int colNextIndex = i + 1;
					obj.put(resultSet.getMetaData().getColumnLabel(colNextIndex).toLowerCase(), resultSet.getObject(colNextIndex));
				}
				jsonArray.put(obj);
			}
		}

		return jsonArray;
	}
}
