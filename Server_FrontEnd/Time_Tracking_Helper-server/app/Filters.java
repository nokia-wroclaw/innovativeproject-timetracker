import javax.inject.*;
import play.*;
import play.mvc.EssentialFilter;
import play.http.DefaultHttpFilters;
import play.http.HttpFilters;
import play.mvc.*;
//import play.mvc.EssentialFilter;
import play.filters.cors.CORSFilter;
import javax.inject.Inject;

//import play.http.DefaultHttpFilters;
/**
 * This class configures filters that run on every request. This
 * class is queried by Play to get a list of filters.
 *
 * Play will automatically use filters from any class called
 * <code>Filters</code> that is placed the root package. You can load filters
 * from a different class by adding a `play.http.filters` setting to
 * the <code>application.conf</code> configuration file.
 */
@Singleton
public class Filters implements HttpFilters {
/*
	@Inject
    CORSFilter corsFilter;

    /*@Inject
    public Filters( CORSFilter corsFilter)  {

    	this.corsFilter=corsFilter;

    }*/
/*    
    public EssentialFilter[] filters() {
        return new EssentialFilter[] { corsFilter.asJava() };
    }
*/    

    @Inject
    CORSFilter corsFilter;

    public EssentialFilter[] filters() {
        return new EssentialFilter[] { corsFilter.asJava() };
    }

}
