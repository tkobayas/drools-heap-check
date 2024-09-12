package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.drools.core.reteoo.ReteDumper;
import org.drools.model.codegen.ExecutableModelProject;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsTest {

    private static final int RULE_NUM = 10000;

    private static final boolean EXECUTABLE_MODEL = false;

    @Test
    void buildKieBase() throws Exception {

        System.out.println("Start building KieBase : RULE_NUM = " + RULE_NUM);

        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/org/example/rules.drl",
                  ks.getResources().newInputStreamResource(new FileInputStream(new File("drl/rules_" + RULE_NUM + ".drl"))));

        ReleaseId releaseId = ks.newReleaseId("com.sample", "my-sample-a", "1.0.0");
        kfs.generateAndWritePomXML(releaseId);

        long start = System.currentTimeMillis();
        if (EXECUTABLE_MODEL) {
            ks.newKieBuilder(kfs).buildAll(ExecutableModelProject.class);
        } else {
            ks.newKieBuilder(kfs).buildAll();
        }
        System.out.println("Build time : " + (System.currentTimeMillis() - start) + " ms");

        System.gc();
        Thread.sleep(5000);
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Used memory after build: " + usedMemory / 1024 / 1024 + " MB");

        start = System.currentTimeMillis();
        KieContainer kcontainer = ks.newKieContainer(releaseId);
        System.out.println("newKieContainer time : " + (System.currentTimeMillis() - start) + " ms");

        start = System.currentTimeMillis();
        KieBase kbase = kcontainer.getKieBase();
        System.out.println("getKieBase time : " + (System.currentTimeMillis() - start) + " ms");

        System.gc();
        Thread.sleep(5000);
        usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Used memory after getKieBase: " + usedMemory / 1024 / 1024 + " MB");

//        ReteDumper.dumpRete(kbase);

        KieSession ksession = kbase.newKieSession();
        List<String> resultList = new ArrayList<>();
        ksession.setGlobal("resultList", resultList);

        FactA factA = new FactA(1, "ABCDEFG9", "A", "A", "A", "A", "A", "A", "A", "A", "A");
        FactB factB = new FactB(1, "ABCDEFG9", "A", "A", "A", "A", "A", "A", "A", "A", "A");
        FactC factC = new FactC(1, "ABCDEFG9", "A", "A", "A", "A", "A", "A", "A", "A", "A");
        FactD factD = new FactD(1, "ABCDEFG9", "A", "A", "A", "A", "A", "A", "A", "A", "A");
        FactE factE = new FactE(1, "ABCDEFG9", "A", "A", "A", "A", "A", "A", "A", "A", "A");
        ksession.insert(factA);
        ksession.insert(factB);
        ksession.insert(factC);
        ksession.insert(factD);
        ksession.insert(factE);
        ksession.fireAllRules();

        System.out.println("resultList = " + resultList);
        ksession.dispose();

    }
}
