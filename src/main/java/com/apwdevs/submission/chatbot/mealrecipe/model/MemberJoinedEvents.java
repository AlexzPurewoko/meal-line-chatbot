package com.apwdevs.submission.chatbot.mealrecipe.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberJoinedEvents implements Event {

    /**
     * Token for replying to this event
     */
    private final String replyToken;

    /**
     * JSON object which contains the source of the event
     */
    private final Source source;

    private final ListsMember memberJoined;

    /**
     * Time of the event
     */
    private final Instant timestamp;

    @JsonCreator
    public MemberJoinedEvents(
            @JsonProperty("replyToken")
            final String replyToken,
            @JsonProperty("source")
            final Source source,
            @JsonProperty("joined")
            final ListsMember memberJoined,
            @JsonProperty("timestamp")
            final Instant timestamp) {
        this.replyToken = replyToken;
        this.source = source;
        this.memberJoined = memberJoined;
        this.timestamp = timestamp;
    }
    @Override
    public Source getSource() {
        return this.source;
    }

    @Override
    public Instant getTimestamp() {
        return this.timestamp;
    }

    public ListsMember getMemberJoined() {
        return memberJoined;
    }

    public String getReplyToken() {
        return replyToken;
    }
}
