package introsde.rest.ehealth.resources;


import introsde.models.*;
import introsde.helper.*;

//import introsde.client.adapterClient.*;
//import introsde.client.dataBaseClient.*;


import java.util.*;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import java.lang.Exception;



@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
@Path("/get")
public class GetResources {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int id;
    String UERRELLE = UriHelper.getStorageServicesURL();
    String UERRELLE2 = UriHelper.getBusinessLogicURL();

    // EntityManager entityManager; // only used if the application is deployed in a Java EE container

    /*
    private static AdapterService getAdapter(){
        AdapterServiceImplService adapterServiceImpl = new AdapterServiceImplService();
        return adapterServiceImpl.getAdapterServiceImplPort();
    }

    private static DataBaseService getDataBaseService(){
        DataBaseServiceImplService dataBaseServiceImplService = new DataBaseServiceImplService();
        return dataBaseServiceImplService.getDataBaseServiceImplPort();
    }
    */


    // Application integration
    @GET
    @Path("/peopleList")
    @Produces({MediaType.APPLICATION_JSON })
    public List<PeopleList> getPeopleList() {
        Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(UERRELLE+"internal/people/");
		Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
		Response res = builder.get();
        System.out.println("SonoQUI");
		List<PeopleList> pp = res.readEntity(new GenericType<List<PeopleList>>(){});
        System.out.println("Sonoqua");
        return pp;
    }

