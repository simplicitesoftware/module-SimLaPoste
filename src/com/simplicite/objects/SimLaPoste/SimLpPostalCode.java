package com.simplicite.objects.SimLaPoste;

import java.util.*;
import org.json.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object SimLpPostalCode
 */
public class SimLpPostalCode extends ObjectService {
	private static final long serialVersionUID = 1L;
	private final Map<String, String> MAP_FIELD_DATANOVA = Map.of(
		"lpCpTownName", "nom_de_la_commune",
		"lpCpPostalCode", "code_postal",
		"lpCpInseeCode", "code_commune_insee"
	);
	private static final String DATANOVA_API = "https://datanova.laposte.fr/data-fair/api/v1/datasets/laposte-hexasmal/lines";
	
	@Override
	public long countService(){
		try{
			return (new JSONObject(RESTTool.get(getRequestUrl(1,1)))).getLong("total");
		}
		catch(Exception e){
			AppLog.error(e, getGrant());
			return super.countService();
		}
	}
	
	@Override
	public List<String[]> searchService(boolean pg){
	    try {
			String res = RESTTool.get(getRequestUrl(rowsPerPage(pg), getPage(pg)));
			
			JSONArray data = (new JSONObject(res)).getJSONArray("results");
			
		    List<String[]> rows = new ArrayList<String[]>();
			try {
				for (int i = 0; i < data.length(); i++) 
					rows.add(getRow(String.valueOf(i), data.getJSONObject(i)));
					
			} catch (JSONException e) {
				AppLog.error(null, e, getGrant());
			}
			return rows;
		} catch(Exception e) {
			AppLog.error(e, getGrant());
			return null;
		}
	}
	
	private String getRequestUrl(int rows, int page){
		String url = DATANOVA_API;
		
		url = HTTPTool.append(url, "size", rows);
		url = HTTPTool.append(url, "after", rows*page);
		
		// Declare which columns we want in results select=code_commune_insee%2Cnom_de_la_commune%2Ccode_postal
		String p = "";
		for (Map.Entry<String, String> entry : MAP_FIELD_DATANOVA.entrySet())
			p += (Tool.isEmpty(p) ? "" : ",")+entry.getValue();
		if(!Tool.isEmpty(p))
			url = HTTPTool.append(url, "select", p);
		
		// Add filters
		p = "";
		for (Map.Entry<String, String> entry : MAP_FIELD_DATANOVA.entrySet())
			if(getField(entry.getKey()).isFiltered()){
				p += Tool.isEmpty(p) ? "" : " AND ";
				p += entry.getValue()+":"+adaptFilter(getField(entry.getKey()).getFilter());
			}
		if(!Tool.isEmpty(p))
			url = HTTPTool.append(url, "qs", p);
			
		return url;
	}
	
	private String adaptFilter(String filter){
		return "("+filter.replace("%", "*")+")";
	}
	
	private String[] getRow(String rowId, JSONObject item){
		String[] row = new String[getFields().size()];
		
		row[0] = rowId;
		for (Map.Entry<String, String> entry : MAP_FIELD_DATANOVA.entrySet())
			row[getFieldIndex(entry.getKey())] = item.optString(entry.getValue(), "");
		
		return row;
	}	

	private int rowsPerPage(boolean pagine){
		return pagine ? (isMoreRows() ? getMaxRows() : getMinRows()) : getSearchLimit();
	}
	
	private int getPage(boolean pagine){
		return pagine ? getCurrentPage() : 0;
	}
}
