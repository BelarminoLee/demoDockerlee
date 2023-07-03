package mz.co.attendance.control.handler;

import com.vaadin.flow.server.DefaultErrorHandler;
import com.vaadin.flow.server.ErrorEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Custom error handler used when exceptions are thrown in lambdas, or are not caught by
 * a HasErrorParameter handler.
 */
public class SessionErrorHandler extends DefaultErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SessionErrorHandler.class);
    public static final String STREAM_IS_CLOSED = "Stream is closed";
    public static final String CLOSED_CHANNEL_EXCEPTION = "java.nio.channels.ClosedChannelException";
    public static final String BROKEN_PIPE = "Broken pipe";
    public static final String CONNECTION_RESET_BY_PEER = "Connection reset by peer";
    public static final String UT010029 = "UT010029";

    @Override
    public void error(ErrorEvent event) {
        Throwable cause = event.getThrowable();
        if (cause instanceof java.nio.channels.ClosedChannelException || cause instanceof java.io.IOException && cause.getMessage() != null && (cause.getMessage().contains(UT010029) || StringUtils.contains(cause.getMessage(), CLOSED_CHANNEL_EXCEPTION) || StringUtils.contains(cause.getMessage(), STREAM_IS_CLOSED)) || StringUtils.contains(cause.getMessage(), BROKEN_PIPE) || StringUtils.contains(cause.getMessage(), CONNECTION_RESET_BY_PEER)) {
            if (LOG.isDebugEnabled()) {
                LOG.warn(StringUtils.EMPTY, cause);
            }
        } else {
            LOG.error(StringUtils.EMPTY, cause);
        }

    }
}
