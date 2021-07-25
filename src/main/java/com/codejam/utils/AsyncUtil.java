package com.codejam.utils;


import com.codejam.error.ServiceErrorMessage;
import com.codejam.error.ServiceErrorsEnum;
import com.codejam.error.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class AsyncUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUtil.class);

    private static Long requestTimeoutSeconds = 10L;

    public static <T> void completeResponse(final Supplier<CompletionStage<T>> completionStageSupplier,
                                            final AsyncResponse response) {
        AsyncUtil.timeOutHandler(response);
        try {
            completionStageSupplier.get()
                    .thenAccept(successBody -> {
                        final Response successResponse = Optional.ofNullable(successBody).filter(Response.class::isInstance)
                                .map(Response.class::cast).orElseGet(() -> {
                                    return Response.status(Response.Status.OK.getStatusCode()).entity(successBody).build();
                                });
                        response.resume(successResponse);
                    }).exceptionally(throwable -> {
                response.resume(getExceptionResponse(throwable));
                return null;
            });
        } catch (Throwable throwable) {
            response.resume(getExceptionResponse(throwable));
        }
    }

    /*
     * Time out handler when request more than required time.
     */
    public static void timeOutHandler(final AsyncResponse response) {
        response.setTimeout(requestTimeoutSeconds, TimeUnit.SECONDS);
        response.setTimeoutHandler(asyncResponse ->
                asyncResponse.resume(getExceptionResponse(new ServiceException(ServiceErrorsEnum.REQUEST_TIME_OUT))));
    }

    public static Response getExceptionResponse(Throwable throwable) {
        final ServiceErrorMessage errorMessage;
        if (throwable instanceof ServiceException) {
            errorMessage = new ServiceErrorMessage((ServiceException) throwable);
        } else if (throwable.getCause() instanceof ServiceException) {
            errorMessage = new ServiceErrorMessage((ServiceException) throwable.getCause());
        } else {
            final Exception exception = (Exception) throwable;
            errorMessage = new ServiceErrorMessage(1001,
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    exception.getMessage());
        }

        LOGGER.error("API call failed with " + throwable);
        return Response.status(errorMessage.getStatusCode()).entity(errorMessage).build();
    }

}


