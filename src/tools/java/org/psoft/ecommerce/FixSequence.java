package org.psoft.ecommerce;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.TestDataSource;


public class FixSequence extends UnitilsJUnit4 {  

	@TestDataSource
	DataSource dataSource;
	
	@Test
	public void fixCategoryToProductSequence() throws Exception{
		Connection conn = dataSource.getConnection();
		Statement select = conn.createStatement();
		Statement update = conn.createStatement();
		
		select.execute("select * from  category_to_product order by  category_id, sequence");
		ResultSet results = select.getResultSet();
		
		int previousId=-1;
		int seq = -1;
		while(results.next()){
			int cId = results.getInt("category_id");
			int pId = results.getInt("product_id");

			if (previousId==cId){
				seq++;
			} else {
				previousId=cId;
				seq=0;
			}
			
			update.addBatch("update category_to_product set sequence="+seq+" where category_id="+cId+" and product_id="+pId);
			
			
		}
		
		update.executeBatch();
	}

	@Test
	public void fixCategoryToCategorySequence() throws Exception{
		Connection conn = dataSource.getConnection();
		Statement select = conn.createStatement();
		Statement update = conn.createStatement();
		
		select.execute("select * from  category_to_category order by  from_category_id, sequence");
		ResultSet results = select.getResultSet();
		
		int previousId=-1;
		int seq = -1;
		while(results.next()){
			int fromId = results.getInt("from_category_id");
			int toId = results.getInt("to_category_id");

			if (fromId==previousId){
				seq++;
			} else {
				previousId=fromId;
				seq=0;
			}
			
			update.addBatch("update category_to_category set sequence="+seq+" where from_category_id="+fromId + " and to_category_id="+toId);
			
			
		}
		
		update.executeBatch();
	}
	
	@Test
	public void fixItemSequence() throws Exception{
		Connection conn = dataSource.getConnection();
		Statement select = conn.createStatement();
		Statement update = conn.createStatement();
		
		select.execute("select * from item order by product_id, sequence");
		ResultSet results = select.getResultSet();
		
		int previousId=-1;
		int seq = -1;
		while(results.next()){
			int pId = results.getInt("product_id");
			int id = results.getInt("id");

			if (pId==previousId){
				seq++;
			} else {
				previousId=pId;
				seq=0;
			}
			
			update.addBatch("update item set sequence="+seq+" where id=" + id);
			
			
		}
		
		update.executeBatch();
	}

}
