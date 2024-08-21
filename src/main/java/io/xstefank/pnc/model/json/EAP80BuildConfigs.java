package io.xstefank.pnc.model.json;

import java.util.List;

public class EAP80BuildConfigs {

    public int pageIndex;
    public int pageSize;
    public int totalPages;
    public int totalHits;
    public List<Artifact> content;

    @Override
    public String toString() {
        return "EAP80BuildConfigs{" +
                "pageIndex='" + pageIndex + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", totalPages='" + totalPages + '\'' +
                ", totalHits='" + totalHits + '\'' +
                ", content=" + content +
                '}';
    }
}
