import org.testng.Assert;
import org.testng.annotations.Test;
import pom.AllSongsPage;
import pom.HomePage;
import pom.LoginPage;

public class ActionsTests extends BaseTest {

//  Test #1 - POM Recap Example
    @Test
    public void playSong()  {

        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        AllSongsPage allSongs = new AllSongsPage(driver);

        loginPage.provideEmail("demo@testpro.io");
        loginPage.providePassword("te$t$tudent");
        loginPage.clickSubmit();
        homePage.chooseAllSongsList();
        allSongs.contextClickFirstSong();
        allSongs.choosePlayOption();
        Assert.assertTrue(allSongs.isSongPlaying());

    }
}
