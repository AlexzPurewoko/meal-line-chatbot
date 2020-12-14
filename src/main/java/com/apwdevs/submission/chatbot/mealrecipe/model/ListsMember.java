package com.apwdevs.submission.chatbot.mealrecipe.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linecorp.bot.model.event.source.Source;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListsMember {

    private final List<Source> members;

    @JsonCreator
    public ListsMember(@JsonProperty("members") List<Source> members){
        this.members = members;
    }

    public List<Source> getMembers() {
        return this.members;
    }
}
