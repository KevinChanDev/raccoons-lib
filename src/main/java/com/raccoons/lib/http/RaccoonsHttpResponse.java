package com.raccoons.lib.http;

import org.apache.http.HttpResponse;

public class RaccoonsHttpResponse {

    private long startTime;
    private long endTime = 0;
    private HttpResponse httpResponse;

    public RaccoonsHttpResponse() {
        this.startTime = System.currentTimeMillis();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public long getDuration() {
        return endTime > 0 ? endTime - startTime : 0;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        this.endTime = System.currentTimeMillis();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
