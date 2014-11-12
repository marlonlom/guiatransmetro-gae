package co.malm.gae.guiatransmetro.restlet;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

import co.malm.gae.guiatransmetro.restlet.resources.routes.BusTrunkAreaResource;
import co.malm.gae.guiatransmetro.restlet.resources.routes.BusTrunkAreasResource;
import co.malm.gae.guiatransmetro.restlet.resources.routes.BusTrunkRoutesResource;
import co.malm.gae.guiatransmetro.restlet.resources.routes.SingleBusRouteResource;
import co.malm.gae.guiatransmetro.restlet.resources.routes.SingleBusRouteScalesResource;
import co.malm.gae.guiatransmetro.restlet.resources.stops.BusPortalsResource;
import co.malm.gae.guiatransmetro.restlet.resources.stops.SingleBusStopResource;
import co.malm.gae.guiatransmetro.restlet.resources.stops.SingleBusStopRoutesResource;

public class GuiaTransmetroRestletApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());

		Restlet world = new Restlet(getContext()) {
			public void handle(Request request, Response response) {
				String message = "Hello Restlet World!";
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		};

		router.attach("/portales", BusPortalsResource.class);
		
		router.attach("/troncales", BusTrunkAreasResource.class);
		router.attach("/troncales/{troncal}", BusTrunkAreaResource.class);
		router.attach("/troncales/{troncal}/rutas", BusTrunkRoutesResource.class);

		router.attach("/rutas/{ruta}", SingleBusRouteResource.class);
		router.attach("/rutas/{ruta}/escalas", SingleBusRouteScalesResource.class);

		router.attach("/paradas/{parada}", SingleBusStopResource.class);
		router.attach("/paradas/{parada}/rutas", SingleBusStopRoutesResource.class);
		
		router.attachDefault(world);
		
		return router;
	}
}
