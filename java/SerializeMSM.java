
/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 * @url https://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
 */

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class SerializeMSM {
    public static void serializeMSM() {
        final ObjectMapper mapper = new ObjectMapper();
        List<MassSpectrometryMeasurement> list = MassSpectrometryMeasurement.getMSMlist();

        JSONObject jObject = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();
            for (MassSpectrometryMeasurement e : list) {
                JSONObject massSpectrometryMeasurementJSON = new JSONObject();
                massSpectrometryMeasurementJSON.put("typ", e.getTyp());
                massSpectrometryMeasurementJSON.put("searchengine", e.getSearchEngine());
                massSpectrometryMeasurementJSON.put("mz", e.getMz());
                massSpectrometryMeasurementJSON.put("intensity", e.getIntensity());
                massSpectrometryMeasurementJSON.put("peptidmass", e.getPeptidMass());
                massSpectrometryMeasurementJSON.put("rt", e.getRt());
                massSpectrometryMeasurementJSON.put("chargestate", e.getChargeState());
                jArray.add(massSpectrometryMeasurementJSON);
            }
            jObject.put("MSMlist", jArray);
        } catch (JSONException jse) {
            jse.printStackTrace();
        }

        try {
            mapper.writeValue(new File("../MSMlist.json"), jObject);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}