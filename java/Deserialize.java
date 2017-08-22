
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

public class Deserialize {
    public static String deserializeIt(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;

        try {
            RData data = mapper.readValue(new File("/srv/lucas1/eclipse-workspace/deisotoper/" + filename + ".json"), RData.class);

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
}
