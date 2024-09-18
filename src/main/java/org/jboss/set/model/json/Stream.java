package org.jboss.set.model.json;

import org.jboss.set.exception.UnknownStreamException;

public enum Stream {
    EAP_7_3_X("EAP 7.3.x", "eap:7.3.x"),
    EAP_7_4_X("EAP 7.4.x", "eap:7.4.x"),
    EAP_8_0_X("EAP 8.0.x", "eap:8.0.x"),
    XP_4_0_X("XP 4.0.x", "eap-xp:4.0.x"),
    XP_5_0_X("XP 5.0.x", "eap-xp:5.0.x");

    public final String frontEnd;
    public final String backEnd;

    Stream(String frontEnd, String backEnd) {
        this.frontEnd = frontEnd;
        this.backEnd = backEnd;
    }

    public static String getJsonStreamName(String streamFromFrontend) {
        for (Stream stream : Stream.values()) {
            if (stream.frontEnd.equals(streamFromFrontend) || stream.backEnd.equals(streamFromFrontend)) {
                return stream.backEnd;
            }
        }
        throw new UnknownStreamException("Unknown stream");
    }
}
