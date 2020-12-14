package com.apwdevs.submission.chatbot.mealrecipe.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineMemberJoinedEvents {
    private final List<MemberJoinedEvents> events;

    @JsonCreator
    public LineMemberJoinedEvents(@JsonProperty("events") List<MemberJoinedEvents> events){
        this.events = events != null ? events : Collections.emptyList();
    }

    public List<MemberJoinedEvents> getEvents() {
        return events;
    }
}
