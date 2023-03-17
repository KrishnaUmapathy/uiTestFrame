package apiPW.com.api.runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = "src/main/java/apiPW/com/api/features",
        glue = "apiPW/com/api/tests",
        plugin = {"pretty","junit:target/junitreport.xml","json:target/jsonreport.json","html:target/cucumber-reports"}
)

//Runner class to initiate the tests
public class TestRunner extends  AbstractTestNGCucumberTests {

	
	
}
