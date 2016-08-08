package at.porscheinformatik.sonarqube.licensecheck;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;

import at.porscheinformatik.sonarqube.licensecheck.license.LicenseService;
import at.porscheinformatik.sonarqube.licensecheck.license.LicenseSettingsService;
import at.porscheinformatik.sonarqube.licensecheck.mavendependency.MavenDependencyService;
import at.porscheinformatik.sonarqube.licensecheck.mavendependency.MavenDependencySettingsService;
import at.porscheinformatik.sonarqube.licensecheck.mavenlicense.MavenLicenseService;
import at.porscheinformatik.sonarqube.licensecheck.mavenlicense.MavenLicenseSettingsService;
import at.porscheinformatik.sonarqube.licensecheck.webservice.license.LicenseWs;
import at.porscheinformatik.sonarqube.licensecheck.webservice.mavendependency.MavenDependencyWs;
import at.porscheinformatik.sonarqube.licensecheck.webservice.mavenlicense.MavenLicenseWs;
import at.porscheinformatik.sonarqube.licensecheck.widget.DependencyCheckWidget;
import at.porscheinformatik.sonarqube.licensecheck.widget.DependencyCheckWidgetValidator;
import at.porscheinformatik.sonarqube.licensecheck.widget.UsedLicensesWidget;

public class LicenseCheckPlugin extends SonarPlugin
{
    @Override
    public List getExtensions()
    {
        return Arrays.asList(
            DependencyCheckWidget.class,
            DependencyCheckWidgetValidator.class,
            UsedLicensesWidget.class,
            ValidateLicenses.class,
            LicenseCheckSensor.class,
            LicenseCheckMetrics.class,
            LicenseCheckConfigurationPage.class,
            LicenseCheckMeasureComputer.class,
            LicenseCheckRulesDefinition.class,
            LicenseCheckPropertyKeys.class,
            LicenseService.class,
            LicenseSettingsService.class,
            LicenseWs.class,
            MavenDependencyService.class,
            MavenDependencySettingsService.class,
            MavenDependencyWs.class,
            MavenLicenseService.class,
            MavenLicenseSettingsService.class,
            MavenLicenseWs.class,
            PropertyDefinition.builder(LicenseCheckPropertyKeys.ACTIVATION_KEY)
                .category("License Check")
                .name("Activate")
                .description("Activate license check")
                .type(PropertyType.BOOLEAN)
                .defaultValue("true")
                .build());
    }
}
