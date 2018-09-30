package com.kaew.inv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryBusiness {
	private static final Logger log = LoggerFactory.getLogger(InventoryBusiness.class);

	public DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		return (DataSource) context.lookup("inventory");
	}

	public List<Inventory> query(Inventory inv) throws Exception {
		boolean hasId = inv.getId() != null && !"".equals(inv.getId());
		boolean hasUserName = inv.getUserName() != null && !"".equals(inv.getUserName());
		boolean hasInventoryName = inv.getInventoryName() != null && !"".equals(inv.getInventoryName());
		StringBuffer sql = new StringBuffer("select id,inventory_name,user_name from inv_inventory where 1=1 ");
		if (hasId) {
			sql.append(" and id = ? ");
		}
		if (hasInventoryName) {
			sql.append(" and inventory_name = ? ");
		}
		if (hasUserName) {
			sql.append(" and user_name = ? ");
		}
		DataSource ds = getDataSource();
		Connection con = ds.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Inventory> inventoryList = new ArrayList<Inventory>();
		try {
			ps = con.prepareStatement(sql.toString());
			int index = 1;
			if (hasId) {
				ps.setString(index++, inv.getId());
			}
			if (hasInventoryName) {
				ps.setString(index++, inv.getInventoryName());
			}
			if (hasUserName) {
				ps.setString(index++, inv.getUserName());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				Inventory inventory = new Inventory();
				inventory.setId(rs.getString("id"));
				inventory.setInventoryName(rs.getString("inventory_name"));
				inventory.setUserName(rs.getString("user_name"));
				inventoryList.add(inventory);
			}
		} catch (Exception e) {
			log.error("query", e);
			throw e;
		} finally {
			con.close();
			ps.close();
		}
		return inventoryList;
	}

	public Inventory save(Inventory inv) throws Exception {
		boolean hasId = inv.getId() != null && !"".equals(inv.getId());
		StringBuffer sql = new StringBuffer();
		if (hasId) {
			sql.append("update inv_inventory set inventory_name = ? , user_name = ? where id = ?");
		} else {
			sql.append("insert into inv_inventory(id,inventory_name,user_name) values (?,?,?) ");
		}
		DataSource ds = getDataSource();
		Connection con = ds.getConnection();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql.toString());
			int index = 1;
			if (!hasId) {
				UUID uuid = UUID.randomUUID();
				inv.setId(uuid.toString());
				ps.setString(index++, uuid.toString());
				ps.setString(index++, inv.getInventoryName());
				ps.setString(index++, inv.getUserName());
			} else {
				ps.setString(index++, inv.getInventoryName());
				ps.setString(index++, inv.getUserName());
				ps.setString(index++, inv.getId());
			}

			ps.executeUpdate();
		} catch (Exception e) {
			log.error("save", e);
			throw e;
		} finally {
			con.close();
			ps.close();
		}
		return inv;
	}

	public void delete(Inventory inv) throws Exception {
		boolean hasId = inv.getId() != null && !"".equals(inv.getId());
		if (!hasId) {
			throw new Exception("id must not be null");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("delete from inv_inventory where id = ?");
		DataSource ds = getDataSource();
		Connection con = ds.getConnection();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql.toString());
			ps.setString(1, inv.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("delete", e);
			throw e;
		} finally {
			con.close();
			ps.close();
		}
	}

	public InputStream print(Inventory inventory) throws Exception {
		StringBuffer result = new StringBuffer();
		List<Inventory> list = query(inventory);
		for (Inventory inv : list) {
			result.append(
					inv.getId()).append(",")
				.append(inv.getUserName()).append(",")
				.append(inv.getInventoryName())
				.append("\n");
		}
		return new ByteArrayInputStream(result.toString().getBytes("UTF-8"));
	}
}
