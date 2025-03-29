package model.dto;

import java.util.HashMap;
import java.util.Map;

public class StringMapDTO implements DTOInterface {

    Map<String, String> map;

    public StringMapDTO(Map<String, String> map ) {
        this.map = map;
    }

    public StringMapDTO(String key, String value) {
        this.map = new HashMap<>();
        this.map.put(key, value);
    }

    @Override
    public String getDataType() {
        return "model.dto." + this.getClass().getSimpleName();
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void set(String key, String value) {
        this.map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }
}
