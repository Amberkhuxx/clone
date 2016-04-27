package at.porscheinformatik.sonarqube.licensecheck.license;

import static at.porscheinformatik.sonarqube.licensecheck.LicenseCheckPropertyKeys.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.BatchSide;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ServerSide;

@ServerSide
@BatchSide
public class LicenseService
{
    private final Settings settings;

    public LicenseService(Settings settings)
    {
        super();
        this.settings = settings;
    }

    public List<License> getLicenses()
    {
        String licenseString = settings.getString(LICENSE_KEY);

        JsonParser parser = Json.createParser(new StringReader(licenseString));
        final List<License> licenses = new ArrayList<>();
        JsonReader jsonReader = Json.createReader(new StringReader(licenseString));
        final JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        while (parser.hasNext())
        {
            JsonParser.Event event = parser.next();
            switch (event)
            {
                case KEY_NAME:
                    if (!"name".equals(parser.getString()) && !"url".equals(parser.getString())
                        && !"osiApproved".equals(parser.getString()) && !"status".equals(parser.getString()))
                    {
                        saveLicensesToList(jsonObject, licenses, parser.getString());
                    }
                    break;
                default:
                    break;
            }
        }
        return licenses;
    }

    public String getLicensesRegex()
    {
        return settings.getString(LICENSE_REGEX);
    }

    private void saveLicensesToList(JsonObject jsonObject, List<License> licenses, String identifier)
    {
        JsonObject identifierObj = jsonObject.getJsonObject(identifier);
        if (identifierObj.containsKey("status"))
        {
            licenses.add(
                new License(identifierObj.getString("name"), identifier, identifierObj.getString("status")));
        }
        else
        {
            licenses.add(new License(identifierObj.getString("name"), identifier, "false"));
        }
    }
}
