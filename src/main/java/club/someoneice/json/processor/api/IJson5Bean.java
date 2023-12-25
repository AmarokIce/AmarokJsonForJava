package club.someoneice.json.processor.api;

import club.someoneice.json.PairList;

public interface IJson5Bean {
    enum COMMAND {
        NODE,
        NOTE,
        LINE,
        ARRAY,
        MAP
    }

    PairList<COMMAND, ?> getTask();
    boolean isMap();
    void clean();
}
