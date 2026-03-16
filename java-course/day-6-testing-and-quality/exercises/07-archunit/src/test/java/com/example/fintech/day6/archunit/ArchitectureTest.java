package com.example.fintech.day6.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Exercise 07 — ArchUnit: Write Architecture Rules
 *
 * ArchUnit reads compiled bytecode and enforces structural rules. It's like
 * ESLint/import-boundaries for your Java package structure — but runs as
 * JUnit tests so CI catches violations automatically.
 *
 * The application has four packages:
 *   model      — JPA entities, plain domain objects
 *   repository — Spring Data repositories
 *   service    — Business logic (@Service)
 *   web        — REST controllers (@RestController)
 *
 * Dependency flow must be:
 *   web → service → repository → model
 *   (no skipping layers, no backwards dependencies)
 *
 * @AnalyzeClasses(packages = "...") tells ArchUnit where to scan.
 * @ArchTest fields are auto-discovered and run by the ArchUnit JUnit engine.
 *
 * TypeScript analogy: eslint-plugin-import with import/no-restricted-paths rules.
 */
@AnalyzeClasses(packages = "com.example.fintech.day6.archunit")
class ArchitectureTest {

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 1 — Layered architecture rule
    //
    // Use layeredArchitecture() to define the 4 layers and their allowed access:
    //
    //   layeredArchitecture()
    //     .consideringAllDependencies()
    //     .layer("Web").definedBy("..web..")
    //     .layer("Service").definedBy("..service..")
    //     .layer("Repository").definedBy("..repository..")
    //     .layer("Model").definedBy("..model..")
    //     .whereLayer("Web").mayOnlyBeAccessedByLayers("Service")  // ← actually Web accesses Service
    //
    // Wait — the direction is: Web calls Service, Service calls Repository, Repository uses Model.
    // The rule syntax is: whereLayer("X").mayOnlyAccessLayers("Y")
    //                  or: whereLayer("X").mayNotBeAccessedByAnyLayer()  if X is top layer
    //
    // Correct form:
    //   .whereLayer("Web").mayNotBeAccessedByAnyLayer()
    //   .whereLayer("Service").mayOnlyBeAccessedByLayers("Web")
    //   .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
    //   .whereLayer("Model").mayOnlyBeAccessedByLayers("Repository","Service","Web")
    // ──────────────────────────────────────────────────────────────────────────
    @ArchTest
    ArchRule TODO1_layeredArchitectureRule = null; // TODO: replace null with the rule

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 2 — @Service classes must be in the service package
    //
    // noClasses().that().resideOutsideOfPackage("..service..")
    //            .should().beAnnotatedWith(Service.class)
    // ──────────────────────────────────────────────────────────────────────────
    @ArchTest
    ArchRule TODO2_serviceAnnotationOnlyInServicePackage = null; // TODO

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 3 — @RestController classes must be in the web package
    //
    // noClasses().that().resideOutsideOfPackage("..web..")
    //            .should().beAnnotatedWith(RestController.class)
    // ──────────────────────────────────────────────────────────────────────────
    @ArchTest
    ArchRule TODO3_restControllerOnlyInWebPackage = null; // TODO

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 4 — Controllers must not access repositories directly
    //
    // noClasses().that().resideInPackage("..web..")
    //            .should().dependOnClassesThat().resideInPackage("..repository..")
    // ──────────────────────────────────────────────────────────────────────────
    @ArchTest
    ArchRule TODO4_controllersMustNotAccessRepositoriesDirectly = null; // TODO

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 5 — Service classes must not depend on web layer
    //
    // noClasses().that().resideInPackage("..service..")
    //            .should().dependOnClassesThat().resideInPackage("..web..")
    // ──────────────────────────────────────────────────────────────────────────
    @ArchTest
    ArchRule TODO5_servicesMustNotDependOnWebLayer = null; // TODO

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 6 — All public service methods should be annotated with @Transactional
    //
    // This is a naming/annotation convention rule. Use:
    //   methods().that().areDeclaredInClassesThat().resideInPackage("..service..")
    //            .and().arePublic()
    //            .should().beAnnotatedWith(Transactional.class)
    //            .orShould().beDeclaredIn(Object.class)
    //
    // Hint: this rule will FAIL on the current code because constructors exist.
    // Restrict to methods only, not constructors:
    //   methods() selects only methods (not constructors).
    // ──────────────────────────────────────────────────────────────────────────
    @ArchTest
    ArchRule TODO6_publicServiceMethodsMustBeTransactional = null; // TODO
}
