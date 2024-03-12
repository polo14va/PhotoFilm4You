package edu.uoc.epcsd.productcatalog;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.AnalyzeClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.springframework.stereotype.Service;

@AnalyzeClasses(packages = "edu.uoc.epcsd.productcatalog",
        importOptions ={ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
 class ArchitectureTest{
    @ArchTest static final ArchRule domain_service_with_spring_annotation = classes()
            .that().resideInAPackage("..domain.service..")
            .and().areAnnotatedWith(Service.class)
            .should()
            .haveSimpleNameEndingWith("Service");

    @ArchTest static final ArchRule onion_architecture_is_respected =
            Architectures.onionArchitecture()
                    .domainModels("..domain..")
                    .domainServices("..domain.service..")
                    .applicationServices("..application..")
                    .adapter("persistence", "..infrastructure.repository..")
                    .adapter("rest", "..application.rest..");
}
