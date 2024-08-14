package club.someoneice.json.api;

import club.someoneice.json.PairList;

public interface IJson5Bean {
    PairList<COMMAND, ?> getTask();

    boolean isMap();

    void clean();

    enum COMMAND {
        NODE,
        NOTE,
        LINE,
        ARRAY,
        MAP
    }
}
