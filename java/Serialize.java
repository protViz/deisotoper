
/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 * @url https://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
 */

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Serialize {
    public static String serializeIt(String filename, double[] mz, double[] intensity, int charge) {
        ObjectMapper mapper = new ObjectMapper();
        RData data = createObject(filename, mz, intensity, charge);
        String jsonInString = null;

        try {
            mapper.writeValue(new File("/srv/lucas1/eclipse-workspace/deisotoper/" + filename + ".json"), data);

            jsonInString = mapper.writeValueAsString(data);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }

    private static RData createObject(String filename, double[] mz, double[] intensity, int charge) {

        RData data = new RData();

        data.setFilename(filename);
        data.setMz(mz);
        data.setIntensity(intensity);
        data.setCharge(charge);

        return data;
    }
}
