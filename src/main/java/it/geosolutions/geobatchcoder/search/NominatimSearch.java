package it.geosolutions.geobatchcoder.search;

import it.geosolutions.geobatchcoder.model.Location;
import it.geosolutions.geobatchcoder.model.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;
import fr.dudie.nominatim.model.Address;

public class NominatimSearch extends Search {

	private NominatimClient client;
	private static Logger LOG = Logger.getLogger(NominatimSearch.class
			.getCanonicalName());
//	private NominatimSearch searcher;
	private static String STATE_SUFFIX = ", Italia";
	
	public NominatimSearch(String email){
		HttpClient httpClient = new DefaultHttpClient();
		client = new JsonNominatimClient(httpClient, email);
	}
	
	@Override
	public boolean calculateGeoData(Location location) {
		
		List<Address> result = null;
		for(String el : location.getPositionsAlternatives()){
			result = new ArrayList<Address>();
//			client.
			try {
				result = client.search(el + STATE_SUFFIX);
				if (result.size() > 0) {
					Position pos = new Position();
					pos.setPosition(result.get(0));
					location.setPosition(pos);
					return true;
				}
			} catch (IOException e) {
				LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
				JOptionPane.showConfirmDialog(null, "Nominatim ti ha rifiutato a " + el);
			}
		}
		Position pos = new Position();
		//pos.setPosition();
		location.setPosition(pos);
		return false;
		
	}

}
