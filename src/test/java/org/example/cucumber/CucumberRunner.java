package org.example.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        //tags = "@smoke",
        //tags = "@positive"
        //tags = "@negative"
        tags = "@all",
        plugin = {"pretty"},
        glue = {"org.example.cucumber.stepdefs"},
        features = {"src/test/java/org/example/cucumber/features"},
        publish = false)
public class CucumberRunner {}
