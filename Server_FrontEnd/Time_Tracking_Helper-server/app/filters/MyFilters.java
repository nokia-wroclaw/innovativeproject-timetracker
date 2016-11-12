package filters;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;

import javax.inject.Inject;

import akka.stream.Materializer;
import play.mvc.Filter;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

public class MyFilters extends Filter {

    private final Executor exec;

    /**
     * @param mat This object is needed to handle streaming of requests
     * and responses.
     * @param exec This class is needed to execute code asynchronously.
     * It is used below by the <code>thenAsyncApply</code> method.
     */
    @Inject
    public MyFilters(Materializer mat, Executor exec) {
        super(mat);
        this.exec = exec;
    }

    @Override
    public CompletionStage<Result> apply(
        Function<RequestHeader, CompletionStage<Result>> next,
        RequestHeader requestHeader) {

        return next.apply(requestHeader).thenApplyAsync(
            result -> result.withHeader("X-ExampleFilter", "foo"),
            exec
        );
    }



}
