package com.kaew.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaew.inv.Inventory;
import com.kaew.inv.InventoryBusiness;

@Path("/inventory-api")
public class InventoryApi {
	private static final Logger log = LoggerFactory.getLogger(InventoryApi.class);
	
	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public String hello() {
		return "สวัสดี";
	}

	
	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response search(Inventory inventory) throws Exception {
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		InventoryBusiness search = new InventoryBusiness();
		List<Inventory> list = search.query(inventory);
		return builder.entity(list).status(Response.Status.OK).encoding("utf-8").build();
	}

	
	@POST
	@Path("/save")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response save(Inventory inventory) throws Exception {
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		InventoryBusiness inv = new InventoryBusiness();
		Inventory result = inv.save(inventory);
		return builder.entity(result).status(Response.Status.OK).encoding("utf-8").build();
	}
	
	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response delete(Inventory inventory) throws Exception {
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		InventoryBusiness inv = new InventoryBusiness();
		inv.delete(inventory);
		return builder.status(Response.Status.OK).encoding("utf-8").build();
	}
}
