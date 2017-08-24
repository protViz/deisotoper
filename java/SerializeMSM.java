
/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 * @url https://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/ and https://community.hortonworks.com/questions/81337/could-not-initialize-class-netsfjsonjsonconfig.html
 * @packages: jackson-all-1.9.0.jar, jackson-core-2.4.1.1.jar, jackson-databind-2.4.1.1.jar, commons-beanutils-1.7.jar, commons-collections.jar, commons-lang.jar, commons-logging-1.1.1.jar, ezmorph.jar and json-lib-2.4-jdk15.jar 
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
        List<MassSpectrometryMeasurement.MassSpectrum> list = MassSpectrometryMeasurement.getMSMlist();

        JSONObject jObject = new JSONObject();

        try {
            JSONArray jArray = new JSONArray();
            for (MassSpectrometryMeasurement.MassSpectrum e : list) {
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
            mapper.writeValue(new File("MSMlist.json"), jObject);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}