package org.yajug.users.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;

public class GridDataTest {

	@Test
	public void testGson() {
	
		final String expected = "[1,2,3]";
		
		Gson gson = new Gson();
		assertEquals(expected, gson.toJson(new Integer[]{1,2,3}));
	}
		
	/*@Test
	public void testGridFormat() {
		GridRowVo row0 = new GridRowVo();
		row0.setId("1");
		Map<String, String> cells0 = new HashMap<String, String>();
		cells0.put("firstname", "bertrand");
		cells0.put("lastname", "chevrier");
		cells0.put("email", "chevrier.bertrand@gmail.com");
		row0.setCell(cells0);
		
		GridRowVo row1 = new GridRowVo();
		row1.setId("2");
		Map<String, String> cells1 = new HashMap<String, String>();
		cells1.put("firstname", "emmylou");
		cells1.put("lastname", "boquet");
		cells1.put("email", "emmylou.boquet@gmail.com");
		row1.setCell(cells1);
		
		List<GridRowVo> rows = new ArrayList<GridRowVo>();
		rows.add(row0);
		rows.add(row1);
		
		GridVo grid = new GridVo();
		grid.setPage(1);
		grid.setTotal(2);
		grid.setRows(rows);
		
		final String expected = "{" +
				"\"page\":1," +
				"\"total\":2," +
				"\"rows\":[{" +
					"\"id\":\"1\"," +
					"\"cell\":{" +
							"\"email\":\"chevrier.bertrand@gmail.com\"," +
							"\"lastname\":\"chevrier\"," +
							"\"firstname\":\"bertrand\"" +
						"}" +
				"},{" +
					"\"id\":\"2\"," +
					"\"cell\":{" +
						"\"email\":\"emmylou.boquet@gmail.com\"," +
						"\"lastname\":\"boquet\"," +
						"\"firstname\":\"emmylou\"" +
					"}" +
				"}" +
			"]}";
		
		Gson gson = new GsonBuilder().serializeNulls().create();
		assertEquals(expected, gson.toJson(grid));
	}*/

}
