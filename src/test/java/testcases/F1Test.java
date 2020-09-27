package testcases;

import org.junit.Test;

import main.TestBase;

import static org.hamcrest.Matchers.*;

public class F1Test extends TestBase{
	
	
	@Test
	public void getSeasonsTotalWithLimit() {
		requestSpecification.when().get("f1/seasons.json?limit=46").then()
		.body("MRData.total", equalTo("71")
				,"MRData.series", equalTo("f1")
				,"MRData.SeasonTable.Seasons.size()", equalTo(46));
	}
	
	@Test
	public void getSeasonsBadRequest() {
		requestSpecification.when().get("f1/seasons.jsonlimit=46").then()
		.statusCode(404);
	}
	
	@Test
	public void getCircuitsAndValidateOne() {
		String circuitNameUT = requestSpecification.when().get("f1/circuits.json")
		.then()
		.statusCode(200)
		.body("MRData.total", equalTo("76")
				,"MRData.CircuitTable.Circuits.circuitId", hasItem("adelaide"))
		.extract()
		.path("MRData.CircuitTable.Circuits[0].circuitId");
		
		requestSpecification.when().get("f1/circuits/" + circuitNameUT + ".json")
		.then()
		.body("MRData.total", equalTo("1"),
				"MRData.CircuitTable.Circuits[0].circuitName", equalTo("Adelaide Street Circuit"),
				"MRData.CircuitTable.Circuits[0].Location.country", equalTo("Australia"),
				"MRData.CircuitTable.Circuits[0].Location.locality", equalTo("Adelaide"));
	}
	
	@Test
	public void checkCurrentRaceCountery() {
		requestSpecification.when().post("f1/current.json")
		.then()
		.statusCode(200)
		.body("MRData.total", equalTo("17"),
				"MRData.RaceTable.Races.size()", equalTo(17),
				"MRData.RaceTable.Races.round", hasItems("1","2","3","4","5", "13", "17"),
				"MRData.RaceTable.Races.find{it.round == '14'}.Circuit.Location.country", equalTo("Turkey"),
				"MRData.RaceTable.Races.find{it.round == '14'}.Circuit.circuitId", equalTo("istanbul"));
	}
	
	@Test
	public void validateRaceStatus() {
		requestSpecification.when().get("f1/status.json?limit=134")
		.then()
		.statusCode(200)
		.body("MRData.limit", equalTo("134"),
				"MRData.total", equalTo("134"),
				"MRData.StatusTable.Status.size()", equalTo(134),
				"MRData.StatusTable.Status.status", hasItems("Finished","Transmission","Collision","Power loss"),
				"MRData.StatusTable.Status.find{it.statusId == '53'}.count",equalTo("13"),
				"MRData.StatusTable.Status.find{it.statusId == '53'}.status",equalTo("+13 Laps"));
	}
	
	@Test
	public void check2010DriversAndValidateSpanish() {
		requestSpecification.when().get("f1/2010/drivers.json")
		.then()
		.statusCode(200)
		.body("MRData.limit", equalTo("30")
				,"MRData.total", equalTo("27")
				,"MRData.DriverTable.season", equalTo("2010")
				,"MRData.DriverTable.Drivers.size()", equalTo(27)
				,"MRData.DriverTable.Drivers.findAll{it.nationality == 'Spanish'}.size()", equalTo(3)
				,"MRData.DriverTable.Drivers.findAll{it.nationality == 'Spanish'}.givenName", hasItems("Jaime","Fernando","Pedro"));
	}
}
