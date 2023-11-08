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
	
	public List<String[]> searchService(boolean pagine){
		/*String q ="";
		for (Map.Entry<String, String> entry : MAP_FIELD_DATANOVA.entrySet()) {
			if(getField(entry.getKey()).isFiltered())
				q += entry.getValue()+"="+getField(entry.getKey()).getFilter();
	    }*/
	    
	    try {
			int nbRows = pagine ? (isMoreRows() ? getMaxRows() : getMinRows()) : getSearchLimit();
			
			String res = RESTTool.get("https://datanova.laposte.fr/data-fair/api/v1/datasets/laposte-hexasmal/lines?select=code_commune_insee%2Cnom_de_la_commune%2Ccode_postal");//&qs=nom_de_la_commune%3A%22SCEAUX%22
			
			JSONObject json = new JSONObject(res);
			JSONArray data = json.getJSONArray("results");
			
		    List<String[]> rows = new ArrayList<String[]>();
			try {
				for (int i = 0; i < data.length(); i++) 
					rows.add(getRow(String.valueOf(i), data.getJSONObject(i)));
					
			} catch (JSONException e) {
				AppLog.error(null, e, getGrant());
			}
			return json.getJSONArray("results");
		} catch(Exception e) {
			AppLog.error(e, getGrant());
			return null;
		}
	    
	    
	    AppLog.info(callService().toString(), Grant.getSystemAdmin());
	    
		JSONArray data = callService();
	    List<String[]> rows = new ArrayList<String[]>();
		try {
			for (int i = 0; i < data.length(); i++) 
				rows.add(getRow(String.valueOf(i), data.getJSONObject(i)));
				
		} catch (JSONException e) {
			AppLog.error(null, e, getGrant());
		}
	    	
		return rows;
	}
	
	private String[] getRow(String rowId, JSONObject item){
		String[] row = new String[getFields().size()];
		
		row[0] = rowId;
		for (Map.Entry<String, String> entry : MAP_FIELD_DATANOVA.entrySet())
			row[getFieldIndex(entry.getKey())] = item.optString(entry.getValue(), "");
		
		return row;
	}	

	private ArrayList<String[]> getFakeRows(){
		ArrayList<String[]> rows = new ArrayList<>();
	    for(int i=0; i<10; i++)
	    	rows.add(getFakeRow(String.valueOf(i)));
	    return rows;
	}
	
	private String[] getFakeRow(String rowId){
		String[] row = new String[getFields().size()];
		
		row[0] = rowId;
		for (Map.Entry<String, String> entry : MAP_FIELD_DATANOVA.entrySet())
			row[getFieldIndex(entry.getKey())] = "hey";
		
		return row;
	}
	
	@Override
	public boolean selectService(String rowId, boolean copy) {
		setValues(getFakeRow(rowId));
		return true;
	}
}
