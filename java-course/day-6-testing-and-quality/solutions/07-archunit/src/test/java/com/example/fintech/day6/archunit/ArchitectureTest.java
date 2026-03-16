package com.example.fintech.day6.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.example.fintech.day6.archunit")
class ArchitectureTest {

    @ArchTest
    ArchRule layeredArchitectureRule =
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("Web").definedBy("..web..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")
            .layer("Model").definedBy("..model..")
            .whereLayer("Web").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Web")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
            .whereLayer("Model").mayOnlyBeAccessedByLayers("Repository", "Service", "Web");

    @ArchTest
    ArchRule serviceAnnotationOnlyInServicePackage =
        noClasses()
            .that().resideOutsideOfPackage("..service..")
            .should().beAnnotatedWith(Service.class);

    @ArchTest
    ArchRule restControllerOnlyInWebPackage =
        noClasses()
            .that().resideOutsideOfPackage("..web..")
            .should().beAnnotatedWith(RestController.class);

    @ArchTest
    ArchRule controllersMustNotAccessRepositoriesDirectly =
        noClasses()
            .that().resideInPackage("..web..")
            .should().dependOnClassesThat().resideInPackage("..repository..");

    @ArchTest
    ArchRule servicesMustNotDependOnWebLayer =
        noClasses()
            .that().resideInPackage("..service..")
            .should().dependOnClassesThat().resideInPackage("..web..");

    @ArchTest
    ArchRule publicServiceMethodsMustBeTransactional =
        methods()
            .that().areDeclaredInClassesThat().resideInPackage("..service..")
            .and().arePublic()
            .should().beAnnotatedWith(Transactional.class);
}
