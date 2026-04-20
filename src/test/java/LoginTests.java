import org.testng.Assert;
import org.testng.annotations.Test;
import pagefactory.HomePage;
import pagefactory.LoginPage;


public class LoginTests extends BaseTest {

    //Fluent interfaces example
    @Test
    public void loginValidEmailPassword () {

        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());

        loginPage.provideEmail("carlitos@testpro.io").providePassword("4IJkPyka").clickSubmit();

        Assert.assertTrue(homePage.isAvatarDisplayed());
    }

    //    OR
    @Test
    public void loginEmptyEmailPassword() throws InterruptedException {

        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.provideEmail("").providePassword("te$t$tudent").clickSubmit();

        Thread.sleep(2000);
        Assert.assertEquals(getDriver().getCurrentUrl(), url);
    }

}