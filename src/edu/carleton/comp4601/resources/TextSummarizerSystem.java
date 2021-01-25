package edu.carleton.comp4601.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("ts")
public class TextSummarizerSystem {

	private static final String NAME = "Murad Berhanu Wikipedia Page Summarizer";

	@GET
	public String nameOfSystem() {
		return NAME;
	}

	@Path("{url}")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getSummaryUrl(@PathParam("url") String url){
		SummaryTool summary = new SummaryTool();
        summary.init(url, 15);
        summary.createIntersectionMatrixAndDictionary();

        System.out.println("SUMMMARY");
        summary.createSummary();
        summary.printSummary();
        
        int index = 1;
        String fullUrl = "https://en.wikipedia.org/wiki/"+url;
        String summaryHtml = "<body><h2>Summary of wikipedia page: <a href = '" + fullUrl + "'>"+url.replaceAll("_", " ") +"</a></h2>";
        for(Sentence sentence : summary.contentSummary){
        	summaryHtml+= index + ". ";
        	summaryHtml+=(sentence.value);
            summaryHtml+="<br>";
            index++;
        }
        summaryHtml += "</body>";
        return summaryHtml;
	}
	
	
	//Same thing, but you can set a maximum number of sentences than the default 15
	@Path("{url}/{length}")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getSummaryUrlVariableLength(@PathParam("url") String url,
			@PathParam("length") int length){
		SummaryTool summary = new SummaryTool();
        summary.init(url, length);
        summary.createIntersectionMatrixAndDictionary();

        System.out.println("SUMMMARY");
        summary.createSummary();
        summary.printSummary();
        
        int index = 1;
        String fullUrl = "https://en.wikipedia.org/wiki/"+url;
        String summaryHtml = "<body><h2>Summary of wikipedia page: <a href = '" + fullUrl + "'>"+url.replaceAll("_", " ") +"</a></h2>";
        for(Sentence sentence : summary.contentSummary){
        	summaryHtml+= index + ". ";
        	summaryHtml+=(sentence.value);
            summaryHtml+="<br>";
            index++;
        }

        summaryHtml += "</body>";
        return summaryHtml;
	}
}
