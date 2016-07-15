package at.porscheinformatik.sonarqube.licensecheck.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.porscheinformatik.sonarqube.licensecheck.Dependency;

class LicenseFinder
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseFinder.class);

    private LicenseFinder()
    {
    }

    public static List<License> getLicenses(File filePath)
    {
        try
        {
            Model model = new MavenXpp3Reader().read(new FileInputStream(filePath));

            if (!model.getLicenses().isEmpty())
            {
                return model.getLicenses();
            }
            else
            {
                if (model.getParent() != null)
                {
                    Parent parent = model.getParent();
                    Dependency dependency =
                        new Dependency(parent.getGroupId() + ":" + parent.getArtifactId(), parent.getVersion(), null);
                    return getLicenses(DirectoryFinder.getPomPath(dependency, DirectoryFinder.getMavenRepsitoryDir()));
                }
                else
                {
                    return Collections.emptyList();
                }
            }

        }
        catch (IOException | XmlPullParserException e)
        {
            LOGGER.warn("Could not parse Maven POM " + filePath, e);
            return Collections.emptyList();
        }
    }

}