package co.malm.gae.guiatransmetro.restlet.resources;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.restlet.Context;
import org.restlet.resource.ServerResource;

public abstract class DatabaseConnectedServerResource extends ServerResource {
	private static final String DB_CONFIGS = "/WEB-INF/database.properties";
	private static final String RESTLET_SERVLET_CONTEXT = "org.restlet.ext.servlet.ServletContext";
	private Connection dbConnection;
	private ServletContext sc;

	public Connection getDatabaseConnection(Context context) throws Exception {
		sc = (ServletContext) context.getAttributes().get(RESTLET_SERVLET_CONTEXT);

		InputStream dbConfigs = sc.getResourceAsStream(DB_CONFIGS);
		Properties dbProps = new Properties();
		dbProps.load(dbConfigs);

		String _class = dbProps.getProperty("db.class");
		String _url = dbProps.getProperty("db.url");
		String _usern = dbProps.getProperty("db.user");
		String _passw = dbProps.getProperty("db.password");

		if (dbConnection == null) {
			Class.forName(_class);
			dbConnection = DriverManager.getConnection(_url, _usern, _passw);
		}

		return dbConnection;
	}
}
