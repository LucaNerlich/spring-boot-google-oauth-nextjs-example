package com.example.springboot.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;
import io.github.bucket4j.local.SynchronizationStrategy;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The BucketFilter class is a filter that limits the number of requests per IP address using a token bucket algorithm.
 * If the number of requests exceeds the limit, an HTTP 429 error header is returned.
 */
@Component
public class BucketFilter implements Filter {

    // Maximum amount Requests a Client can send each minute.
    static final int TOKEN_CAPACITY = 50;
    private static final long overdraft = 50;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Creates a new Bucket object with specified IP address.
     *
     * @param ip the IP address of the bucket
     * @return the created Bucket object
     */
    private Bucket newBucket(String ip) {
        final LocalBucketBuilder builder = Bucket.builder().withSynchronizationStrategy(SynchronizationStrategy.NONE);

        /*
        https://bucket4j.com/8.3.0/toc.html#quick-start-examples

        The bucket size is 50 calls (which cannot be exceeded at any given time),
        with a "refill rate" of 10 calls per second that continually increases tokens in the bucket.
         In other words. if the client app averages 10 calls per second, it will never be throttled, and moreover,
         the client has overdraft equals to 50 calls which can be used if the average is a little bit higher than 10 calls/sec in a short time period.
         */
        final Refill refill = Refill.greedy(TOKEN_CAPACITY, Duration.ofSeconds(1));
        final Bandwidth limit = Bandwidth.classic(overdraft, refill);
        return builder.addLimit(limit).build();
    }

    /**
     * Applies the filter to the given servlet request and response.
     * The filter limits the number of requests per IP address using a token bucket algorithm.
     * If the number of requests exceeds the limit, an HTTP 429 error header is returned.
     *
     * @param request  the servlet request to be filtered
     * @param response the servlet response to be filtered
     * @param chain    the chain of filters for the request
     * @throws IOException                      if an I/O error occurs during the filter process
     * @throws jakarta.servlet.ServletException if a servlet error occurs during the filter process
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        final String ip = request.getRemoteAddr();
        final Bucket requestBucket = buckets.computeIfAbsent(ip, this::newBucket);

        // tryConsume returns true if the token can be consumed
        // If not, we return with an HTTP 429 error header
        if (!requestBucket.tryConsume(1)) {
            final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests");
            return;
        }

        chain.doFilter(request, response);
    }
}
