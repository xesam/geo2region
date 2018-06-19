package io.github.xesam.geo2district;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import io.github.xesam.geo.Point;
import io.github.xesam.geo2district.data.PointDeserializer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author xesamguo@gmail.com
 */
public class FileDistrictTreeLoader implements DistrictTreeLoader {


    private File treeFile;

    public FileDistrictTreeLoader(File file) {
        this.treeFile = file;
    }

    @Override
    public DistrictTree getDistrictTree() {
        try (FileReader jsonReader = new FileReader(treeFile)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Point.class, new PointDeserializer())
                    .registerTypeAdapter(DistrictTree.class, (JsonDeserializer<DistrictTree>) (json, typeOfT, context) -> {
                        District district = context.deserialize(json, District.class);
                        JsonArray jDistricts = json.getAsJsonObject().getAsJsonArray("districts");
                        List<DistrictTree> subSkeletons = context.deserialize(jDistricts, new TypeToken<List<DistrictTree>>() {
                        }.getType());
                        return new DistrictTree(district, subSkeletons);
                    })
                    .create();
            return gson.fromJson(jsonReader, new TypeToken<DistrictTree>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("load skeleton file error");
        }
    }
}