import org.testng.Assert;
import org.testng.annotations.Test;
import pagefactory.HomePage;
import pagefactory.LoginPage;
import pagefactory.ProfilePage;

public class ProfileTests extends BaseTest {

    @Test
    public void changeCurrentTheme() {
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());
        ProfilePage profilePage = new ProfilePage(getDriver());

        loginPage.provideEmail("carlitos@testpro.io").providePassword("4IJkPyka").clickSubmit();
        homePage.clickProfileIcon();
        profilePage.chooseVioletTheme();

        Assert.assertTrue(profilePage.isVioletThemeSelected());

    }
}