    @GET
    @Path("/person/{idPerson}")
    @Produces({MediaType.APPLICATION_JSON })
    public Person getPerson(@PathParam("idPerson") int idPerson) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UERRELLE+"internal/people/"+idPerson);
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
        Response res = builder.get();
        Person pp = null;
        try{
            pp = res.readEntity(Person.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Sonoqua");
        return pp;
    }

    //people/{idPerson}/goals

    @GET
    @Path("/person/{idPerson}/goals")
    @Produces({MediaType.APPLICATION_JSON })
    public List<CustomGoal> getGoals(@PathParam("idPerson") int idPerson) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UERRELLE + "internal/people/" + idPerson + "/goals");
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
        Response res = builder.get();
        List<CustomGoal> pp = null;
        try{
            pp = res.readEntity(new GenericType<List<CustomGoal>>(){});
        } catch (Exception e){
            e.printStackTrace();
        }
        return pp;
    }

    @GET
    @Path("/person/{idPerson}/dailygoals")
    @Produces({MediaType.APPLICATION_JSON })
    public List<DailyGoal> getDailyGoals(@PathParam("idPerson") int idPerson) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UERRELLE + "internal/people/" + idPerson + "/dailygoals");
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
        Response res = builder.get();
        List<DailyGoal> pp = null;
        try{
            pp = res.readEntity(new GenericType<List<DailyGoal>>(){});
        } catch (Exception e){
            e.printStackTrace();
        }
        return pp;
    }

    @DELETE
    @Path("/person/{idPerson}/dailygoals/{dgId}")
    @Produces({MediaType.APPLICATION_JSON })
    public void deleteDailyGoal(@PathParam("idPerson") int idPerson, @PathParam("dgId") int dgId) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UERRELLE + "internal/dailygoal/" + dgId);
        Builder builder = webTarget.request();
        Response res = builder.delete();
    }

    @POST
    @Path("/person/{idPerson}/dailygoals")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    public Score getScore(@PathParam("idPerson") int idPerson,List<DailyGoal> dgl){

        Client client2 = ClientBuilder.newClient();
        System.out.println(UERRELLE2 + "get/person/" + idPerson + "/dailygoals");
        WebTarget webTarget2 = client2.target(UERRELLE2 + "get/person/" + idPerson + "/dailygoals");
        Builder builder2 = webTarget2.request(MediaType.APPLICATION_JSON);
        Response res2 = builder2.post(Entity.json(dgl));
        Score gr = res2.readEntity(Score.class);
        return gr;
    }

    // http://127.0.1.1:5700/sdelab/internal/people/1/dailygoals

    @POST
    @Path("/person/{idPerson}/dailygoals/saveorupdate")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    public DailyGoal saveOrUpdateDailyGoal(@PathParam("idPerson") int idPerson, DailyGoal dg){
        dg.setIdPerson(idPerson);
        Client client2 = ClientBuilder.newClient();
        //http://127.0.1.1:5700/sdelab/internal/people/1/dailygoals
        System.out.println(UERRELLE + "internal/people/" + idPerson + "/dailygoals");
        WebTarget webTarget2 = client2.target(UERRELLE + "internal/people/" + idPerson + "/dailygoals");
        Builder builder2 = webTarget2.request(MediaType.APPLICATION_JSON);
        Response res2 = builder2.post(Entity.json(dg));
        DailyGoal gr = res2.readEntity(DailyGoal.class);
        return gr;
    }

    @POST
    @Path("/person/{idPerson}/measure/{measureName}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    public GoalResponse saveMeasure(@PathParam("idPerson") int idPerson, Measure m, @PathParam("measureName") String measureName) {

        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target(UERRELLE + "internal/measuretype/"+measureName);
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
        Response res = builder.get();
        MeasureDefinition md = res.readEntity(MeasureDefinition.class);

        m.setMeasureDefinition(md);

        CustomMeasure cm = new CustomMeasure(md,m.getMeasureValue());

        //System.out.println(m.getMeasureDefinition().getIdMeasureDefinition());
        //System.out.println(m.getMeasureDefinition().getMeasureType());
        //System.out.println(m.getMeasureDefinition().getMeasureValueType());

        WebTarget webTarget1 = client.target(UERRELLE + "internal/people/"+ idPerson +"/measure");
        Builder builder1 = webTarget1.request(MediaType.APPLICATION_JSON);

        Response res1 =  null;

        CustomMeasure savedMeasure = null;

        try{
            //System.out.println(Entity.json(cm));
            res1 = builder1.post(Entity.json(cm));
            savedMeasure = res1.readEntity(CustomMeasure.class);
        } catch(Exception e){
            e.printStackTrace();
        }

        //System.out.println("=====================================================");
        //System.out.println(savedMeasure.getMeasureDefinition().getIdMeasureDefinition());
        //System.out.println(savedMeasure.getMeasureDefinition().getMeasureType());
        //System.out.println(savedMeasure.getMeasureDefinition().getMeasureValueType());
        //System.out.println("=====================================================");

        System.out.println(UERRELLE2 + "get/person/" + idPerson + "/measure");
        Client client2 = ClientBuilder.newClient();
        WebTarget webTarget2 = client2.target(UERRELLE2 + "get/person/" + idPerson + "/measure");
        Builder builder2 = webTarget2.request(MediaType.APPLICATION_JSON);
        Response res2 = builder2.post(Entity.json(savedMeasure));

        GoalResponse gr = res2.readEntity(GoalResponse.class);
        return gr;
    }

    @GET
    @Path("/quote")
    @Produces({MediaType.APPLICATION_JSON })
    public Quote getQuote() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UERRELLE+"external/quote");
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

        Response res = builder.get();
        System.out.println("SonoQUI");
        Quote pp = null;
        try{
        pp = res.readEntity(Quote.class);
        } catch (Exception e){
        e.printStackTrace();
        }
        System.out.println("Sonoqua");
        return pp;
    }



    /*
    @GET
    @Path("/quote")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Quote getQuote() {
        Quote quote = new Quote(getAdapter().getQuote());
        if (quote == null){
            Response res = Response.status(404).entity("404 Not Found").build();
            throw new NotFoundException("ERROR ON ExternalApi",res);
        }
        return quote;
    }

    @GET
    @Path("/people")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<Person> getPeople() {
        List<Person> peopleList = getDataBaseService().readPersonList();
        if (peopleList == null){
            Response res = Response.status(404).entity("404 Not Found").build();
            throw new NotFoundException("ERROR ON ExternalApi",res);
        }
        return peopleList;
    }

    // for the browser
    @GET
    @Produces(MediaType.TEXT_XML)
    public Person getPersonHTML() {
        Person person = this.getPersonById(id);
        if (person == null){
            //Response res = Response.status(404).build();
            throw new NotFoundException("Delete: Person with " + id
        + " not found");
            //throw new NotFoundException("Get: Person with " + id + " not found",res);
            //return Response.status(404).entity(yourMessage).type( getAcceptType()).build();
        }
        System.out.println("Returning person... " + person.getIdPerson());
        return person;
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person putPerson(Person person) {
        Response res;
        Person existing = getPersonById(this.id);

        // if the person is not in the db it will create a new one with the
        // given ID
        if (existing == null) {
            System.out.println("--> The given ID is not in our DB " + id);
            System.out.println("--> A new person will be created with this ID: " + id);
            // we set the ID that the client provided from the URI
            person.setIdPerson(id);
            // force the life status to null to be consistent with the DB
            person.setLifeStatus(null);
            //Maybe also Person.savePerson(person) could be ok?
            Person.savePerson(person);
            // actually i've putted the response to "created" because a new entity is just created
            // in the existing case the response will be a generic OK response
            res = Response.created(uriInfo.getAbsolutePath()).build();

            // in my opinion this was not very correct: res = Response.noContent().build();
        } else {
            System.out.println("--> Updating Person... " +this.id);
            System.out.println("--> "+person.toString());
            person.setIdPerson(this.id);
            person.setLifeStatus(existing.getLifeStatus());

            // We handle the case that we would like to change only some
            // properties, so we check if some "fields" are null, in a positive
            // case the old properties will be setted up to the "new" person object

            if(person.getName()==null){
                person.setName(existing.getName());
            }
            if(person.getLastname()==null){
                person.setLastname(existing.getLastname());
            }
            if(person.getBirthdate()==null){
                person.setBirthdate(existing.getBirthdate());
            }
            Person.updatePerson(person);
            // We create an "ok" response because the request have been well accomplished
            res = Response.ok().build();
        }
        return getPersonById(this.id);
    }


    @DELETE
    public void deletePerson() {
        Person person = getPersonById(id);
        if (person != null){
            Person.removePerson(person);
        }
        else{
            throw new NotFoundException("Delete: Person with " + id
        + " not found");
        }
    }

	public Person getPersonById(int personId) {
		System.out.println("Reading person from DB with id: "+personId);
		//Person person = entityManager.find(Person.class, personId);

		Person person = Person.getPersonById(personId);
		//I've added this if in order to not break things if the given ID is not in the DB
		if (person!=null){
	   		System.out.println("Person: "+person.toString());
		}
		return person;
	}


    @GET
    @Path("/lf")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<LifeStatus> getLifeStatus(){
        Person person = this.getPersonById(id);
        return person.getLifeStatus();
    }

    @GET
    @Path("{measureType}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<HealthMeasureHistory> getMeasureType(@PathParam("measureType") String measureName, @QueryParam("after") String from, @QueryParam("before") String to) throws ParseException{
        if(from != null && to != null){
            return getPersonHistoryByDate(measureName,from,to);
        }

    	//searches the measure definition associated with the name of the measure
    	MeasureDefinition mdef = new MeasureDefinition();
    	mdef = MeasureDefinition.getMeasureDefinitionByName(measureName);
    	Person person = this.getPersonById(id);
    	List<HealthMeasureHistory> hmh = new ArrayList<HealthMeasureHistory>();
		hmh = HealthMeasureHistory.getByPersonMeasureNameAndPerson(person, mdef);
    	if (hmh == null)
    		throw new RuntimeException("Get: History for person " + id + " not found");
    	return hmh;
    }

    @POST
    @Path("{measureType}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public LifeStatus setMeasureType(HealthMeasureHistory hmh_p, @PathParam("measureType") String measureName){

        Person p = Person.getPersonById(this.id);
        MeasureDefinition mdef = MeasureDefinition.getMeasureDefinitionByName(measureName);
        String value = hmh_p.getValue();

        LifeStatus olf = LifeStatus.getLifeStatusByMeasureDefinitionAndPerson(mdef,p);
        if(olf!=null){
            LifeStatus.removeLifeStatus(olf);
        }

        LifeStatus lf = new LifeStatus(p,mdef,value);
        lf.setIdMeasure(mdef.getIdMeasureDef());
        lf.setPerson(p);
        lf.setMeasureDefinition(mdef);
        //lf.setValue(hmh.getValue());
        lf = LifeStatus.saveLifeStatus(lf);

        if (hmh_p.getTimestamp() == null){
            Calendar today = Calendar.getInstance();
            hmh_p.setTimestamp(today.getTime());
        }

        hmh_p.setMeasureDefinition(mdef);
        hmh_p.setPerson(p);
        hmh_p.setValue(lf.getValue());

        HealthMeasureHistory.saveHealthMeasureHistory(hmh_p);

        return LifeStatus.getLifeStatusById(lf.getIdMeasure());
}

// SELECT *, MAX(timestamp) FROM HealthMeasureHistory GROUP BY idPerson,idMeasureDef

    @GET
    @Path("{measureType}/{mid}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public HealthMeasureHistory getMeasureTypePidAndMid(@PathParam("measureType") String measureName, @PathParam("mid") String mid){
        //System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        Person p = Person.getPersonById(this.id);
        HealthMeasureHistory hmh  = HealthMeasureHistory.getHealthMeasureHistoryByPidAndMid(p,Integer.parseInt(mid));
        //System.out.println("\n\n\n\n\nDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD\n\n\n\n\n\n");
        if (hmh == null)
            throw new RuntimeException("Get: History for person " + id + " not found");
        return hmh;//.getValue();
    }

    @PUT
    @Path("{measureType}/{mid}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response postMeasureTypePidAndMid(LifeStatus lf, @PathParam("measureType") String measureName, @PathParam("mid") String mid){
        //System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        Person p = Person.getPersonById(this.id);
        HealthMeasureHistory hmh  = HealthMeasureHistory.getHealthMeasureHistoryByPidAndMid(p,Integer.parseInt(mid));
        //System.out.println("\n\n\n\n\nDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD\n\n\n\n\n\n");
        Response res;
        if (hmh == null){
            res = Response.status(404).entity("404 Not Found").build();
            throw new NotFoundException("Get: Person with " + id + " not found",res);
        }
        hmh.setValue(lf.getValue());
        HealthMeasureHistory.updateHealthMeasureHistory(hmh);
        res = Response.ok(hmh).contentLocation(uriInfo.getAbsolutePath()).build();
        return res;
        //return hmh.getValue();
    }

//    @GET
//    @Path("{measureTypeeee}")
//    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<HealthMeasureHistory> getPersonHistoryByDate(@PathParam("measureType") String measureName, @QueryParam("after") String to, @QueryParam("before") String from) throws ParseException {
        Response res;

    	MeasureDefinition mdef = MeasureDefinition.getMeasureDefinitionByName(measureName);

    	Person person = this.getPersonById(id);

        List<HealthMeasureHistory> hmhList = new ArrayList<HealthMeasureHistory>();
    	if(from != null && to != null){
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarTo = Calendar.getInstance();
            Calendar calendarFrom = Calendar.getInstance();
        	calendarTo.setTime(dateFormat.parse(to));
        	calendarFrom.setTime(dateFormat.parse(from));
    		hmhList = HealthMeasureHistory.getMeasureByDate(mdef, person, calendarFrom, calendarTo);

    	if (hmhList == null){
    		throw new NotFoundException("No History for ID: " + id);
            }
    	}
        return hmhList;
    } */
}
